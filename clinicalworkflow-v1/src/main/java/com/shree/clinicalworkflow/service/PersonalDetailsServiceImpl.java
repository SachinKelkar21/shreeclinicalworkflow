package com.shree.clinicalworkflow.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shree.clinicalworkflow.domain.DepartmentModuleGroup;
import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.domain.PersonDepartmentTagLog;
import com.shree.clinicalworkflow.domain.PersonType;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidReader;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.RfidTagStatus;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagLogRepository;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagRepository;
import com.shree.clinicalworkflow.repository.PersonTypeRepository;
import com.shree.clinicalworkflow.repository.PersonalDetailsRepository;
import com.shree.clinicalworkflow.repository.RfidReaderRepository;
import com.shree.clinicalworkflow.repository.RfidTagRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {
	private PersonDepartmentTagRepository personDepartmentTagRepository;
    private PersonDepartmentTagLogRepository personDepartmentTagLogRepository;
    @Autowired
    private RfidTagRepository rfidTagRepository;
    private RfidReaderRepository rfidReaderRepository;
    
    @Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	private List<PersonalDetails> personalDetailss ; 
	
    
	public PersonalDetailsServiceImpl() {
		
	}
	
	@Autowired
	public PersonalDetailsServiceImpl(PersonDepartmentTagRepository personDepartmentTagRepository,
			PersonDepartmentTagLogRepository personDepartmentTagLogRepository,
			RfidTagRepository rfidTagRepository,
			RfidReaderRepository rfidReaderRepository) {
		this.personDepartmentTagRepository=personDepartmentTagRepository;
		this.personDepartmentTagLogRepository=personDepartmentTagLogRepository;
		this.rfidTagRepository=rfidTagRepository;
		this.rfidReaderRepository=rfidReaderRepository;
	}

	@Override
	public PersonalDetails createPersonalDetails(
			 String code,
			 String intials,
			 String firstName, String middleName,	 String lastName,
			 String address1, String address2, String address3,	
			 Integer mobileNo, Integer aadharNo, String email) {
		PersonalDetails p = new PersonalDetails();
		p.setCode(code);
		p.setIntials(intials);
		p.setFirstName(firstName);
		p.setMiddleName(middleName);
		p.setLastName(lastName);
		p.setAddress1(address1);
		p.setAddress2(address2);
		p.setAddress3(address3);
		p.setMobileNo(mobileNo);
		p.setAadharNo(aadharNo);
		p.setEmail(email);
		return p;
	}
	
	@Override
	public boolean getAccess(String rfidTagId, Integer doorNo, Integer readerNo) {
		boolean access=false;
		PersonalDetails authorisedRfid =personalDetailsRepository.getPersonalDetailsByRfidTagHexNoAndStatus(rfidTagId, RfidTagStatus.ISSUE);
		if(authorisedRfid!=null)
		{
			List<PersonDepartmentTag> personDepartmentTags = authorisedRfid.getPersonDepartmentTags();
			if(authorisedRfid.getPersonType().getDept().equals(new String("MODULE")))
			{
			
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								access=true;
								PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"GRANTED",personDepartmentTag.getId());
								personDepartmentTagLogRepository.save(pl);
								break;
							}
						}
					}
					
				}
				
			}
			else if(authorisedRfid.getPersonType().getDept().equals(new String("DEPT")))
			{
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getDepartment()!=null 
							&& personDepartmentTag.getDepartment().getDeactivationDate()==null)
					{
						List<DepartmentModuleGroup> departmentModuleGroups 
							= personDepartmentTag.getDepartment().getDepartmentModuleGroups();
						
						for (DepartmentModuleGroup departmentModuleGroup : departmentModuleGroups) {
							if(departmentModuleGroup.getDeactivationDate()==null 
									&& departmentModuleGroup.getModule()!=null
									&& departmentModuleGroup.getModule().getDeactivationDate()==null
									&& departmentModuleGroup.getModule().getDoorNo()==doorNo.intValue())
							{
								List<RfidReader> rfidReaders =departmentModuleGroup.getModule().getRfidReaders();
								
								for (RfidReader rfidReader : rfidReaders) {
									if(rfidReader.getDeactivationDate()==null 
											&& rfidReader.getReaderNo()==readerNo.intValue())
									{
										access=true;
										PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"GRANTED",personDepartmentTag.getId());
										personDepartmentTagLogRepository.save(pl);
										break;
									}
								}
							}
							
						}
				
					}
					
				}
				
			}
			
			if(access==false)
			{	
				log.info(""+doorNo);
				log.info("****");
				log.info(""+readerNo);
				RfidReader rfidReader = rfidReaderRepository.getRfidReaderByDoorNoReaderNo(doorNo, readerNo);
				log.info("1:"+rfidReader);
				log.info("1:"+rfidReader);
				log.info("2:"+authorisedRfid.getId());
				PersonDepartmentTagLog pl2 = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"DENIED",null);
				personDepartmentTagLogRepository.save(pl2);
				authorisedRfid.setPermission("DENIED");
				authorisedRfid.setLog(null);
				authorisedRfid.setLogTime(System.currentTimeMillis());
				authorisedRfid.setCurrentPosition("D"+rfidReader.getModule().getDoorNo());
				personalDetailsRepository.save(authorisedRfid);
			}
			else
			{
				authorisedRfid.setPermission("GRANTED");
				authorisedRfid.setLog((Math.floorMod(readerNo, 2L)!=0?"IN":"OUT"));
				authorisedRfid.setLogTime(System.currentTimeMillis());
				authorisedRfid.setCurrentPosition("D"+doorNo);
			    personalDetailsRepository.save(authorisedRfid);
			}
		}
		else
		{
			RfidReader rfidReader = rfidReaderRepository.getRfidReaderByDoorNoReaderNo(doorNo, readerNo);
			PersonDepartmentTagLog pl2 = new PersonDepartmentTagLog(-1L ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"INVAID",null);
			personDepartmentTagLogRepository.save(pl2);
			
		}
		return access;
	}	

	@Override
	public List<PersonalDetails> listAll(RfidTagStatus status,String dept,String keywords) {
		List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
		if(keywords!=null)
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDeptKeyword(status,dept,keywords).forEach(personalDetailss::add);
		else
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(status,dept).forEach(personalDetailss::add);
			
	    return personalDetailss;
	    
	    
	}
	
	
	@Override
	public void save(PersonalDetails personalDetails) {
		personalDetailsRepository.save(personalDetails);
	}

	@Override
	public PersonalDetails get(Long id) {
		// TODO Auto-generated method stub
		return personalDetailsRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		PersonalDetails personalDetails =personalDetailsRepository.getId(id);
		personalDetails.getRfidTag().setStatus(RfidTagStatus.DEPOSITE);
		personalDetails.getRfidTag().setLastUpdated(new Date(System.currentTimeMillis()));
	    List<PersonDepartmentTag> personDepartmentTags = new ArrayList<PersonDepartmentTag>();
		 for (PersonDepartmentTag personDepartmentTag : personalDetails.getPersonDepartmentTags()) {
			 personDepartmentTag.setDeactivationDate(new Date(System.currentTimeMillis()));
			 personDepartmentTags.add(personDepartmentTag);
		 }
		 personalDetails.setPersonDepartmentTags(personDepartmentTags);
		 personalDetails.setLog(null);
		 personalDetails.setLogTime(null);
		 personalDetails.setPermission(null);
		 personalDetails.setCurrentPosition(null);
		 personalDetailsRepository.save(personalDetails);

	}
	
	@Override
	public Page<PersonalDetails> findPaginated(Pageable pageable,RfidTagStatus status,String dept,String keyword) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        personalDetailss=this.listAll(status,dept,keyword);
        		
        List<PersonalDetails> list;
 
        if (personalDetailss.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, personalDetailss.size());
            list = personalDetailss.subList(startItem, toIndex);
        }
 
        Page<PersonalDetails> personalDetailsPage
          = new PageImpl<PersonalDetails>(list, PageRequest.of(currentPage, pageSize), personalDetailss.size());
 
        return personalDetailsPage;
    }
	@Override
	public String getCode(String tagId) {
		// TODO Auto-generated method stub
		String code =null;
		try {
			code = personalDetailsRepository.getPersonalDetailsByRfidTagHexNoAndStatus(tagId,RfidTagStatus.DEPOSITE).getCode();
		}
		catch(NullPointerException e)
		{
			return " ";
		}
		return code;
	}
	
}
