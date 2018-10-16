package com.inquicker.monitoring.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.inquicker.monitoring.main.IQMonitor;
import com.inquicker.monitoring.vo.BerylIntegrationJobStatusVO;
import com.inquicker.monitoring.vo.BerylIntegrationResultVO;

import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;
import in.ashwanthkumar.slack.webhook.SlackAttachment.Field;
import okhttp3.Response;

public class BerylIntegrationSlack {

	private final static LoggerUtil LOGGER = LoggerUtil.getLogger(SlackUtil2.class);

	String webhookUrl = null;
	String slackChannel = null;

	private static String colorRed = "#ff0000";
	private static String colorGreen = "#00ff00";

	public BerylIntegrationSlack() {
		String apiKey = IQMonitor.props.getProperty("GarySlackAPIKey");
		webhookUrl = "https://hooks.slack.com/services/" + apiKey;
		slackChannel = IQMonitor.props.getProperty("GarySlackChannel");

		// String apiKey = IQMonitor.props.getProperty("SlackAPIKey");
		// webhookUrl = "https://hooks.slack.com/services/" + apiKey;
		// slackChannel = IQMonitor.props.getProperty("SlackChannel");
	}

	public void sendSlackBerylIntegrationStatImage(String html) {

		try {
			
			String htmlFileName = IQMonitor.props.getProperty("HtmlFileFullPath");  
			File file = new File(htmlFileName);
			FileUtils.writeStringToFile(file, html, "utf-8");

			String outputFileName = IQMonitor.props.getProperty("ImageFileFullpath"); 

			File uploadFile = new File(outputFileName);

			XHTMLToImage.convertToImage(htmlFileName, outputFileName);

			Response response = new Meteoroid.Builder()
					.token(IQMonitor.props.getProperty("GarySlackToken"))  // "xoxp-169845299954-169880221620-188757618775-658e5b4f3f852328f695235c1714a0c2")
					.uploadFile(uploadFile).channels("#general").title("IQ Beryl Run Statistics") //.initialComment("test comment")
					.build().post();

			System.out.println("All done....");

		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private List<SlackAttachment> buildJobAttachment(String line, String dbEnv) {

		List<SlackAttachment> list = new ArrayList<SlackAttachment>();

		String color = "#00ff00";

		int row = 0;
		Field analyticInfo = new Field("Environment - (" + dbEnv + ")", line, false);

		SlackAttachment attachment = new SlackAttachment("").color(color)
				// .title("Analytic Info")
				.addField(analyticInfo);

		list.add(attachment);
		return list;
	}

	private SlackAttachment transformSlackAttachment(BerylIntegrationJobStatusVO vo) {

		Field insertCount = new Field("Inserted", new Integer(vo.getInsertCount()).toString(), true);
		Field updateCount = new Field("Updated", new Integer(vo.getUpdateCount()).toString(), true);
		Field deleteCount = new Field("Deleted", new Integer(vo.getDeleteCount()).toString(), true);
		Field invalidCount = new Field("Invalid", new Integer(vo.getInvalidCount()).toString(), true);

		String color = colorGreen;

		if (vo.getStatus().equals("FAILED"))
			color = colorRed;

		SlackAttachment attachment = new SlackAttachment("").color(color)
				.title(vo.getDescription() + " - (" + vo.getStatus() + ")").addField(insertCount).addField(updateCount)
				.addField(deleteCount).addField(invalidCount);

		return attachment;
	}

	private SlackAttachment transformRunTim(BerylIntegrationResultVO vo, String runDate) {

		String color = "#3A3EAF";
		Field startDate = new Field("Start: ", vo.getStartDateTime(), true);
		Field endDate = new Field("End: ", vo.getEndDateTime(), true);

		SlackAttachment attachment = new SlackAttachment("").color(color).title("Run Date: " + runDate)
				.addField(startDate).addField(endDate);

		return attachment;
	}

	private String getRunDate() {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date = simpleDateFormat.format(new Date());
		return date;
	}

	public void sendSlackBerylIntegrationStat(BerylIntegrationResultVO result, List<String> formattedResult) {

		try {

			String line = "";

			String icon = ":page_with_curl:";

			List<SlackAttachment> jobAttachments = new ArrayList<SlackAttachment>();
			jobAttachments.add(this.transformRunTim(result, this.getRunDate()));

			for (BerylIntegrationJobStatusVO jobStatus : result.getJobStatus()) {

				jobAttachments.add(transformSlackAttachment(jobStatus));
			}

			new Slack(webhookUrl).icon(icon).sendToChannel(slackChannel).displayName("Beryl Integration run stat ")
					.push(jobAttachments);

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			LOGGER.error(exception.getMessage());
			LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception));
		}

	}

	public void sendSlackJobMessage(String line, String dbEnv) {
		try {

			List<SlackAttachment> jobAttachments = buildJobAttachment(line, dbEnv);
			new Slack(webhookUrl).icon(":page_with_curl:").sendToChannel(slackChannel)
					.displayName("PHI Uploader job not run ").push(jobAttachments);

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

			SlackAttachment attachment1 = new SlackAttachment("").color("#3AA3E3").title("Exception")
					.text(exception.getMessage()).addField(job).addField(method);

			SlackAttachment attachment2 = new SlackAttachment("").color("#B12").addField(stackTraceField);
			attachments.add(attachment1);
			attachments.add(attachment2);

			new Slack(webhookUrl).sendToChannel(slackChannel).push(attachments);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
