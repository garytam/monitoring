package com.inquicker.monitoring.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
 

public class DatabaseUtils {

	private final static LoggerUtil LOGGER = LoggerUtil.getLogger(DatabaseUtils.class);
	
	/***********************************/
	/* makeJDBCConnectionIqEtl         */
	/***********************************/
	public static Connection makeJDBCConnectionBerylStaging(Properties props ) {
		
		Connection conn = null; 
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			LOGGER.info("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return conn;
		}
 
		try { 
			
			String url = props.getProperty("berylStagingDatabaseUrl");
			String user = props.getProperty("beryStagingDatabaseUser");
			String pswd = props.getProperty("beryStagingDatabasePswd");
			 conn = DriverManager.getConnection(url, user, pswd);
			
			if (conn != null) {
				LOGGER.info("Connection Successful!");
			} else {
				LOGGER.info("Failed to make connection!");
			}
		} catch (SQLException e) {
			LOGGER.info("SqlServer BerylStaging Connection Failed!");
			e.printStackTrace();
			return conn;
		}
		
		return conn;
	}
	
	
	
	/***********************************/
	/* makeJDBCConnectionIqEtl         */
	/***********************************/
	public static Connection makeJDBCConnectionIqEtl(Properties props, String dbUse) {
		
		Connection conn = null; 
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.info("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return conn;
		}
 
		try { 
			if (dbUse.equals("DEV"))
				conn = DriverManager.getConnection(props.getProperty("iqetlDevDatabaseUrl"), props.getProperty("iqetlDevDatabaseUser"), props.getProperty("iqetlDevDatabasePswd"));
			
			if (dbUse.equals("PROD"))
				conn = DriverManager.getConnection(props.getProperty("iqetlProdDatabaseUrl"), props.getProperty("iqetlProdDatabaseUser"), props.getProperty("iqetlProdDatabasePswd"));
			
			if (conn != null) {
				LOGGER.info("Connection Successful!");
			} else {
				LOGGER.info("Failed to make connection!");
			}
		} catch (SQLException e) {
			LOGGER.info("MySQL Connection Failed!");
			e.printStackTrace();
			return conn;
		}
		
		return conn;
	}
	

	/***********************************/
	/* makeJDBCConnectionIqApp         */
	/***********************************/
	public static Connection makeJDBCConnectionIqApp(Properties props, String env) {
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.info("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return conn;
		}
 
		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.
			
			if (env.equals("DEV"))
				conn = DriverManager.getConnection(props.getProperty("iqappDevDatabaseUrl"), props.getProperty("iqetlDevDatabaseUser"), props.getProperty("iqetlDevDatabasePswd"));
			
			if (env.equals("PROD"))
				conn = DriverManager.getConnection(props.getProperty("iqappProdDatabaseUrl"), props.getProperty("iqappProdDatabaseUser"), props.getProperty("iqappProdDatabasePswd"));
			
			if (conn != null) {
				LOGGER.info("Connection Successful!");
			} else {
				LOGGER.info("Failed to make connection!");
			}
		} catch (SQLException e) {
			LOGGER.info("MySQL Connection Failed!");
			e.printStackTrace();
			return conn;
		}
		
		return conn;
 
	}
	
	/***********************************/
	/* handleExcpetion                 */
	/***********************************/
	public static void handleExcpetion(Exception exception, Connection conn, PreparedStatement preparedStatement) {
		exception.printStackTrace();
		DatabaseUtils.closePrepareStatement(preparedStatement);
		DatabaseUtils.closeConnection(conn); 
		LOGGER.error(exception.getMessage());
		LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception)); 
	}
	
	public static void handleExcpetion(Exception exception, Connection conn1, Connection conn2, PreparedStatement preparedStatement) {
		DatabaseUtils.closeConnection(conn2);
		handleExcpetion(exception, conn1, preparedStatement);
		
	}

	/***********************************/
	/* closePrepareStatement           */
	/***********************************/
	public static void closePrepareStatement(PreparedStatement pstm) {
		
		if (pstm == null)
			return;
		
		try {
			pstm.close();
		} catch(SQLException sqle) {
			LOGGER.warning("Fail to close prepared statment => " + sqle.getMessage());
			sqle.printStackTrace();
		}
	}
	
	/***********************************/
	/* closeConnection                 */
	/***********************************/
	public static void closeConnection(Connection conn) {
		
		if (conn == null)
			return;
		
		try { 
			conn.close();
		} catch(SQLException sqle) {
			LOGGER.warning("Fail to close Connection => " + sqle.getMessage());
			sqle.printStackTrace();
		}
	}
	
	/***********************************/
	/* closeResultSet                  */
	/***********************************/
	public static void closeResultSet(ResultSet resultSet) {
		
		if (resultSet == null)
			return;
		
		try { 
			resultSet.close();
		} catch(SQLException sqle) {
			LOGGER.warning("Fail to close resultSet => " + sqle.getMessage());
			sqle.printStackTrace();
		}
	}
	 
	 
	public static final String scheduleSql = 
			"select s.id as 'schedule_id' " + 
			"	, s.active as 'schedule_active_ind' " + 
			"	, s.type as 'schedule_type'  " + 
			"   , s.hidden as 'schedule_hidden' " +
			"   , r.name as 'region_name' " +
			"	, f.name as 'facility_name'  " + 
			"	, loc.id as 'location_id' " + 
			"	, loc.name as 'location_name' " + 
			"	, loc.address as 'location_street'  " + 
			"	, loc.city as 'location_city'  " + 
			"	, loc.state as 'location_state'  " + 
			"	, loc.zip as 'location_zip'  " + 
			"    , p.id as 'provider_id' " + 
			"	, p.name as 'provider_name'  " + 
			"	, p.npi as 'provider_npi' " + 
			"	, p.type as 'provider_type' " + 
			"	, s.launched_on 'schedule_launch_date' " + 
			"	, f.id as 'facility_id'  " + 
			"	, s.service_id as 'service_id'  	" + 
			"   , substring(serv.type, 10, length(serv.type) - 8) as 'service_type' " +
			"	from schedules s  " + 
			"	left join providers p on s.provider_id = p.id" + 
			"	left join locations loc on s.location_id = loc.id" + 
			"	left join facilities f on s.facility_id = f.id " + 
			"	left join regions r on f.region_id = r.id " + 
			"   left join services serv on s.service_id = serv.id " +
			"	where f.health_system_id = ?  	 " + 
			"	and f.deleted_at is null" + 
			"	and loc.deleted_at is null " + 
			"	and p.deleted_at is null" + 
