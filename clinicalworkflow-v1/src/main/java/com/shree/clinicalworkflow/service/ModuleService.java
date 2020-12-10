package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.Module;

public interface ModuleService {
	public List<Module> listAll(); 
	public void save(Module module);
	public Module get(Long id) ;
	public void delete(Long id) ;
	public Page<Module> findPaginated(Pageable pageable) ;
}
