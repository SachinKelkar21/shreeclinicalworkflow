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

import com.shree.clinicalworkflow.domain.Department;
import com.shree.clinicalworkflow.repository.DepartmentRepository;
@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentRepository departmentRepository;
	private List<Department> departments; 
	
	public DepartmentServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository=departmentRepository;
		
	}
	
	@Override
	public List<Department> listAll() {
		// TODO Auto-generated method stub
		List<Department> departments = new ArrayList<Department>();
		departmentRepository.findAll().forEach(departments::add);
	    return departments;
	}

	@Override
	public void save(Department department) {
		departmentRepository.save(department);

	}

	@Override
	public Department get(Long id) {
		// TODO Auto-generated method stub
		return departmentRepository.findById(id).get();
	}

	@Override
	public void delete(Long id) {
		departmentRepository.deleteById(id);

	}
	
	@Override
	public Page<Department> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        departments=this.listAll();
        List<Department> list;
 
        if (departments.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, departments.size());
            list = departments.subList(startItem, toIndex);
        }
 
        Page<Department> departmentPage
          = new PageImpl<Department>(list, PageRequest.of(currentPage, pageSize), departments.size());
 
        return departmentPage;
    }

}
