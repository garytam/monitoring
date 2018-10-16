package com.inquicker.monitoring.main;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.inquicker.monitoring.dao.EventLogsDAO;
import com.inquicker.monitoring.utils.CommonUtils;
import com.inquicker.monitoring.utils.DatabaseUtils;
import com.inquicker.monitoring.utils.LoggerUtil;
import com.inquicker.monitoring.utils.SlackUtil2;
import com.inquicker.monitoring.vo.EventLogVO;


public class IQMonitor_PHI_jobs {

	public static String DB_USE       = null;
	static Connection iqappConn       = null;
	static Connection iqetlConn       = null; 
	public static Properties props    = null;	  
	
	public static String reportDirDateTime;
	
	private final LoggerUtil LOGGER          = LoggerUtil.getLogger(IQMonitor_PHI_jobs.class);
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static Date today = new Date();
	
	private static Map<String, List<EventLogVO>> addToMap(EventLogVO vo, Map<String, List<EventLogVO>> result) {
	
		
		if (result.containsKey(vo.getFrequency())) {
			result.get(vo.getFrequency()).add(vo);
		} else {
			List<EventLogVO> list = new ArrayList<EventLogVO>();
			list.add(vo);
			result.put(vo.getFrequency(), list);
		}
		 
		return result;
	}

	private static Map<String, List<EventLogVO>> validateLastRunDate (Map<String, List<EventLogVO>> result){
		
		String formattedDate = dateFormat.format(today);
		
		Map<String, List<EventLogVO>> jobNotRun = new HashMap<String, List<EventLogVO>>();
		
		for (String key : result.keySet()) {
			for (EventLogVO vo : result.get(key)) {
				String runDate = dateFormat.format(vo.getLastSendDate());
				
				if (! runDate.equals(formattedDate)) {
					jobNotRun = addToMap(vo, jobNotRun);
				}
			}
		}
		
		
		return jobNotRun;
	}
 
	private void phiUploader(String frequency) {
		
		try {
			
			props = CommonUtils.loadProperties();
			 
			DB_USE = props.getProperty("DB_USE");
			iqappConn = DatabaseUtils.makeJDBCConnectionIqApp(props, DB_USE); 
			
			Map<String, List<EventLogVO>> result = EventLogsDAO.getPhiLoaderJobs(iqappConn, frequency);
			 
			Map<String, List<EventLogVO>> jobNotRun = validateLastRunDate(result);
			
			SlackUtil2 slackUtil = new SlackUtil2();
			slackUtil.sendSlackJobStat(jobNotRun, DB_USE); 
			
			System.out.println("All Done...");
			
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	// TO BUILD
	// mvn clean compile assembly:single
	// To RUN
	// copy env.properties and log4j.properties to current directory
	// java -Denv.properties=./env.properties -Dlog4j.configuration=file:"./log4j.properties" -jar IQMonitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar 

	public static void main(String[] args) {
		IQMonitor_PHI_jobs montor = new IQMonitor_PHI_jobs();
		String runFrequency = getJobFrequency();
		montor.phiUploader(runFrequency);
		
		 
		
	}

	private static String getJobFrequency() {
		Date date = today;
		int todayDay= new Integer(new SimpleDateFormat("dd").format(date)).intValue();
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		 
		
		String runFrequency = "'daily'";
		
		if (todayDay == 1) {
			runFrequency = runFrequency + ", 'monthly'";
		}
		
		if (dayOfWeek == 1) {
			runFrequency = runFrequency + ", 'weekly'";
		}
		
		runFrequency = "'daily', 'monthly'";
		return runFrequency;
	}
}
