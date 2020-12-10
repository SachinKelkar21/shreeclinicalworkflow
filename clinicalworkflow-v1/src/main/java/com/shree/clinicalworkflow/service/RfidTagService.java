package com.shree.clinicalworkflow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shree.clinicalworkflow.domain.RfidTag;

public interface RfidTagService {
	public List<RfidTag> listAll(); 
	public void save(RfidTag role);
	public RfidTag get(Long id) ;
	public void delete(Long id) ;
	public Page<RfidTag> findPaginated(Pageable pageable) ;

}
