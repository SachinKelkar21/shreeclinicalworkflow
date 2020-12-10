package com.shree.clinicalworkflow.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Where;

import com.shree.clinicalworkflow.dto.PersonDepartments;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.ToString;


@Entity
public class PersonalDetails {
	@Id
	@GeneratedValue
	@Column(name="PERSONAL_DETAILS_ID")
	private Long id;
	private String code;
	private String intials;
	private String firstName;
	private String middleName;
	private String lastName;
	private String address1;
	private String address2;
	private String address3;
	private Integer mobileNo;
	private Integer aadharNo;
	private String email;
	private String currentPosition;
    private String log;
	private Long logTime;
	private String permission;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_TYPE_ID")
	private  PersonType personType;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private RfidTag rfidTag;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "personalDetails", fetch = FetchType.EAGER )
	@Where(clause = " deactivation_date is null ") 
	private List<PersonDepartmentTag> personDepartmentTags = new ArrayList<PersonDepartmentTag>();
	
	
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Long getLogTime() {
		return logTime;
	}

	public void setLogTime(Long logTime) {
		this.logTime = logTime;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public  PersonalDetails() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIntials() {
		return intials;
	}

	public void setIntials(String intials) {
		this.intials = intials;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public Integer getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Integer mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Integer getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(Integer aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setPersonDepartmentTags(List<PersonDepartmentTag> personDepartmentTags) {
		//System.out.println("Inside set !"+personDepartmentTags.size());
		this.personDepartmentTags=personDepartmentTags;
	}
	public List<PersonDepartmentTag> getPersonDepartmentTags() {
		//System.out.println("Inside get !"+personDepartmentTags.size());
		if(personDepartmentTags!=null && personDepartmentTags.size()>0)
		{
			List<PersonDepartmentTag> temp= new ArrayList<PersonDepartmentTag>();
			for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
				if(personDepartmentTag!=null && personDepartmentTag.getDeactivationDate()==null)
				{
					temp.add(personDepartmentTag);
				}
			}
			personDepartmentTags=temp;
		}
		return personDepartmentTags;
	}
	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}
	public String getCurrentPosition() {
		return currentPosition;
	}
	
	/*public void addPersonDepartmentTag(PersonDepartmentTag personDepartmentTag) {
		personDepartmentTags.add(personDepartmentTag);
		
    }
 
    public void removePersonalDetails(PersonDepartmentTag personDepartmentTag) {
        this.getPersonDepartmentTags().remove(personDepartmentTag);
        
    }
 
    public void remove() {
        for(PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
            removePersonalDetails(personDepartmentTag);
        }
    }*/
	public void setRfidTag(RfidTag rfidTag) {
		this.rfidTag = rfidTag;
	}
	public RfidTag getRfidTag() {
		return rfidTag;
	}
	public void setPersonType(PersonType personType) {
		this.personType = personType;
	}
	public PersonType getPersonType() {
		return personType;
	}
	public static boolean isValidPersonalDetails(PersonalDetails personalDetails) {
		
	    return personalDetails != null
	      && personalDetails.code!=null 
	      && personalDetails.getPersonType()!=null
	      && personalDetails.getPersonType().getId()!=null
	      && personalDetails.getRfidTag()!=null
	      && personalDetails.getRfidTag().getRfidTagHexNo()!=null;	
		      
	}
}