//			"   and s.id = 773 " +
			"	order by s.id";
	
	public static final String appointmentSql =
			"select at.schedule_id as 'schedule_id', " +
	        "   att.name as 'appointment_type', " +
			"   att.id as 'appointment_type_template_id' " + 
			"	from appointment_types at " + 
			"	, appointment_type_templates att " + 
			"	where at.appointment_type_template_id = att.id " + 
			"	and att.health_system_id = ? " + 
			"	order by schedule_id ";
	
	public static final String providerIntegrationSql = 
			"select ik.adapter as 'integration'" +
			", p.id as 'id' " + 
			", ik.active 'integration_active' " +
			"from id_maps im " + 
			", providers p " + 
			", integration_keys ik " + 
			"where im.local_mappable_type = 'Provider' " + 
			"and im.id_mappable_type = 'IntegrationKey' " + 
			"and im.local_mappable_id = p.id " + 
			"and p.health_system_id = ? " + 
			"and im.id_mappable_id = ik.id " + 
			"and p.deleted_at is null ";
	
	
	public static final String screeningQuestionsSql = 
			" select facility_id " + 
			", region_id " + 
			", iqs.key " + 
			", iqs.value " + 
			", service_id " + 
			", appointment_type_template_id " + 
			"from iq_settings iqs " + 
			"where iqs.value in ( " + 
			"      select permalink " + 
			"      from screening_questions " + 
			"      where health_system_id = ?) " +
			" and iqs.key = 'visit.screening_questions_group' ";
	
	
	public static final String deactivaionInfoSql =  
			"select le.remote_addr, DATE_FORMAT(le.created_at, '%Y-%m-%d') as created_at, staff.name, le.description " + 
			"from log_events le " + 
			"left join iq_staffs staff on staff.id = le.iq_staff_id " + 
			"where le.health_system_id  = ? " + 
			"and le.facility_id = ? " +  
			"and le.event = 'schedule:toggle' " +
			"and le.description like \"";
	
	public static final String scheduleSummarysql = "select dd.date_iso, dss.title, dss.active " + 
			"from fact_schedule_summary dss" + 
			", dim_date dd " + 
			"where dss.dim_schedule_id = ? " + 
			"and dss.dim_date_id = dd.id " + 
			"order by date_iso desc ";

	public static final String healthSystemSQL =  
			"select  id  as 'health_system_id',  " + 
			"name as 'health_system_name'  "  +
			"from health_systems   " + 
			"where deleted_at is null " +
			"and id in (72, 97) " + //29, 83, 97, 72) " +
			"order by id ";
	
	public static final String dimDateSQL = 
			"select id as 'dim_date_id', year, month, day, date_iso, DATE_ADD(str_to_date(date_iso, '%Y-%m-%d'), INTERVAL 7 day) as 'dateISO_plus_7' " + 
			"from dim_date " + 
			"where date_iso >= ? " + 
			"and date_iso < curdate() " + 
			"and day_of_week = 0 ";
	
	public static final String factVisitSQL =
			"select count(*) as 'visit_count' " + 
			" from fact_visit " + 
			" where health_system_id = ? " +  
			" and utc_created_at between ? and ? ";
			
	public static final String rollupScheduleWeeklySQL = 
			"select f.health_system_id,  " + 
			 "f.health_system_name,  " + 
			 "count(distinct fss.id) as 'schedule_count',  " + 
			 "count(distinct f.id) as 'facility_count', " + 
			 "count( distinct region_id) as 'region_count', " + 
			 "count( distinct dim_provider_id) as 'provider_count' " + 
			 "from fact_schedule_summary fss,  " + 
			 "dim_facility f " + 
			 "where (fss.dim_schedule_id, fss.dim_date_id) in ( " + 
			 "			select dim_schedule_id,  max(dim_date_id) " + 
			 "			from fact_schedule_summary  " + 
			 "			where dim_date_id <= ?  " + 
			 "			group by dim_schedule_id ) " + 
			 "and fss.active = 1 " + 
			 "and fss.launched_on is not null " + 
			 "and fss.dim_facility_id = f.id " + 
			 "group by f.health_system_name, f.health_system_id " + 
			 "order by 1";
}
