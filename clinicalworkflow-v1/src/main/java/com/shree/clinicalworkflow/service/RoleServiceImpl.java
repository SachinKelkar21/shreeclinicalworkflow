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

import com.shree.clinicalworkflow.domain.Role;
import com.shree.clinicalworkflow.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;
	
	private List<Role> roles; 
	
	@Override
	public List<Role> listAll() {
		List<Role> roles = new ArrayList<Role>();
		roleRepository.findAll().forEach(roles::add);
	    return roles;
	}

	@Override
	public void save(Role role) {
		roleRepository.save(role);
	}

	@Override
	public Role get(Long id) {
		// TODO Auto-generated method stub
		return roleRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		 Role role =roleRepository.getId(id);
		 roleRepository.delete(role);

	}
	
	@Override
	public Page<Role> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        roles=this.listAll();
        List<Role> list;
 
        if (roles.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, roles.size());
            list = roles.subList(startItem, toIndex);
        }
 
        Page<Role> rolePage
          = new PageImpl<Role>(list, PageRequest.of(currentPage, pageSize), roles.size());
 
        return rolePage;
    }

}
