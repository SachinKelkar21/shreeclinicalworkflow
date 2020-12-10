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

import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagRepository;

@Service
public class PersonDepartmentTagServiceImpl implements PersonDepartmentTagService {

	@Autowired
	private PersonDepartmentTagRepository personDepartmentTagRepository;
	
	private List<PersonDepartmentTag> personDepartmentTags; 
	
	@Override
	public List<PersonDepartmentTag> listAll() {
		List<PersonDepartmentTag> personDepartmentTags = new ArrayList<PersonDepartmentTag>();
		personDepartmentTagRepository.findAll().forEach(personDepartmentTags::add);
	    return personDepartmentTags;
	}

	@Override
	public void save(PersonDepartmentTag personDepartmentTag) {
		personDepartmentTagRepository.save(personDepartmentTag);
	}

	@Override
	public PersonDepartmentTag get(Long id) {
		// TODO Auto-generated method stub
		return personDepartmentTagRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		 PersonDepartmentTag personDepartmentTag =personDepartmentTagRepository.getId(id);
		 personDepartmentTagRepository.delete(personDepartmentTag);

	}
	
	@Override
	public Page<PersonDepartmentTag> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        personDepartmentTags=this.listAll();
        List<PersonDepartmentTag> list;
 
        if (personDepartmentTags.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, personDepartmentTags.size());
            list = personDepartmentTags.subList(startItem, toIndex);
        }
 
        Page<PersonDepartmentTag> personDepartmentTagPage
          = new PageImpl<PersonDepartmentTag>(list, PageRequest.of(currentPage, pageSize), personDepartmentTags.size());
 
        return personDepartmentTagPage;
    }


}
