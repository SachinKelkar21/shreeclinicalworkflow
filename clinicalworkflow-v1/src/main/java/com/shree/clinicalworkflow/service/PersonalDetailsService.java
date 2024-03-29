package com.shree.clinicalworkflow.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidTagStatus;
import com.shree.clinicalworkflow.dto.PersonDTO;

public interface PersonalDetailsService {
	
	PersonalDetails createPersonalDetails(String code,
			 String intials,
			 String firstName, String middleName,	 String lastName,
			 String address1, String address2, String address3,	
			 Integer mobileNo, Integer aadharNo, String email);
	boolean getAccess(final String rfidTagId,final Integer doorNo,final Integer readerNo);
	PersonDTO getAccess2(final String rfidTagId,final Integer doorNo,final Integer readerNo);
	
	
	public void save(PersonalDetails role);
	public PersonalDetails get(Long id) ;
	public void delete(Long id) ;
	public Page<PersonalDetails> findPaginated(Pageable pageable,RfidTagStatus status,String dept,String keyword) ;
	public Page<PersonalDetails> findPaginated(Pageable pageable,RfidTagStatus status,String dept,String keyword,String searchBy);
	public List<PersonalDetails> listAll(RfidTagStatus status,String dept,String keyword);
	public List<PersonalDetails> listAll(RfidTagStatus status,String dept,String keyword,String searchBy);
	public String getCode(String tagId) ;
	public Boolean backupDB(String dir) ;
}
