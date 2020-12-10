package com.shree.clinicalworkflow.dto;

import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import com.shree.clinicalworkflow.domain.Department;
import com.shree.clinicalworkflow.domain.Module;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.RfidTagStatus;
import com.shree.clinicalworkflow.repository.PersonalDetailsRepository;
import com.shree.clinicalworkflow.service.PersonalDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonDepartments {
	private PersonalDetails personalDetails;
	private RfidTag rfidTag;
	private String code;
	
	@Autowired
	private PersonalDetailsRepository service;
	
	private List<Department> departments = new ArrayList<Department>();
	private List<Module> modules = new ArrayList<Module>();
	
	public PersonalDetails getPersonalDetails() {
		return personalDetails;
	}
	public void setPersonalDetails(PersonalDetails personalDetails) {
		this.personalDetails = personalDetails;
	}
	public List<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	public void setRfidTag(RfidTag rfidTag) {
		this.rfidTag = rfidTag;
	}
	public RfidTag getRfidTag() {
		return rfidTag;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	public List<Module> getModules() {
		return modules;
	}
	public static boolean isValidPersonDepartments(PersonDepartments personDepartments) {
		
	    return personDepartments != null
	      && personDepartments.getPersonalDetails()!=null 
	      && personDepartments.getPersonalDetails().getRfidTag()!=null
	      && personDepartments.getPersonalDetails().getRfidTag().getRfidTagHexNo()!=null
	      && personDepartments.getPersonalDetails().getRfidTag().getRfidTagHexNo().equals("-1")!=true	;	
	    	      
	}
	public static boolean isValidCode(PersonDepartments personDepartments,String rfidTagHexNo) {
		
	     return personDepartments != null
	   	      && personDepartments.getPersonalDetails()!=null 
		      && personDepartments.getPersonalDetails().getRfidTag()!=null
		      &&  !personDepartments.getPersonalDetails().getRfidTag().equals(rfidTagHexNo);   
	}
}
