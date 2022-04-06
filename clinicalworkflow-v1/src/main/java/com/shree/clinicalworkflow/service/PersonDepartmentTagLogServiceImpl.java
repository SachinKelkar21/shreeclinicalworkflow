package com.shree.clinicalworkflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagLogRepository;

@Service
public class PersonDepartmentTagLogServiceImpl implements PersonDepartmentTagLogService {
	
	@Autowired
	PersonDepartmentTagLogRepository personDepartmentTagLogRepository;
	
	//mili sec,sec,hour,month,year,weedday
	@Override
	//@Scheduled(cron = "0 59 23 ? * SUN")
	public void removeOneWeekDataOnEverySunday() {
		long now = System.currentTimeMillis();
		long oneWeekDate = now -(7*24*60*60*1000);
		personDepartmentTagLogRepository.removeOneWeekDataOnEverySunday(oneWeekDate);
	}
	
	/*
	@Override
	@Scheduled(cron ="0 15 10 15 * ?")
	public void removeTwoMonthkDataOnEveryMonth() {
		
		long now = System.currentTimeMillis();
		long oneDate = now -(60*24*60*60*1000);
		personDepartmentTagLogBkpRepository.removeTwoMonthsData(oneDate);
	}
	*/
}
