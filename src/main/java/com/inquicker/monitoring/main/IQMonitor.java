package com.inquicker.monitoring.main;

import java.util.Properties;

import com.inquicker.monitoring.utils.CommonUtils;

public class IQMonitor {

	public static Properties props    = null;	
	private static String applicationName    = null;
	
	// TO BUILD
	// mvn clean compile assembly:single
	// To RUN
	// copy env.properties and log4j.properties to current directory
	// java -Denv.properties=./env.properties -Dlog4j.configuration=file:"./log4j.properties" -jar SimpleDAO-0.0.1-SNAPSHOT-jar-with-dependencies.jar 

	
	public static void main(String args[]) throws Exception{
		
		props = CommonUtils.loadProperties();
		applicationName = props.getProperty("APPLICATION_TO_MONITOR");
		
		if (applicationName.equals("BERYL_INTEGRATION")) {
			MonitorBerylIntegration monitor = new MonitorBerylIntegration();
			monitor.monitorBerylIntegration();
		}
	}
}
