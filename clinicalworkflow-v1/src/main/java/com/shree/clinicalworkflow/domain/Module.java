package com.shree.clinicalworkflow.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

@Entity
public class Module {
	@Id
	@GeneratedValue
	@Column(name="MODULE_ID")
	private Long id;
	private String macAddress;
	private int doorNo;
	private Date activationDate;
	private Date deactivationDate;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "module", fetch = FetchType.LAZY)
	@Where(clause = " deactivation_date is null ")
	private List<RfidReader> rfidReaders = new ArrayList<RfidReader>();
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "module", fetch = FetchType.LAZY)
	@Where(clause = " deactivation_date is null ")
	private List<DepartmentModuleGroup> departmentModuleGroups = new ArrayList<DepartmentModuleGroup>();
	
	
    public Module() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(int doorNo) {
		this.doorNo = doorNo;
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
    public void setRfidReaders(List<RfidReader> rfidReaders) {
		this.rfidReaders = rfidReaders;
	}
    public List<RfidReader> getRfidReaders() {
		return rfidReaders;
	}
    public void setDepartmentModuleGroups(List<DepartmentModuleGroup> departmentModuleGroups) {
		this.departmentModuleGroups = departmentModuleGroups;
	}
    public List<DepartmentModuleGroup> getDepartmentModuleGroups() {
		return departmentModuleGroups;
	}
}
