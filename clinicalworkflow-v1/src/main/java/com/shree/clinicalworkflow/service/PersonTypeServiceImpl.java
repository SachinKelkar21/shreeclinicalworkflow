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
import com.shree.clinicalworkflow.domain.PersonType;
import com.shree.clinicalworkflow.repository.PersonTypeRepository;

@Service
public class PersonTypeServiceImpl implements PersonTypeService {

	@Autowired
	private PersonTypeRepository personTypeRepository;
	
	private List<PersonType> personTypes; 
	
	@Override
	public List<PersonType> listAll() {
		List<PersonType> personTypes = new ArrayList<PersonType>();
		personTypeRepository.findAll().forEach(personTypes::add);
	    return personTypes;
	}

	@Override
	public void save(PersonType personType) {
		personTypeRepository.save(personType);
	}

	@Override
	public PersonType get(Long id) {
		// TODO Auto-generated method stub
		return personTypeRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		 PersonType personType =personTypeRepository.getId(id);
		 personTypeRepository.delete(personType);

	}
	
	@Override
	public Page<PersonType> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        personTypes=this.listAll();
        List<PersonType> list;
 
        if (personTypes.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, personTypes.size());
            list = personTypes.subList(startItem, toIndex);
        }
 
        Page<PersonType> personTypePage
          = new PageImpl<PersonType>(list, PageRequest.of(currentPage, pageSize), personTypes.size());
 
        return personTypePage;
    }

}
