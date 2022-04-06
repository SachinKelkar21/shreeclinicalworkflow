package com.shree.clinicalworkflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public final class PersonDepartmentTagLog {
	@Id
	@GeneratedValue
	@Column(name="PERSONAL_DEPARTMENT_TAG_LOG_ID")
	private Long id;
	
	private final Long tagId;
	
	private final Long moduleId;
	
	private final Long rfidReaderId;
	
	private final Long personDepartmentTagId;
	
	private final String log;
	
	private final Long logTime;
	
	private final String permission;
	
	private final LocalDateTime logLocalDateTime;
	
	
	public PersonDepartmentTagLog() {
		
		tagId=null;
		moduleId=null;
		rfidReaderId=null;
		personDepartmentTagId=null;
		log=null;
		logTime=null;
		permission=null;
		logLocalDateTime=null;
		
	}
	public PersonDepartmentTagLog(
			final Long tagId,
			final Long moduleId,
			final Long rfidReaderId,
			final String log,
			final String permission,
			final Long personDepartmentTagId
			) {
		this.tagId=tagId;
		this.moduleId=moduleId;
		this.rfidReaderId=rfidReaderId;
		this.log=log;
		this.logTime=System.currentTimeMillis();
		this.permission=permission;
		this.logLocalDateTime=LocalDateTime.now();
		this.personDepartmentTagId=personDepartmentTagId;
	}

}
