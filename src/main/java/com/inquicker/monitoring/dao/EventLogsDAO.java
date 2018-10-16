package com.inquicker.monitoring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inquicker.monitoring.utils.DatabaseUtils;
import com.inquicker.monitoring.utils.LoggerUtil;
import com.inquicker.monitoring.vo.EventLogVO; 

public class EventLogsDAO {

	private static final LoggerUtil LOGGER          = LoggerUtil.getLogger(EventLogsDAO.class);
	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	
	
	public static Map<String, List<EventLogVO>> result = new HashMap<String, List<EventLogVO>>();
	
	private static void addToMap(EventLogVO vo) {
		
		if (result.containsKey(vo.getFrequency())) {
			result.get(vo.getFrequency()).add(vo);
		} else {
			List<EventLogVO> list = new ArrayList<EventLogVO>();
			list.add(vo);
			result.put(vo.getFrequency(), list);
		}
		 
	}
	
	public static Map<String, List<EventLogVO>> getPhiLoaderJobs(Connection connection, String frequency){
		
		List<EventLogVO> list = new ArrayList<EventLogVO>();
		PreparedStatement preparedStatement = null;   
		
		try {
			
			String sql = "select hs.name as health_system_name " +
					     ", pu.health_system_id  as health_system_id" +
					     ", pu.frequency " +
					     ", DATE_FORMAT(CONVERT_TZ(pu.last_sent_at,'+00:00','-04:00'), '%Y-%m-%d') as last_run_date " +
					     ", pu.uuid " + 
					     "from phi_uploaders pu  " + 
					     ", health_systems hs " + 
					     "where frequency in (" + frequency + ") " + 
					     "and pu.active = 1 " + 
					     "and pu.health_system_id = hs.id " + 
					     "order by hs.name, frequency";
			
			
			preparedStatement = connection.prepareStatement(sql);  
			
			ResultSet resultSet = preparedStatement.executeQuery(); 
			 
			
			while (resultSet.next()) { 
				EventLogVO vo = new EventLogVO();
				vo.setHealthSystemName(resultSet.getString("health_system_name"));
				vo.setHealthSystemId(resultSet.getInt("health_system_id"));
				vo.setLastSendDate(parseDate(resultSet.getString("last_run_date")));
				vo.setUuid(resultSet.getString("uuid"));
				vo.setFrequency(resultSet.getString("frequency"));
				addToMap(vo);
			}
			
		 
		} catch (Exception exception) {
			DatabaseUtils.handleExcpetion(exception, connection, preparedStatement); 
			throw new RuntimeException(exception);
		}
		  
		
		return result;
	}
	
	private static Date parseDate(String dateString) {
		try {
			return (Date) formatter.parse(dateString); //
		} catch (Exception e) {
			LOGGER.error("Fail to parse last_run_date => (" + dateString + ")");
		}
		
		return null;
	}
}
