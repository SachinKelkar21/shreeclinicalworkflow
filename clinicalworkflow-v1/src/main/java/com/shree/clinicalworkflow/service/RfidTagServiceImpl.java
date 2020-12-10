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

import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.repository.RfidTagRepository;
@Service
public class RfidTagServiceImpl implements RfidTagService {

	@Autowired
	private RfidTagRepository rfidTagRepository;
	
	private List<RfidTag> rfidTags; 
	
	@Override
	public List<RfidTag> listAll() {
		List<RfidTag> rfidTags = new ArrayList<RfidTag>();
		rfidTagRepository.findAll().forEach(rfidTags::add);
	    return rfidTags;
	}

	@Override
	public void save(RfidTag rfidTag) {
		rfidTagRepository.save(rfidTag);
	}

	@Override
	public RfidTag get(Long id) {
		// TODO Auto-generated method stub
		return rfidTagRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		 RfidTag rfidTag =rfidTagRepository.getId(id);
		 rfidTagRepository.delete(rfidTag);

	}
	
	@Override
	public Page<RfidTag> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        rfidTags=this.listAll();
        List<RfidTag> list;
 
        if (rfidTags.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, rfidTags.size());
            list = rfidTags.subList(startItem, toIndex);
        }
 
        Page<RfidTag> rfidTagPage
          = new PageImpl<RfidTag>(list, PageRequest.of(currentPage, pageSize), rfidTags.size());
 
        return rfidTagPage;
    }
}
