package com.shree.clinicalworkflow.dto;

public class PersonLog {

	private Long logDateTime;
	private String permission;
	private Integer door;
	private String log;
	
	public PersonLog(Long logDateTime,String permission, Integer door, String log) {
		super();
		this.logDateTime = logDateTime;
		this.permission = permission;
		this.door = door;
		this.log = log;
	}
	
	
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Integer getDoor() {
		return door;
	}
	public void setDoor(Integer door) {
		this.door = door;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}


	public Long getLogDateTime() {
		return logDateTime;
	}


	public void setLogTime(Long logDateTime) {
		this.logDateTime = logDateTime;
	}



	
}
