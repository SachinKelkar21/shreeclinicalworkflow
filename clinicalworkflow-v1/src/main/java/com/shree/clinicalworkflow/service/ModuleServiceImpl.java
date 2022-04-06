package com.shree.clinicalworkflow.service;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shree.clinicalworkflow.domain.Module;
import com.shree.clinicalworkflow.repository.ModuleRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ModuleServiceImpl implements ModuleService {
	@Autowired
	private ModuleRepository moduleRepository;
	private List<Module> modules; 
	
	public ModuleServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	public ModuleServiceImpl(ModuleRepository moduleRepository) {
		this.moduleRepository=moduleRepository;
		
	}
	
	@Override
	public List<Module> listAll() {
	log.info("listAll");	
		List<Module> modules = new ArrayList<Module>();
		moduleRepository.findAll().forEach(modules::add);
	    return modules;
	}

	@Override
	public void save(Module module) {
		log.info("save");
		moduleRepository.save(module);

	}

	@Override
	public Module get(Long id) {
		log.info("get(id)");
		// TODO Auto-generated method stub
		return moduleRepository.findById(id).get();
	}

	@Override
	public void delete(Long id) {
		log.info("delete");
		moduleRepository.deleteById(id);

	}
	
	@Override
	public Page<Module> findPaginated(Pageable pageable) {
		log.info("findPaginated");
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        modules=this.listAll();
        List<Module> list;
 
        if (modules.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, modules.size());
            list = modules.subList(startItem, toIndex);
        }
 
        Page<Module> modulePage
          = new PageImpl<Module>(list, PageRequest.of(currentPage, pageSize), modules.size());
 
        return modulePage;
    }


}
