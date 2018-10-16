package com.inquicker.monitoring.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inquicker.monitoring.main.IQMonitor_PHI_jobs;
import com.inquicker.monitoring.vo.EventLogVO;

import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;
import in.ashwanthkumar.slack.webhook.SlackAttachment.Field;

public class SlackUtil2 {
	
	private final static LoggerUtil LOGGER = LoggerUtil.getLogger(SlackUtil2.class);
	
	String webhookUrl = null;
	String slackChannel = null;
	
	public SlackUtil2() {
		String apiKey = IQMonitor_PHI_jobs.props.getProperty("GarySlackAPIKey");
		webhookUrl = "https://hooks.slack.com/services/" + apiKey;
		slackChannel = IQMonitor_PHI_jobs.props.getProperty("GarySlackChannel");
	}
	 
	private List<SlackAttachment> buildJobAttachment(List<EventLogVO> visitCountList) {
		
		List<SlackAttachment> list = new ArrayList<SlackAttachment>();
		
		String color1 = "#00ff00";
		String color2 = "#00aeff";
		String color = "";
		
		int row = 0;
		
		String line = "";
		
//		for (FactVisitCounts visitCounts : visitCountList) {
//			Field registartionDate = new Field("Registration Date", visitCounts.getRegistration_date(), false);
//			Field visitWithAnalytic = new Field("Visits with Analytic Data", visitCounts.getVisitsWithAnalyticDataString(), true);
//			Field visitWithoutAnalytic = new Field("Visits without Analytic Data", visitCounts.getVisitsWithoutAnalyticDataString(), true);
//			
//			row ++;
//			
//			if (row%2 == 0) {
//				color = color1;
//			} else {
//				color = color2;
//			}
//			
//			SlackAttachment attachment = new SlackAttachment("") 
//					.color(color)
////					.title("Job completed successful")
//					.addField(registartionDate)
//					.addField(visitWithAnalytic)
//					.addField(visitWithoutAnalytic);
//			
//			list.add(attachment);
//		}
		 
		
		
		
		
		return list;
	}
	
	private List<SlackAttachment> buildJobAttachment(String line, String dbEnv) {
		
		List<SlackAttachment> list = new ArrayList<SlackAttachment>();
		
		String color  = "#00ff00"; 
		
		int row = 0;
		Field analyticInfo = new Field("Environment - (" + dbEnv + ")", line, false);  
			
			SlackAttachment attachment = new SlackAttachment("") 
					.color(color)
//					.title("Analytic Info")
					.addField(analyticInfo);
			
		list.add(attachment);
		return list;
	}
	
	private SlackAttachment buildJobAttachment(String line, String dbEnv, String color) {
		 
		
		int row = 0;
		Field analyticInfo = new Field(" - (" + dbEnv + ")", line, false);  
			
			SlackAttachment attachment = new SlackAttachment("") 
					.color(color) 
					.addField(analyticInfo);
			
		return attachment;
	}


	private List<SlackAttachment> buildJobAttachments(String line, String frequency, String dbEnv, String color) {
		
		List<SlackAttachment> list = new ArrayList<SlackAttachment>();
		 
		
		int row = 0;
		Field analyticInfo = new Field(frequency.toUpperCase() + " - (" + dbEnv + ")", line, false);  
			
		SlackAttachment attachment = new SlackAttachment("") 
				.color(color) 
				.addField(analyticInfo);
			
		list.add(attachment);
		return list;
	}
	
	public void sendSlackJobStat(Map<String, List<EventLogVO>> result, String dbEnv) {
		try {
	
			 
			
			String line = "";
			String colorRed    = "#ff0000";
			String colorGreen  = "#00ff00"; 
			String color = "";
			String icon = ":page_with_curl:";  
			 
			
			for (String frequency : result.keySet()) {
				for (EventLogVO vo : result.get(frequency)) {
					line = line + vo.getHealthSystemName() + "\n       last send date: " + vo.getLastSendDate() + 
							"\n      UUID:" + vo.getUuid() + "\n";
					
				}
				
				List<SlackAttachment> jobAttachments = buildJobAttachments(line, frequency, dbEnv, color);
				
				color = colorRed;
				icon = ":bomb:";
				
				new Slack(webhookUrl).icon(icon).sendToChannel(slackChannel).displayName("PHI Uploader job status ").push(jobAttachments);
			}
		 		
			
			
			 
			
			
			
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			LOGGER.error(exception.getMessage());
			LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception));
		}
		
	}


	public void sendSlackJobMessage(String line, String dbEnv) {
		try {

			
			List<SlackAttachment> jobAttachments = buildJobAttachment(line, dbEnv);
			new Slack(webhookUrl).icon(":page_with_curl:").sendToChannel(slackChannel).displayName("PHI Uploader job not run ").push(jobAttachments);
			
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			LOGGER.error(exception.getMessage());
			LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception));
		}
		
	}

	public void sendSlackException(Exception exception, String jobName, String methodName) {
		
		
		 
		Field job = new Field("Job", jobName, true);
		Field method = new Field("Method", methodName, true);
		
		String stackTrace = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception);
		
		Field stackTraceField = new Field("StackTrace", stackTrace, false);
		
		try {
			List<SlackAttachment> attachments = new ArrayList<SlackAttachment>();
			
			SlackAttachment attachment1 = new SlackAttachment("") 
					.color("#3AA3E3")
					.title("Exception")
					.text(exception.getMessage())
					.addField(job)
					.addField(method);
			
			SlackAttachment attachment2= new SlackAttachment("") 
					.color("#B12")
					.addField(stackTraceField);
			attachments.add(attachment1);
			attachments.add(attachment2);
			
			new Slack(webhookUrl).sendToChannel(slackChannel)
			.push(attachments);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
