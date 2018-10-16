package com.inquicker.monitoring.utils;

import org.apache.log4j.Logger;
 
 

public class LoggerUtil {
 
	private static Logger logger = null;
	
	private LoggerUtil(Class logClass) { 
		logger = Logger.getLogger(logClass);
	}
	
	public static LoggerUtil getLogger(Class logClass) {
		return new LoggerUtil(logClass);
	}
	
	public void debug(String msg) {
		if (logger != null)
			logger.debug(msg);
	}
	
	public void info(String msg) {
		if (logger != null)
			logger.info(msg);
	}
	
	public void error(String msg) {
		if (logger != null)
			logger.error(msg);
	}
	
	public void warning(String msg) {
		if (logger != null)
			logger.error(msg);
	}
}
