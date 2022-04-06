package com.shree.clinicalworkflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

public class ClinicalworkflowApplicationTraceTest {
	protected Logger logger = Logger.getLogger(ClinicalworkflowApplicationTraceTest.class.getName());

	TestRestTemplate template = new TestRestTemplate();
	
	
	public void testGetReport() throws InterruptedException {
		logger.info("Test finished");
		
		
		String url = "http://localhost:8081/personDeptTag/requestPersonDTO?tagId=295336F0&doorNo=1";

		Map<String, String> params = new HashMap<String, String>();
		params.put("tagId", "295336F0");
		params.put("doorNo", "1");
		
		RestTemplate restTemplate = new RestTemplate();

		
		
		List<HttpStatus> responses = new ArrayList<>();
		Random r = new Random();
		int i = 0;
		for (; i < 148; i++) {
						
			params.put("readerNo", (Math.floorMod(i, 2L)!=0?"1":"2"));
			
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int age = r.nextInt(99);
					long start = System.currentTimeMillis();
					ResponseEntity<String> response = restTemplate.postForEntity( url+"&readerNo="+(Math.floorMod(age, 2L)!=0?"1":"2"), params, String.class );
					logger.info("Response (" +  (System.currentTimeMillis()-start) + "): " + response.getStatusCode());
					responses.add(response.getStatusCode());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		while (responses.size() != i) {
			Thread.sleep(500);
		}
		
		logger.info("Test finished");
	}
	

}
