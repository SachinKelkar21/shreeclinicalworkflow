/*********************************************************************
Copyright 2020 Shree Eelectricals & Engineering

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
**********************************************************************/

/* ---------------------------------------------------------------------------------------
   Pin layout should be as follows:
   Signal     Pin              Pin               Pin              Pin      Pin            
            ESP8266          LCD I2C         MFRC522 board     Buzzer      Relay(5V)      
                                                                           One channel    
   ---------------------------------------------------------------------------------------
   Reset      RST                                RST
   SPI SS     D4                                 SDA
   SPI MOSI   D5                                 MOSI
   SPI MISO   D7                                 MISO
   SPI SCK    D6                                 SCK
   SERVO      D3                                                 
   SDA        D2              SDA
   SCL        D1              SCL
   5V         VV             VIN                                
   3.3V       3V3                                3.3V             
              D8                                                 +ve      VCC              
              D0                                                          In1
   GND        GND             GND                GND             GND      GND
   ---------------------------------------------------------------------------------------
                                                                  
*/

#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <SPI.h>
#include <MFRC522.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <ArduinoJson.h>
//Standard pin no.
#define SS_2_PIN  D4        
#define RST_PIN D3
#define NR_OF_READERS   1
byte ssPins[] = {SS_2_PIN};
MFRC522 mfrc522[NR_OF_READERS];
LiquidCrystal_I2C lcd(0x27, 16, 4);
#define BUZZER D8 
#define ON_Board_LED 16
#define RELAY_PIN D0

ESP8266WebServer server(80);
int readsuccess;
byte readcard[4];
char str[32] = "";
String StrUID;

//Step1>Enter Wifi id & password.
//const char* ssid = "CT/MRI";
//const char* password = "37885199";
const char* ssid = "ICU";
const char* password = "goldroof#4024";
//const char* ssid = "OT";
//const char* password = "goldroof#4024";
//const char* ssid = "IPD";
//const char* password = "goldroof#4024";

//Step2>Enter doorNo as a moduleNo & readerNo.
String moduleNo= "11";
String readerNo= "21";

//Step3>Enter application server ip & port.
String serverIp="http://192.168.1.15:8081/";

void setup() 
{
  
  lcd.backlight();
  lcd.clear();
  lcd.print("Setup....");  
  delay(500);
  
  Serial.begin(115200);   // Initiate a serial communication
  delay(500);
  while (!Serial);
  SPI.begin();      // Initiate  SPI bus
  for (uint8_t reader = 0; reader < NR_OF_READERS; reader++) {
    mfrc522[reader].PCD_Init(ssPins[reader], RST_PIN); // Init each MFRC522 card
    Serial.print(F("Reader "));
    Serial.print(reader);
    Serial.print(F(": "));
    mfrc522[reader].PCD_DumpVersionToSerial();
    lcd.clear();
    lcd.print("Door:"+moduleNo);
    delay(500);
    lcd.clear();
    lcd.print("Reader:"+readerNo);
    delay(500);
 
  }
  delay(500);
 
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, HIGH); 

  WiFi.mode(WIFI_STA);// Connect to your WiFi router
  WiFi.begin(ssid, password); // Connect to your WiFi router
  pinMode(ON_Board_LED, OUTPUT);
  digitalWrite(ON_Board_LED, HIGH); //--> Turn off Led On Board
    
  lcd.clear();
  lcd.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    lcd.print(".");
    digitalWrite(ON_Board_LED, LOW);
    delay(250);
    digitalWrite(ON_Board_LED, HIGH);
    delay(250);
  }
  digitalWrite(ON_Board_LED, HIGH);
  lcd.clear();
  lcd.print("IP Address ");
  delay(500);
  lcd.clear();
  lcd.print(WiFi.localIP());
  delay(500);
  lcd.clear();
  lcd.print("Connected !");
  delay(500);
  pinMode(BUZZER, OUTPUT);
  noTone(BUZZER);
//  lcd.init(); 
  lcd.backlight();
  lcd.clear();
  lcd.print("Put your card");  
}


