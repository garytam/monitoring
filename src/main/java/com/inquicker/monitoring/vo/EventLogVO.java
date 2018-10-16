package com.inquicker.monitoring.vo;

import java.util.Date;

public class EventLogVO {

	//hs.name, pu.health_system_id, pu.frequency, DATE_FORMAT(CONVERT_TZ(pu.last_sent_at,'+00:00','-04:00'), '%Y-%m-%d') as 'last_send_date'
	
	private String healthSystemName;
	private int healthSystemId;
	private Date lastSendDate;
	private String uuid;
	private String frequency;
	
	
	
	
	
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getHealthSystemName() {
		return healthSystemName;
	}
	public void setHealthSystemName(String healthSystemName) {
		this.healthSystemName = healthSystemName;
	}
	public int getHealthSystemId() {
		return healthSystemId;
	}
	public void setHealthSystemId(int healthSystemId) {
		this.healthSystemId = healthSystemId;
	}
	public Date getLastSendDate() {
		return lastSendDate;
	}
	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
