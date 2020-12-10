package com.shree.clinicalworkflow.domain;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Where;

@Entity
@Where(clause = " DEACTIVATION_DATE IS NULL ")
public  class PersonDepartmentTag {

	@Id
	@GeneratedValue
	@Column(name="PERSONAL_DEPARTMENT_ID")
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSONAL_DETAILS_ID")
	private  PersonalDetails personalDetails;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENT_ID")
	private  Department department;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "MODULE_ID")
	private  Module module;
	
	private  Long startTime;
	
	private  Long endTime;
	
	private  Date activationDate;
	
	private  Date deactivationDate;
	
   
	public PersonDepartmentTag() {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public PersonalDetails getPersonalDetails() {
		return personalDetails;
	}


	public void setPersonalDetails(PersonalDetails personalDetails) {
		this.personalDetails = personalDetails;
	}


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}

	public Long getStartTime() {
		return startTime;
	}


	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}


	public Long getEndTime() {
		return endTime;
	}


	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}


	public Date getActivationDate() {
		return activationDate;
	}


	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}


	public Date getDeactivationDate() {
		return deactivationDate;
	}


	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}
	
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}

}
