package com.shree.clinicalworkflow.dto;

import java.util.List;
import com.shree.clinicalworkflow.domain.Department;
import com.shree.clinicalworkflow.domain.Module;

public class DepartmentModuleDTO {
	
	private Department department;
	private List<Module> modules;
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public List<Module> getModules() {
		return modules;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	

}
