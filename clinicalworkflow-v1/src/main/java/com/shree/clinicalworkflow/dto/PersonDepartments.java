package com.shree.clinicalworkflow.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.shree.clinicalworkflow.domain.Department;
import com.shree.clinicalworkflow.domain.Module;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidTag;

public class PersonDepartments {
	private PersonalDetails personalDetails;
	private RfidTag rfidTag;
	private String code;
	@DateTimeFormat(pattern = "MM/dd/yyyy h:mm a")
    private Date fromDate;
	@DateTimeFormat(pattern = "MM/dd/yyyy h:mm a")
    private Date toDate;
	private String reportName;
	private List<Department> departments = new ArrayList<Department>();
	private List<Module> modules = new ArrayList<Module>();
	private List<PersonLog> personLogs = new ArrayList<PersonLog>();
	
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
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportName() {
		return reportName;
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
	public void setPersonLogs(List<PersonLog> personLogs) {
		this.personLogs = personLogs;
	}
	public List<PersonLog> getPersonLogs() {
		return personLogs;
	}
}
