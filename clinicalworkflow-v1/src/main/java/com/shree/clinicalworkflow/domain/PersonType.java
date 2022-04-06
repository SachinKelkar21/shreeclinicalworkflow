package com.shree.clinicalworkflow.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PersonType {
	@Id
	@GeneratedValue
	@Column(name="PERSON_TYPE_ID")
	private Long id;
	
	private String name;
	
	private String code;

	private String dept;
	
	private Date activationDate;
	
	private Date deactivationDate;
	
	private Boolean previousEntryCheck;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "personType", fetch = FetchType.LAZY )
	
	private List<PersonalDetails> personalDetailsList = new ArrayList<PersonalDetails>();
	

	public PersonType() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDept() {
		return dept;
	}
	
	public void setPersonalDetailsList(List<PersonalDetails> personalDetailsList) {
		this.personalDetailsList = personalDetailsList;
	}
	public List<PersonalDetails> getPersonalDetailsList() {
		return personalDetailsList;
	}

	public Boolean getPreviousEntryCheck() {
		return previousEntryCheck;
	}

	public void setPreviousEntryCheck(Boolean previousEntryCheck) {
		this.previousEntryCheck = previousEntryCheck;
	}
	
	
}
