package com.shree.clinicalworkflow.domain;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RfidTag {
	@Id
	@GeneratedValue
	@Column(name="RFID_TAG_ID")
	private Long id;
	private String rfidTagHexNo;
	private String code;
	private Date activationDate; 
	private Date deactivationDate;
	private Date lastUpdated;
	private RfidTagStatus status;
	

	
	public RfidTag() {

	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setRfidTagHexNo(String rfidTagHexNo) {
		this.rfidTagHexNo = rfidTagHexNo;
	}
	public String getRfidTagHexNo() {
		return rfidTagHexNo;
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

	public void setStatus(RfidTagStatus status) {
		this.status = status;
	}
	public RfidTagStatus getStatus() {
		return status;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
}
