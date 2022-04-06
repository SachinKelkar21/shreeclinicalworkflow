package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.PersonDepartmentTag;

public interface PersonDepartmentTagService {
	public List<PersonDepartmentTag> listAll(); 
	public void save(PersonDepartmentTag personDepartmentTag);
	public PersonDepartmentTag get(Long id) ;
	public void delete(Long id) ;
	public Page<PersonDepartmentTag> findPaginated(Pageable pageable) ;
	 

}
