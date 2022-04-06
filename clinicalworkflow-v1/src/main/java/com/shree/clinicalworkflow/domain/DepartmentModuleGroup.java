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
@Entity
public class DepartmentModuleGroup {
	@Id
	@GeneratedValue
	@Column(name="DEPARTMENT_MODULE_GROUP_ID")
	private Long id;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODULE_ID")
	private Module module;
	
	private Date activationDate;
	
	private Date deactivationDate;

	public DepartmentModuleGroup() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
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
	
}
