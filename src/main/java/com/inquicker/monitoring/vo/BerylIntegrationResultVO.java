package com.inquicker.monitoring.vo;

import java.util.ArrayList;
import java.util.List;

public class BerylIntegrationResultVO {
	
	private String startDateTime;
	private String endDateTime;
	
	List<BerylIntegrationJobStatusVO> jobStatus = new ArrayList<BerylIntegrationJobStatusVO>();

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public List<BerylIntegrationJobStatusVO> getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(List<BerylIntegrationJobStatusVO> jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	

}