void loop() {
    for (uint8_t reader = 0; reader < NR_OF_READERS; reader++) {
       
      if (mfrc522[reader].PICC_IsNewCardPresent() && mfrc522[reader].PICC_ReadCardSerial()) {
        
       dump_byte_array(mfrc522[reader].uid.uidByte, mfrc522[reader].uid.size);
        
       for (int i = 0; i < mfrc522[reader].uid.size; i++) {
           readcard[i] = mfrc522[reader].uid.uidByte[i]; 
           array_to_string(readcard, 4, str);
           StrUID = str;
       }
      lcd.clear();
      lcd.print("******"+StrUID.substring(4, StrUID.length()));
      tone(BUZZER, 500);
      delay(300);
      noTone(BUZZER);
      digitalWrite(ON_Board_LED, LOW);
      String UIDresultSend, postData;
      UIDresultSend = StrUID;
      postData = "UIDresult=" + UIDresultSend;
      
      HTTPClient http;
      http.begin(serverIp+"personDeptTag/requestPersonDTO?tagId="+UIDresultSend+"&doorNo="+moduleNo+"&readerNo="+readerNo+"");
      http.addHeader("Content-Type", "application/json"); 
        
      int httpCode = http.POST(postData);
      String payload = http.getString(); 

      char  buf[100 + 1];
      payload.toCharArray(buf, 100 + 1); 
        
      int bufLength = sizeof(buf);
      StaticJsonBuffer<500> jsonStringBuffer;
      JsonObject& jsonString = jsonStringBuffer.parseObject(buf);
      
      String access = jsonString["access"];
      String code = jsonString["code"];
  
      if (httpCode==200 && access=="true") {
          lcd.clear();
          lcd.print("Authorized access");
          lcd.setCursor(0,1);
          lcd.print(code);
          delay(500);
          tone(BUZZER, 500);
          delay(300);
          noTone(BUZZER);
          digitalWrite(RELAY_PIN, LOW); 
          delay(5000); // 5 seconds
          digitalWrite(RELAY_PIN, HIGH);
      }
      else if (httpCode==200 && access=="false"){
          tone(BUZZER, 300);
          lcd.clear();
          lcd.print("Access denied !");
          lcd.setCursor(0,1);
          lcd.print(code);
          delay(1000);
          noTone(BUZZER);
      } else {
          lcd.clear();
          lcd.print("Access due to ");
          lcd.setCursor(0,1);
          lcd.print("Server down ! ");
          delay(500);
          tone(BUZZER, 800);
          delay(300);
          noTone(BUZZER);
          digitalWrite(RELAY_PIN, LOW);
          delay(5000);
          digitalWrite(RELAY_PIN, HIGH);
      }
      http.end();  
      delay(1000);
      digitalWrite(ON_Board_LED, HIGH);
      lcd.clear();
      lcd.print("Put your card");
 
      Serial.println();
      Serial.print(F("PICC type: "));
      MFRC522::PICC_Type piccType = mfrc522[reader].PICC_GetType(mfrc522[reader].uid.sak);
      Serial.println(mfrc522[reader].PICC_GetTypeName(piccType));
      // Halt PICC
       mfrc522[reader].PICC_HaltA();
      // Stop encryption on PCD
        mfrc522[reader].PCD_StopCrypto1();
      } //if (mfrc522[reader].PICC_IsNewC
   } //for(uint8_t reader
}

void array_to_string(byte array[], unsigned int len, char buffer[]) {
  for (unsigned int i = 0; i < len; i++){
    byte nib1 = (array[i] >> 4) & 0x0F;
    byte nib2 = (array[i] >> 0) & 0x0F;
    buffer[i * 2 + 0] = nib1  < 0xA ? '0' + nib1  : 'A' + nib1  - 0xA;
    buffer[i * 2 + 1] = nib2  < 0xA ? '0' + nib2  : 'A' + nib2  - 0xA;
  }
  buffer[len * 2] = '\0';
}
/**
 * Helper routine to dump a byte array as hex values to Serial.
 */
void dump_byte_array(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}

 
