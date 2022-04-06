package com.shree.clinicalworkflow.service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shree.clinicalworkflow.DateTimeUtils;
import com.shree.clinicalworkflow.domain.DepartmentModuleGroup;
import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.domain.PersonDepartmentTagLog;


import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidReader;
import com.shree.clinicalworkflow.domain.RfidTagStatus;
import com.shree.clinicalworkflow.dto.PersonDTO;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagLogRepository;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagRepository;

import com.shree.clinicalworkflow.repository.PersonalDetailsRepository;
import com.shree.clinicalworkflow.repository.RfidReaderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {
	private PersonDepartmentTagRepository personDepartmentTagRepository;
    private PersonDepartmentTagLogRepository personDepartmentTagLogRepository;
    private RfidReaderRepository rfidReaderRepository;
    @Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	private List<PersonalDetails> personalDetailss ; 
	
    
	public PersonalDetailsServiceImpl() {
		
	}
	
	@Autowired
	public PersonalDetailsServiceImpl(PersonDepartmentTagRepository personDepartmentTagRepository,
			PersonDepartmentTagLogRepository personDepartmentTagLogRepository,
			RfidReaderRepository rfidReaderRepository) {
		this.personDepartmentTagRepository=personDepartmentTagRepository;
		this.personDepartmentTagLogRepository=personDepartmentTagLogRepository;
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
		log.info("Inside access");
		boolean access=false;
		PersonalDetails authorisedRfid =personalDetailsRepository.getPersonalDetailsByRfidTagHexNoAndStatus(rfidTagId, RfidTagStatus.ISSUE);
		String currentLog="";
		PersonDepartmentTag prevPersonDepartmentTag =null; 
		
		if(authorisedRfid!=null)
		{
			List<PersonDepartmentTag> personDepartmentTags = authorisedRfid.getPersonDepartmentTags();
			if(authorisedRfid.getPersonType().getDept().equals(new String("ADMIN")) 
				&& authorisedRfid.getLoginCheck().equals(Boolean.FALSE))
			{
			
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
						
						Integer prevReaderNo =null;
						if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true){
							prevReaderNo =personDepartmentTag.getPreviousReaderNo();
						}	
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");

								if(prevReaderNo==null || (prevReaderNo!=null && prevReaderNo.intValue()!=rfidReader.getReaderNo()))
								{
									access=true;
									PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
									personDepartmentTagLogRepository.save(pl);
									personDepartmentTag.setPreviousReaderNo(rfidReader.getReaderNo());
									prevPersonDepartmentTag=personDepartmentTag;
									break;
								}
								
							}
						}
					}
					
				}
				
			}
			else if(authorisedRfid.getPersonType().getDept().equals(new String("ADMIN"))
					&& (authorisedRfid.getLoginCheck().equals(Boolean.TRUE) 
							&& authorisedRfid.getAccess().equals("AUTHORIZED")))
			{
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
						
						Integer prevReaderNo =null;
						if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true){
							prevReaderNo =personDepartmentTag.getPreviousReaderNo();
						}	
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");

								if(prevReaderNo==null 
										|| (prevReaderNo!=null 
											&& prevReaderNo.intValue()!=rfidReader.getReaderNo()))
								{
									access=true;
									PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
									personDepartmentTagLogRepository.save(pl);
									personDepartmentTag.setPreviousReaderNo(rfidReader.getReaderNo());
									prevPersonDepartmentTag=personDepartmentTag;
									if(personDepartmentTag.getModule().getDoorNo()==1 
											&&rfidReader.getReaderNo()==2)
									{
										authorisedRfid.setAccess("DENIED");
									}
									break;
								}
								
							}
						}
					}
					
				}
				
			}
			else if(authorisedRfid.getPersonType().getDept().equals(new String("ADMIN"))
					&& (authorisedRfid.getLoginCheck().equals(Boolean.TRUE) 
							&& authorisedRfid.getAccess().equals("DENIED")
							&& doorNo.intValue()==1 && readerNo.intValue()==1))
			{
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
						
						Integer prevReaderNo =null;
						if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true){
							prevReaderNo =personDepartmentTag.getPreviousReaderNo();
						}	
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");

								if(prevReaderNo==null 
										|| (prevReaderNo!=null 
											&& prevReaderNo.intValue()!=rfidReader.getReaderNo()))
								{
									access=true;
									PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
									personDepartmentTagLogRepository.save(pl);
									personDepartmentTag.setPreviousReaderNo(rfidReader.getReaderNo());
									prevPersonDepartmentTag=personDepartmentTag;
									if(personDepartmentTag.getModule().getDoorNo()==1 
											&&rfidReader.getReaderNo()==1)
									{
										authorisedRfid.setAccess("AUTHORIZED");
									}
									break;
								}
								
							}
						}
					}
					
				}
				
			}
			else if(!authorisedRfid.getPersonType().getDept().equals(new String("ADMIN")))
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
										currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");
										access=true;
										PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
										personDepartmentTagLogRepository.save(pl);
										break;
									}
									if(access==true)
									{
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
				RfidReader rfidReader = rfidReaderRepository.getRfidReaderByDoorNoReaderNo(doorNo, readerNo);
				PersonDepartmentTagLog pl2 = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"DENIED",null);
				personDepartmentTagLogRepository.save(pl2);
				authorisedRfid.setPermission("DENIED");
				authorisedRfid.setLog(currentLog);
				authorisedRfid.setLogTime(System.currentTimeMillis());
				authorisedRfid.setCurrentPosition("D"+rfidReader.getModule().getDoorNo());
				personalDetailsRepository.save(authorisedRfid);
				log.info("DENIED:"+authorisedRfid.getCode()+ ":"+authorisedRfid.getCurrentPosition()+":"+currentLog);
			}
			else
			{
				authorisedRfid.setPermission("GRANTED");
				authorisedRfid.setLog((Math.floorMod(readerNo, 2L)!=0?"IN":"OUT"));
				authorisedRfid.setLogTime(System.currentTimeMillis());
				authorisedRfid.setCurrentPosition("D"+doorNo);
			    personalDetailsRepository.save(authorisedRfid);
			    if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true 
			    		&& authorisedRfid.getPersonType().getDept().equals(new String("ADMIN")))
			    	personDepartmentTagRepository.save(prevPersonDepartmentTag);
			    log.info("GRANTED:"+authorisedRfid.getCode()+ ":"+authorisedRfid.getCurrentPosition()+":"+currentLog);
			}
		}
		else
		{
			RfidReader rfidReader = rfidReaderRepository.getRfidReaderByDoorNoReaderNo(doorNo, readerNo);
			PersonDepartmentTagLog pl2 = new PersonDepartmentTagLog(-1L ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"INVALID",null);
			personDepartmentTagLogRepository.save(pl2);
			
		}
		return access;
	}
	
	@Override
	public PersonDTO getAccess2(String rfidTagId, Integer doorNo, Integer readerNo) {
		long start = System.currentTimeMillis();
		log.info("getAccess2");
		PersonDTO pd  = new PersonDTO();
		pd.setAccess("false");
		boolean access=false;
		PersonalDetails authorisedRfid = personalDetailsRepository.getPersonalDetailsByRfidTagHexNoAndStatus(rfidTagId, RfidTagStatus.ISSUE);
		String currentLog="";
		if(authorisedRfid!=null)
		{
			List<PersonDepartmentTag> personDepartmentTags = authorisedRfid.getPersonDepartmentTags();
			if(authorisedRfid.getPersonType().getDept().equals(new String("ADMIN")) 
				&& authorisedRfid.getLoginCheck().equals(Boolean.FALSE))
			{
		
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
						
						Integer prevReaderNo =null;
						if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true){
							prevReaderNo =personDepartmentTag.getPreviousReaderNo();
						}	
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");

								if(prevReaderNo==null || (prevReaderNo!=null && prevReaderNo.intValue()!=rfidReader.getReaderNo()))
								{
									access=true;
									PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
									personDepartmentTagLogRepository.save(pl);
									if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true)
										personDepartmentTag.setPreviousReaderNo(rfidReader.getReaderNo());
									pl=null;
									break;
								}
								
							}
						}
					}
					if(access==true)
					{
						break;
					}
					
				}
				
			}
			else if(authorisedRfid.getPersonType().getDept().equals(new String("ADMIN"))
					&& (authorisedRfid.getLoginCheck().equals(Boolean.TRUE) 
							&& authorisedRfid.getAccess().equals("AUTHORIZED")))
			{
				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
						
						Integer prevReaderNo =null;
						if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true){
							prevReaderNo =personDepartmentTag.getPreviousReaderNo();
						}	
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");

								if(prevReaderNo==null 
										|| (prevReaderNo!=null 
											&& prevReaderNo.intValue()!=rfidReader.getReaderNo()))
								{
									access=true;
									PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
									personDepartmentTagLogRepository.save(pl);
									if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true)
									personDepartmentTag.setPreviousReaderNo(rfidReader.getReaderNo());
									if(personDepartmentTag.getModule().getDoorNo()==1 
											&& rfidReader.getReaderNo()==2)
									{
										authorisedRfid.setAccess("DENIED");
									}
									pl=null;
									break;
								}
								
							}
							
						}
						if(access==true)
						{
							break;
						}
						
					}
					
				}
				
			}
			else if(authorisedRfid.getPersonType().getDept().equals(new String("ADMIN"))
					&& (authorisedRfid.getLoginCheck().equals(Boolean.TRUE) 
							&& authorisedRfid.getAccess().equals("DENIED")
							&& doorNo.intValue()==1 && readerNo.intValue()==1))
			{

				for (PersonDepartmentTag personDepartmentTag : personDepartmentTags) {
					if(personDepartmentTag.getDeactivationDate()==null 
							&& personDepartmentTag.getModule()!=null
							&& personDepartmentTag.getModule().getDeactivationDate()==null
							&& personDepartmentTag.getModule().getDoorNo()==doorNo.intValue())
					{
						
						Integer prevReaderNo =null;
						if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true){
							prevReaderNo =personDepartmentTag.getPreviousReaderNo();
						}	
			
						List<RfidReader> rfidReaders =personDepartmentTag.getModule().getRfidReaders();
						
						for (RfidReader rfidReader : rfidReaders) {
							if(rfidReader.getDeactivationDate()==null 
									&& rfidReader.getReaderNo()==readerNo.intValue())
							{
								currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");

								if(prevReaderNo==null 
										|| (prevReaderNo!=null 
											&& prevReaderNo.intValue()!=rfidReader.getReaderNo()))
								{

									access=true;
									PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
									personDepartmentTagLogRepository.save(pl);

									if(authorisedRfid.getPersonType().getPreviousEntryCheck().booleanValue()==true)
          							   personDepartmentTag.setPreviousReaderNo(rfidReader.getReaderNo());

									if(personDepartmentTag.getModule().getDoorNo()==1 
											&&rfidReader.getReaderNo()==1)
									{
										authorisedRfid.setAccess("AUTHORIZED");
									}
									pl=null;
									break;
								}
								
							}
						}
						if(access==true)
						{
							break;
						}
					}
					
				}
				
			}
			else if(!authorisedRfid.getPersonType().getDept().equals(new String("ADMIN")))
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
										currentLog=(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT");
										access=true;
										PersonDepartmentTagLog pl = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),currentLog,"GRANTED",personDepartmentTag.getId());
										personDepartmentTagLogRepository.save(pl);
										pl=null;
										break;
									}
									if(access==true)
									{
										break;
									}
								}
							}
							if(access==true)
							{
								break;
							}
							
						}
				
					}
					if(access==true)
					{
						break;
					}
				}
				
			}
			
			if(access==false)
			{	
				RfidReader rfidReader = rfidReaderRepository.getRfidReaderByDoorNoReaderNo(doorNo, readerNo);
				PersonDepartmentTagLog pl2 = new PersonDepartmentTagLog(authorisedRfid.getId() ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"DENIED",null);
				personDepartmentTagLogRepository.save(pl2);
				authorisedRfid.setPermission("DENIED");
				authorisedRfid.setLog(currentLog);
				authorisedRfid.setLogTime(System.currentTimeMillis());
				authorisedRfid.setCurrentPosition("D"+rfidReader.getModule().getDoorNo());
				personalDetailsRepository.save(authorisedRfid);
				log.info("DENIED:"+authorisedRfid.getCode()+ ":"+authorisedRfid.getCurrentPosition()+":"+currentLog);
				pd.setAccess("false");
				pd.setCode(authorisedRfid.getCode());
				pl2=null;
			}
			else
			{
				authorisedRfid.setPermission("GRANTED");
				authorisedRfid.setLog((Math.floorMod(readerNo, 2L)!=0?"IN":"OUT"));
				authorisedRfid.setLogTime(System.currentTimeMillis());
				authorisedRfid.setCurrentPosition("D"+doorNo);
			    personalDetailsRepository.save(authorisedRfid);
			    log.info("GRANTED:"+authorisedRfid.getCode()+ ":"+authorisedRfid.getCurrentPosition()+":"+currentLog);
				pd.setAccess("true");
				if(doorNo.intValue()==1) {
					pd.setCode(authorisedRfid.getCode()+" "+authorisedRfid.getLog()+" "+DateTimeUtils.getTime(authorisedRfid.getLogTime()));
				}	
				else {
					pd.setCode(authorisedRfid.getCode());
				}	
			}
		}
		else	
		{
			RfidReader rfidReader = rfidReaderRepository.getRfidReaderByDoorNoReaderNo(doorNo, readerNo);
			PersonDepartmentTagLog pl2 = new PersonDepartmentTagLog(-1L ,rfidReader.getModule().getId(), rfidReader.getId(),(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"),"INVALID",null);
			personDepartmentTagLogRepository.save(pl2);
			log.info("INVALID:"+rfidTagId+ ":D"+rfidReader.getModule().getDoorNo()+":"+(Math.floorMod(rfidReader.getReaderNo(), 2L)!=0?"IN":"OUT"));
			pl2=null;
			pd.setCode("INVALID");

		}
		
		long end = System.currentTimeMillis();
	    log.info("Run time: " + Long.toString(end - start));
		return pd;
	}	

	@Override
	public List<PersonalDetails> listAll(RfidTagStatus status,String dept,String keywords) {
		log.info("listAll");
		List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
		if(keywords!=null)
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDeptKeyword(status,dept,keywords).forEach(personalDetailss::add);
		else
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(status,dept).forEach(personalDetailss::add);
			
	    return personalDetailss;
	    
	    
	}
	@Override
	public List<PersonalDetails> listAll(RfidTagStatus status,String dept,String keywords,String searchBy) {
		log.info("listAllSearchBy");
		List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
		if(keywords!=null && searchBy!=null && searchBy.equals("code")==true)
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDeptKeywordSearchByCode(status,dept,keywords).forEach(personalDetailss::add);
		else if(keywords!=null && searchBy!=null && searchBy.equals("firstName")==true)
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDeptKeywordSearchByFirstName(status,dept,keywords).forEach(personalDetailss::add);
		else if(keywords!=null && searchBy!=null && searchBy.equals("lastName")==true)
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDeptKeywordSearchByLastName(status,dept,keywords).forEach(personalDetailss::add);
		else
			personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(status,dept).forEach(personalDetailss::add);
	    return personalDetailss;
	}
	
	@Override
	public void save(PersonalDetails personalDetails) {
		log.info("save");
		personalDetailsRepository.save(personalDetails);
	}

	@Override
	public PersonalDetails get(Long id) {
		log.info("get(id)");
		return personalDetailsRepository.getId(id);
	}

	@Override
	public void delete(Long id) {
		log.info("delete(id)");
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
		log.info("findPaginated");
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
	public Page<PersonalDetails> findPaginated(Pageable pageable,RfidTagStatus status,String dept,String keyword,String searchBy) {
		log.info("findPaginated");
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        personalDetailss=this.listAll(status,dept,keyword,searchBy);
        		
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
		log.info("getCode");
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
	
	@Override
	public Boolean backupDB(String dir) {
		log.info("backupDB");
		int i= personalDetailsRepository.backupDB(dir+"/backupDB");
		return i==0? Boolean.TRUE:Boolean.FALSE;
	}

}	
