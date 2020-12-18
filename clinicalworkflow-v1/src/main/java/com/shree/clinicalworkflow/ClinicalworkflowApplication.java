package com.shree.clinicalworkflow;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IterableMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import com.shree.clinicalworkflow.domain.Department;
import com.shree.clinicalworkflow.domain.DepartmentModuleGroup;
import com.shree.clinicalworkflow.domain.Module;
import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.domain.PersonType;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidReader;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.RfidTagStatus;
import com.shree.clinicalworkflow.domain.Role;

import com.shree.clinicalworkflow.domain.User;
import com.shree.clinicalworkflow.repository.DepartmentModuleGroupRepository;
import com.shree.clinicalworkflow.repository.DepartmentRepository;
import com.shree.clinicalworkflow.repository.ModuleRepository;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagRepository;
import com.shree.clinicalworkflow.repository.PersonTypeRepository;
import com.shree.clinicalworkflow.repository.PersonalDetailsRepository;
import com.shree.clinicalworkflow.repository.RfidReaderRepository;
import com.shree.clinicalworkflow.repository.RfidTagRepository;
import com.shree.clinicalworkflow.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRSwapFile;
@Slf4j
@SpringBootApplication
public class ClinicalworkflowApplication {
	
/*	@Value("${directory}")
	private String directory;

*/
	public static void main(String[] args) {
		SpringApplication.run(ClinicalworkflowApplication.class, args);
	}
	
/*		
	@Bean
	JRFileVirtualizer fileVirtualizer() {
		return new JRFileVirtualizer(100, directory);
	}
	
	@Bean
	JRSwapFileVirtualizer swapFileVirtualizer() {
		JRSwapFile sf = new JRSwapFile(directory, 1024, 100);
		return new JRSwapFileVirtualizer(20, sf, true);
	}
*/
/*
	@Bean
	  public CommandLineRunner delete(
			  UserRepository userRepository,
			  PersonTypeRepository personTypeRepository,
			  PersonalDetailsRepository personalDetailsRepository,
			  RfidTagRepository rfidTagRepository,
			  PersonDepartmentTagRepository pdtRepository,
			  DepartmentRepository departmentRepository,
			  ModuleRepository moduleRepository,
			  RfidReaderRepository rfidReaderRepository,
			  DepartmentModuleGroupRepository departmentModuleGroupRepository
			  ) throws java.lang.NumberFormatException {
	    return (args) -> {
	      // save a few customers
	    	try 
	    	{
	    		userRepository.deleteAll();
	    		pdtRepository.deleteAll();
	    		personTypeRepository.deleteAll();
	    		rfidTagRepository.deleteAll();
	    		departmentModuleGroupRepository.deleteAll();
	    		rfidReaderRepository.deleteAll();
	    		moduleRepository.deleteAll();
	    		departmentRepository.deleteAll();
	    		personTypeRepository.deleteAll();
	    		
	    		
	    		log.info("Config0 : deletedd!");
	    	}
	    	catch(java.lang.NumberFormatException ne) {
	    		ne.printStackTrace();
	    	}
	      };
	  }
	

 
 
 
 
 	@Bean
	  public CommandLineRunner insertUserData(UserRepository repository,ModuleRepository repository2) throws java.lang.NumberFormatException {
	    return (args) -> {
	      // save a few customers
	    	try 
	    	{
	    	
	    	  Set<Role> roles = new HashSet<Role>();
	    	  Role role = new Role();
	    	  role.setName("RECEPTION1");
	    	  roles.add(role);
	    	
	    	  User user = new User();
		      user.setName("pritee");
		      user.setPassword("$2a$10$tHZ8FmGpcWYPNZ.L/slxC.YrErsr6z0PrjX1UEMLxRpOKB1qnpZ2q");
		      user.setEnabled(true);
		      user.setRoles(roles);
		      repository.save(user);
	    	
	    	  roles = new HashSet<Role>();
	    	  role = new Role();
	    	  role.setName("RECEPTION2");
	    	  roles.add(role);
	    	  
	    	  user = new User();
		      user.setName("raju");
		      user.setPassword("$2a$10$tHZ8FmGpcWYPNZ.L/slxC.YrErsr6z0PrjX1UEMLxRpOKB1qnpZ2q");
		      user.setEnabled(true);
		      user.setRoles(roles);
		      repository.save(user);
		      
	    	  
	    	  role = new Role();
	    	  role.setName("ADMIN");
	    	  roles = new HashSet<Role>();
	    	  roles.add(role);

	    	  user = new User();
		      user.setName("anand");
		      user.setEnabled(true);
		      user.setPassword("$2a$10$tHZ8FmGpcWYPNZ.L/slxC.YrErsr6z0PrjX1UEMLxRpOKB1qnpZ2q");
		      user.setRoles(roles);
		      repository.save(user);
	    	  
	    	  role = new Role();
	    	  role.setName("SUPERADMIN");
	    	  roles = new HashSet<Role>();
	    	  roles.add(role);
	    	  
	    	  user = new User();
		      user.setName("sachin");
		      user.setPassword("$2a$10$tHZ8FmGpcWYPNZ.L/slxC.YrErsr6z0PrjX1UEMLxRpOKB1qnpZ2q");
		      user.setRoles(roles);
		      repository.save(user);
		      
		      
		      log.info("reader saved!");
	    	}
	    	catch(java.lang.NumberFormatException ne) {
	    		ne.printStackTrace();
	    	}
	      };
	  }
*/	 
    //config1
/*
	@Bean
	  public CommandLineRunner insertDataForDepartmentModuleAndRfidReader(
			  DepartmentRepository departmentRepository,
			  ModuleRepository moduleRepository
			  ) throws java.lang.NumberFormatException {
	    return (args) -> {
	      // save a few customers
	    	try 
	    	{
	    		String deptString="OPD#DIGNOSTIC#ICU#OT#IPD#ALL";//ENTER DEPARTEMENT 
		    	int totalModules=11;//ENTER DOOR NO
		    	Department department;
		    	Map<String,Department> departmentMap = new HashMap<String,Department>();
		    	List<RfidReader> rfidReaders;
		    	Module module;
		    	RfidReader reader1;
		    	RfidReader reader2;
		    	DepartmentModuleGroup departmentModuleGroup;
		    	List<DepartmentModuleGroup> departmentModuleGroups;
		    	
		    	
	        	for(int i=0;i<deptString.split("#").length;i++)
		    	{
			    	department = new Department();
				    department.setName(deptString.split("#")[i]);
				    department.setActivationDate(new Date(System.currentTimeMillis()));
				    departmentMap.put(deptString.split("#")[i],departmentRepository.save(department));
		    	}    
		    	
		    	for(int i=1;i<totalModules+1;i++) 
		    	{
		    	  rfidReaders = new ArrayList<RfidReader>();
		    	  departmentModuleGroups = new ArrayList<DepartmentModuleGroup>();
		    	  module = new Module();
		    	  module.setDoorNo(i);
		    	  module.setActivationDate(new Date(System.currentTimeMillis()));
		    	  module.setMacAddress(""+i+"."+i+"."+i+"."+i);
		    	  
		    	  reader1 = new RfidReader();
		    	  reader1.setModule(module);
		    	  reader1.setReaderNo(i<2?i:(i*2)-1);
		    	  reader1.setActivationDate(new Date(System.currentTimeMillis()));
		    	  rfidReaders.add(reader1);
		    	  
		       	  reader2 = new RfidReader();
		    	  reader2.setModule(module);
		    	  reader2.setReaderNo(i<2?i+1:i*2);
		    	  reader2.setActivationDate(new Date(System.currentTimeMillis()));
		    	  rfidReaders.add(reader2);
		    	  
		    	  module.setRfidReaders(rfidReaders);
		    	  
		    	  if(i==1)
		    	  {
		    		  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("OPD"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	  
			    	  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("DIGNOSTIC"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	  
			    	  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("ICU"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	  
			    	  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("OT"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	  
			    	  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("IPD"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
		    		  
		    	  }
		    	  else if(i==4) 
		    	  {
		    		
		    		  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("DIGNOSTIC"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	
		    	  }
		    	  else if(i==6) 
		    	  {
		    		
		    		  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("DIGNOSTIC"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	  
			    	  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("IPD"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	
		    	  }	
		    	  else if(i==8) 
		    	  {
		    		
		    		  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("OT"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	
		    	  }
		    	  else if(i==9) 
		    	  {
		    		
		    		  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("ICU"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
			    	
		    	  }
		    	  else if(i==11)
		    	  {
		    		  departmentModuleGroup = new DepartmentModuleGroup();
			    	  departmentModuleGroup.setModule(module);
			    	  departmentModuleGroup.setDepartment(departmentMap.get("IPD"));
			    	  departmentModuleGroup.setActivationDate(new Date(System.currentTimeMillis()));
			    	  departmentModuleGroups.add(departmentModuleGroup);
		    	  }
		    	  module.setDepartmentModuleGroups(departmentModuleGroups);
		    	  moduleRepository.save(module);
	    	} 
		      
		      log.info("Config1 : Department,Module,Readers saved!");
	    	}
	    	catch(java.lang.NumberFormatException ne) {
	    		ne.printStackTrace();
	    	}
	      };
	  }*/
	  //config2
/*
	  @Bean
	  public CommandLineRunner insertDataForPersonTypeAndPersonDetailsMappingForPrCvVip(
			  PersonTypeRepository personTypeRepository,
			  PersonalDetailsRepository personalDetailsRepository,
			  RfidTagRepository rfidTagRepository,
			  PersonDepartmentTagRepository pdtRepository,
			  DepartmentRepository departmentRepository,
			  ModuleRepository moduleRepository
			  ) throws java.lang.NumberFormatException {
		  
	    return (args) -> {
	
	    	try 
	    	{	  Map<String,String> map = new HashMap<String,String>();
	    		  map.put("PatiantRelative", "PR#20#DEPT");//Patiant*2
	    		  map.put("ConsultantVisting", "CV#5#DEPT");
	    		  map.put("VeryImportantPerson", "VIP#2#DEPT");
	    		  map.put("Doctor", "D#12#MODULE");
	    		  map.put("Employee", "E#25#MODULE");
	    		  map.put("ServiceProvider", "SP#10#MODULE");
	    		  map.put("ConsultantInhouse", "CI#5#MODULE");
	    		  List<PersonalDetails> personalDetailsList =null;
	    		  PersonalDetails personalDetails =null;
	    		  List<PersonDepartmentTag> personDepartmentTags =null;
	    		  for(Map.Entry<String, String> entry : map.entrySet())
	    		  {	  
	    		  personalDetailsList = new ArrayList<PersonalDetails>(); 	  
	    		  PersonType personType = new PersonType();
	    		  personType.setName(entry.getKey());
	    		  personType.setCode(entry.getValue().split("#")[0]);
	    		  personType.setActivationDate(new Date(System.currentTimeMillis()));
	    		  personType.setDept(entry.getValue().split("#")[2]);
	    		  
	    		  for(int i=0;i< Integer.parseInt(entry.getValue().split("#")[1]);i++) 
		  	    	  {
	    			    personDepartmentTags = new ArrayList<PersonDepartmentTag>();
	    			  	personalDetails = new PersonalDetails();
	    			  	personalDetails.setCode(personType.getCode().concat(""+(i+1)));
	    				personalDetails.setIntials(" ");
	    				personalDetails.setFirstName(" ");
	    				personalDetails.setMiddleName(" ");
	    				personalDetails.setLastName(" ");
	    				personalDetails.setAddress1(" ");
	    				personalDetails.setAddress2(" ");
	    				personalDetails.setAddress3(" ");
	    				personalDetails.setMobileNo(0);
	    				personalDetails.setAadharNo(0);
	    				personalDetails.setEmail(" ");
	
	    				RfidTag rfidTag = new RfidTag();
	    				rfidTag.setRfidTagHexNo(0L);
	    				rfidTag.setCode(personType.getCode().concat(""+(i+1)));
	    				rfidTag.setActivationDate(new Date(System.currentTimeMillis()));
	    				rfidTag.setStatus(RfidTagStatus.DEPOSITE);
	    				personalDetails.setRfidTag(rfidTag);
	    				rfidTagRepository.save(rfidTag);
				
	    				
	    				if("DEPT".equals(personType.getDept())==true)
	    				{
		    				Iterable<Department> departmentList = departmentRepository.findAll();
		    				for (Department dept : departmentList) {
								
								if(!(dept!=null && dept.getName().equals("ALL")))
								{
			    					PersonDepartmentTag personDepartmentTag = new PersonDepartmentTag();
				    			    personDepartmentTag.setPersonalDetails(personalDetails);
				    			    personDepartmentTag.setActivationDate(new Date(System.currentTimeMillis()));
				    			    personDepartmentTag.setDepartment(dept);
				    			    personDepartmentTag.setStartTime(System.currentTimeMillis());
				    			    personDepartmentTag.setEndTime(System.currentTimeMillis()*1000);
				    			    personDepartmentTags.add(personDepartmentTag);
								}    
		    				 }		
	    				}
	    				else if("MODULE".equals(personType.getDept())==true)
	    				{
	    					log.info("MODULE START!");
	    					Department dept=departmentRepository.getDepartmentByName("ALL");
	    					if(dept!=null)
	    					{
		    					Iterable<Module> moduleList = moduleRepository.findAll();
		    					for (Module module : moduleList) {
									
									if(module.getDeactivationDate()==null)
									{
				    					PersonDepartmentTag personDepartmentTag = new PersonDepartmentTag();
					    			    personDepartmentTag.setPersonalDetails(personalDetails);
					    			    personDepartmentTag.setActivationDate(new Date(System.currentTimeMillis()));
					    			    personDepartmentTag.setDepartment(dept);
					    			    personDepartmentTag.setModule(module);
					    			    personDepartmentTag.setStartTime(System.currentTimeMillis());
					    			    personDepartmentTag.setEndTime(System.currentTimeMillis()*1000);
					    			    personDepartmentTags.add(personDepartmentTag);
									}    
			    				 }
	    					}
	    				}
	    				personalDetails.setPersonDepartmentTags(personDepartmentTags);
	    				personalDetails.setPersonType(personType);
	    				
	    				personalDetailsList.add(personalDetails);
	    				
	   	    	  	  }
	    		    personType.setPersonalDetailsList(personalDetailsList);
	    		  	personTypeRepository.save(personType);

	    		   
		    	  }
	    		  log.info("Config2 : PersonType PersonDetails Tags saved!");
	    	}
	    	catch(java.lang.NumberFormatException ne) {
	    		ne.printStackTrace();
	    	}
	      };
	  }
	*/
	
}
