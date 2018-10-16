package com.inquicker.monitoring.main;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.inquicker.monitoring.dao.BerylIntegrationDAO;
import com.inquicker.monitoring.utils.BerylIntegrationSlack;
import com.inquicker.monitoring.utils.DatabaseUtils;
import com.inquicker.monitoring.utils.FormatBerylResult;
import com.inquicker.monitoring.vo.BerylIntegrationResultVO;

public class MonitorBerylIntegration extends Monitor{

	private String runDate;
	
	
	private String getRunDate() {
		String pattern = "yyyyMMdd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date = simpleDateFormat.format(new Date());
		return date;
	}
	
	public void monitorBerylIntegration() {
		runDate = getRunDate();
		Connection conn  = DatabaseUtils.makeJDBCConnectionBerylStaging(IQMonitor.props);
		
		BerylIntegrationDAO dao = new BerylIntegrationDAO();
		
		BerylIntegrationResultVO result = dao.getBeryIntegrationResult(conn, runDate);
		
		FormatBerylResult formatter = new FormatBerylResult();
		String formattedResult = formatter.formatToHtml(result);
		
		formattedResult = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" + 
				"        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
				"<head>\n" + 
				"    <title>Flying Saucer: 2 Column (Menu and Content) Fluid Layout</title>\n" + 
				"    <style type=\"text/css\" media=\"screen\">\n" + 
				"        html, body {\n" + 
				"            padding: 0px;\n" + 
				"            margin: 0px;\n" + 
				"        }\n" + 
				"        body {\n" + 
				"            background-color: #F5F5F5;\n" + 
				"            font-size: 12px;\n" + 
				"            font-family: Verdana, Arial, SunSans-Regular, Sans-Serif;\n" + 
				"            color: black;\n" + 
				"            padding: 0px 20px;\n" + 
				"            margin: 0px;\n" + 
				"        }\n" + 
				"\n" + 
				"        #content {\n" + 
				"            float: left;\n" + 
				"            width: 75%;\n" + 
				"            background-color: #fff;\n" + 
				"            margin: 0px 0px 50px 0px;\n" + 
				"            overflow: auto;\n" + 
				"            border: 1px solid #000;\n" + 
				"        }\n" + 
				"\n" + 
				"        #menu {\n" + 
				"            float: left;\n" + 
				"            width: 25%;\n" + 
				"            background-color: #F5F5F5;\n" + 
				"            overflow: auto;\n" + 
				"            border: 1px solid #000;\n" + 
				"        }\n" + 
				"\n" + 
				"        h1 {\n" + 
				"            font-size: 11px;\n" + 
				"            text-transform: uppercase;\n" + 
				"            text-align: left;\n" + 
				"            color: #564b47;\n" + 
				"            background-color: #F0F8FF;\n" + 
				"            padding: 5px 15px;\n" + 
				"            margin: 0px;\n" + 
				"            border: 1px solid #000;               \n" + 
				"        }\n" + 
				"\n" + 
				"        h2 {\n" + 
				"            font-size: 14px;\n" + 
				"            padding-top: 10px;\n" + 
				"            color: #564b47;\n" + 
				"            background-color: transparent;\n" + 
				"            border: 1px solid #000;\n" + 
				"        }\n" + 
				"\n" + 
				"        #menu p {\n" + 
				"            font-size: 11px;\n" + 
				"            border: 1px solid #000;\n" + 
				"        }\n" + 
				"\n" + 
				"        p, h2, pre {\n" + 
				"            padding: 10px;\n" + 
				"        }\n" + 
				"        \n" + 
				"        table {\n" + 
				"          background-color: #FFF;\n" + 
				"          border-collapse: collapse;\n" + 
				"        }\n" + 
				"        \n" + 
				"        tr {\n" + 
				"          background-color: #FFF;\n" + 
				"        }\n" + 
				"        \n" + 
				"        td {\n" + 
				"          background-color: #FFF;\n" + 
				"          border: 1px solid orange;\n" + 
				"          text-align: center;\n" + 
				"          padding: 10px;\n" + 
				"        }\n" + 
				"        th {\n" + 
				"          background-color: #F9F9F9;\n" + 
				"          border: 1px solid orange;\n" + 
				"          font-weight: plain;\n" + 
				"          text-align: center;\n" + 
				"          padding: 10px;\n" + 
				"        }\n" + 
				"    </style>\n" + 
				"</head>\n" +
				"<body>\n"+
				"<h1>Beryl Integration Run Statistics</h1>" + formattedResult +
				"</body>\n" + 
				"</html>";
		BerylIntegrationSlack slacker = new BerylIntegrationSlack();
		slacker.sendSlackBerylIntegrationStatImage(formattedResult); 
	}
}
