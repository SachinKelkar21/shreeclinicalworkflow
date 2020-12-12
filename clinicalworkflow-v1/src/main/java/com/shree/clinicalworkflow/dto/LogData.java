package com.shree.clinicalworkflow.dto;

public class LogData {
	public LogData(String log, long logCount) {
		super();
		this.log = log;
		this.logCount = logCount;
	}
	private String log;
	private long logCount;
	
	public LogData() {
		// TODO Auto-generated constructor stub
	}
	
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public long getLogCount() {
		return logCount;
	}
	public void setLogCount(long logCount) {
		this.logCount = logCount;
	}

}
