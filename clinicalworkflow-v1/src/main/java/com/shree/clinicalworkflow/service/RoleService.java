package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.Role;


public interface RoleService {
	
	public List<Role> listAll(); 
	public void save(Role role);
	public Role get(Long id) ;
	public void delete(Long id) ;
	public Page<Role> findPaginated(Pageable pageable) ;

}
