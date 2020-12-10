package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.Department;

public interface DepartmentService {

	public List<Department> listAll(); 
	public void save(Department department);
	public Department get(Long id) ;
	public void delete(Long id) ;
	public Page<Department> findPaginated(Pageable pageable) ;

}
