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

import org.hibernate.annotations.Where;



@Entity
public class Department {
	@Id
	@GeneratedValue
	@Column(name="DEPARTMENT_ID")
	private Long id;

	private String name;
	
    private Date activationDate;
	
	private Date deactivationDate;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "department", fetch = FetchType.LAZY)
	@Where(clause = " deactivation_date is null ")
	private List<DepartmentModuleGroup> departmentModuleGroups = new ArrayList<DepartmentModuleGroup>();
	
	
	public Department() {
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
	
	public void setDepartmentModuleGroups(List<DepartmentModuleGroup> departmentModuleGroups) {
		this.departmentModuleGroups = departmentModuleGroups;
	}
	
	public List<DepartmentModuleGroup> getDepartmentModuleGroups() {
		return departmentModuleGroups;
	}
	
}
