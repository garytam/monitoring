package com.inquicker.monitoring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.inquicker.monitoring.utils.DatabaseUtils;
import com.inquicker.monitoring.utils.LoggerUtil;
import com.inquicker.monitoring.vo.BerylIntegrationJobStatusVO;
import com.inquicker.monitoring.vo.BerylIntegrationResultVO;

public class BerylIntegrationDAO {

	private static final LoggerUtil LOGGER          = LoggerUtil.getLogger(EventLogsDAO.class); 
	
	
	public BerylIntegrationResultVO getBeryIntegrationResult(Connection connection, String runDate){
		
		BerylIntegrationResultVO resultVO = new BerylIntegrationResultVO();
		
		List<BerylIntegrationJobStatusVO> list = new ArrayList<BerylIntegrationJobStatusVO>();
		PreparedStatement preparedStatement = null;   
		PreparedStatement preparedStatement2 = null;  
		
		try {
			
			String sql = "select run_date " + 
					", sequence " + 
					", job_name " + 
					", description " + 
					", status " + 
					", run_note " + 
					", insert_count " + 
					", update_count " + 
					", delete_count " + 
					", invalid_count " + 
					"from beryl_run_control " + 
					"where run_date = ? " + 
					"order by sequence";
			
			
			preparedStatement = connection.prepareStatement(sql);  
			preparedStatement.setString(1, runDate);
			
			ResultSet resultSet = preparedStatement.executeQuery(); 
			 
			
			while (resultSet.next()) { 
				BerylIntegrationJobStatusVO vo = new BerylIntegrationJobStatusVO();
				vo.setRunDate(resultSet.getString("run_date"));
				vo.setJobName(resultSet.getString("job_name"));
				vo.setDescription(resultSet.getString("description"));
				vo.setStatus(resultSet.getString("status"));
				vo.setRunNote(resultSet.getString("run_note"));
				vo.setInsertCount(resultSet.getInt("insert_count"));
				vo.setUpdateCount(resultSet.getInt("update_count"));
				vo.setDeleteCount(resultSet.getInt("delete_count"));
				vo.setInvalidCount(resultSet.getInt("invalid_count"));
				
				list.add(vo);
			}
			
			
			String sql2 = "select convert(varchar, max(created_at), 21) job_end_date " + 
					", convert(varchar, min(created_at), 21) job_start_date " + 
					"from log " + 
					"where convert(varchar, created_at, 112) = ? ";
			preparedStatement2 = connection.prepareStatement(sql2);  
			preparedStatement2.setString(1, runDate);
			
			ResultSet resultSet2 = preparedStatement2.executeQuery(); 
			 
			
			while (resultSet2.next()) { 
				resultVO.setEndDateTime(resultSet2.getString("job_end_date"));
				resultVO.setStartDateTime(resultSet2.getString("job_start_date"));
				
			}
			
			resultVO.setJobStatus(list);
		 
		} catch (Exception exception) {
			DatabaseUtils.handleExcpetion(exception, connection, preparedStatement); 
			throw new RuntimeException(exception);
		}
		  
		
		return resultVO;
	}
}
