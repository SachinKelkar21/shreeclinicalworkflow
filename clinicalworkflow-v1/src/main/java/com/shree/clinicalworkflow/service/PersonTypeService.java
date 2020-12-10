package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.PersonType;

public interface PersonTypeService {
	public List<PersonType> listAll(); 
	public void save(PersonType role);
	public PersonType get(Long id) ;
	public void delete(Long id) ;
	public Page<PersonType> findPaginated(Pageable pageable) ;

}
