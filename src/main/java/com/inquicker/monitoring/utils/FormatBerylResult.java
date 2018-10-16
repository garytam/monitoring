package com.inquicker.monitoring.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.inquicker.monitoring.vo.BerylIntegrationJobStatusVO;
import com.inquicker.monitoring.vo.BerylIntegrationResultVO;

public class FormatBerylResult {
	

	
	public String formatToHtml(BerylIntegrationResultVO resultVO){
				
		String result = "<table><tr><td style=\"border: 0px;\">Start:</td><td style=\"border: 0px;\">" + resultVO.getStartDateTime() + "</td></tr>\n" + 
						"    <tr><td style=\"border: 0px;\">End:</td><td style=\"border: 0px;\">" + resultVO.getEndDateTime() + "</td></tr></table>\n";
		
		result = result + "<table><tr><th>Name</th><th>Status</th><th>Inserted</th><th>Updated</th><th>Deleted</th><th>Invalid</th></tr>";
		
		
		for (BerylIntegrationJobStatusVO statusVO : resultVO.getJobStatus()) { 
			result = result + ("<tr>" + 
										"<td>" + statusVO.getDescription() + "</td>" + 
										"<td>" + statusVO.getStatus() + "</td>" + 
										"<td>" + statusVO.getInsertCount() + "</td>" + 
										"<td>" + statusVO.getUpdateCount() + "</td>" + 
										"<td>" + statusVO.getDeleteCount() + "</td>" + 
										"<td>" + statusVO.getInvalidCount() + "</td></tr>" 
										);
		}
		
		
		
		
		
		result = result + "</table>";
		return result;
		
		
	}
//	public List<String> formatResult(BerylIntegrationResultVO resultVO){
//		List<String> result = new ArrayList<String>();
//		
//		for (BerylIntegrationJobStatusVO statusVO : resultVO.getJobStatus()) { 
//				String inserted = NumberFormat.getNumberInstance(Locale.US).format(statusVO.getInsertCount());
//				String updated = NumberFormat.getNumberInstance(Locale.US).format(statusVO.getUpdateCount());
//				String deleted = NumberFormat.getNumberInstance(Locale.US).format(statusVO.getDeleteCount());
//				String invalid = NumberFormat.getNumberInstance(Locale.US).format(statusVO.getInvalidCount());
//				
//				String desc = statusVO.getDescription();
//				int idx = desc.indexOf("sync ");
//				if (idx == 0) {
//					desc = desc.substring(5, desc.length());
//				}   
//				
//				int padInt = 30 - desc.length(); 
//				
//				String line = String.format("%1$-30s", desc).replace(' ',  '-') + "|";
//				
//				result.add(line);
//				 
//			
//		}
//		
//		for (String line : result) {
//			System.out.println(line);
//		}
//		
//		
//		return result;
//	}
}
