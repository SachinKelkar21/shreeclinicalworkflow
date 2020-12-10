package com.shree.clinicalworkflow.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public final class PersonDepartmentTagLog {
	@Id
	@GeneratedValue
	@Column(name="PERSONAL_DEPARTMENT_TAG_LOG_ID")
	private Long id;
		
	private final Long tagId;
	
	private final Long moduleId;
	
	private final Long rfidReaderId;
	
	private final String log;
	
	private final Long logTime;
	
	private final String permission;
	
	public PersonDepartmentTagLog() {
		tagId=null;
		moduleId=null;
		rfidReaderId=null;
		log=null;
		logTime=null;
		permission=null;
		
	}
	public PersonDepartmentTagLog(
			final Long tagId,
			final Long moduleId,
			final Long rfidReaderId,
			final String log,
			final String permission) {
		this.tagId=tagId;
		this.moduleId=moduleId;
		this.rfidReaderId=rfidReaderId;
		this.log=log;
		this.logTime=System.currentTimeMillis();
		this.permission=permission;
	}
}
