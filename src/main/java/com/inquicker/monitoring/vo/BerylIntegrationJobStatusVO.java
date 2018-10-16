package com.inquicker.monitoring.vo;

public class BerylIntegrationJobStatusVO {

	private String runDate;
	private String jobName;
	private String description;
	private String status;
	private String runNote;
	private int sequence;
	private int insertCount;
	private int updateCount;
	private int deleteCount;
	private int invalidCount;
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRunNote() {
		return runNote;
	}
	public void setRunNote(String runNote) {
		this.runNote = runNote;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getInsertCount() {
		return insertCount;
	}
	public void setInsertCount(int insertCount) {
		this.insertCount = insertCount;
	}
	public int getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}
	public int getDeleteCount() {
		return deleteCount;
	}
	public void setDeleteCount(int deleteCount) {
		this.deleteCount = deleteCount;
	}
	public int getInvalidCount() {
		return invalidCount;
	}
	public void setInvalidCount(int invalidCount) {
		this.invalidCount = invalidCount;
	}
	@Override
	public String toString() {
		return "BerylIntegrationResultVO [runDate=" + runDate + ", jobName=" + jobName + ", description=" + description
				+ ", status=" + status + ", runNote=" + runNote + ", sequence=" + sequence + ", insertCount="
				+ insertCount + ", updateCount=" + updateCount + ", deleteCount=" + deleteCount + ", invalidCount="
				+ invalidCount + "]";
	}
	
	
	
	
}
