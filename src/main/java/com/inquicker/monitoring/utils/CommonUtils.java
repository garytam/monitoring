package com.inquicker.monitoring.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class CommonUtils {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
	
	/***********************************/
	/* convertToString                 */
	/***********************************/
	public static String convertToString(Object obj) {
		String result = "";
		
		if (obj instanceof Timestamp) {
			result = sdf.format(obj);
		} else {
			result = obj.toString();
		}
		
		return result;
	}
	
	public static Properties loadPropertiesInternal() throws IOException {
		Properties props = new Properties();
		InputStream resourceStream = CommonUtils.class.getClass().getResourceAsStream("/env.properties");
		if (resourceStream != null) {
			props.load(resourceStream);
		}
		return props;
	}
	
	public static Properties loadProperties() throws IOException {
		Properties prop = new Properties();
		InputStream input = null; 
		String propPath = System.getProperty( "env.properties" );
		 
		if (propPath == null) {
			return loadPropertiesInternal();
		}

		try {

			input = new FileInputStream(propPath);

			// load a properties file
			prop.load(input); 

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		
		
		
		return prop;
	}

}
