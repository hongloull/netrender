package com.webrender.axis.beanxml;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.jdom.Element;

import com.webrender.dao.Command;
import com.webrender.dao.Commandarg;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;

public class ChunkDetailUtils {
	public static Element getElement(Command command)
	{
		Element root = new Element("Detail");
		ExecutelogDAO executeLogDAO = new ExecutelogDAO();
		int statusId = command.getStatus().getStatusId(); 
		if ( statusId ==72 || statusId ==71)
		{
			Date startTime = null;
			Date endTime = null;
			String start = "";
			String end = "";
			String timeSpan = "";
			String nodeName = "";
			String nodeIp = "";
			String errorResult ="";
			try {
				Executelog startLog = executeLogDAO.findStartLog(command);
				Executelog endLog = executeLogDAO.findEndLog(command);
				startTime = startLog.getLogTime();
				endTime = endLog.getLogTime();
				long between = (endTime.getTime()-startTime.getTime())/1000;
				SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				start = dateFormat.format(startTime);
				end   = dateFormat.format(endTime);
				long day1=between/(24*3600);
				long hour1=between%(24*3600)/3600;
				long minute1=between%3600/60;
				long second1=between%60;
				timeSpan = ""+day1+"days,"+hour1+"hours,"+minute1+"mins,"+second1+"secs";
			} catch (NullPointerException e) {
			}
			Iterator ite_Commandargs = command.getCommandargs().iterator();
			StringBuilder frames = new StringBuilder("");
			while(ite_Commandargs.hasNext())
			{
				Commandarg commandArg = (Commandarg)ite_Commandargs.next();
				
				frames.append(commandArg.getCommandmodelarg().getArgName()).append(" ").append(commandArg.getValue()).append(" ");
			}
			try{
				nodeIp = command.getNode().getNodeIp();
				nodeName = command.getNode().getNodeName();
				
			}catch (NullPointerException e) {
			}
			try{
				ExecutelogDAO exeLogDAO = new ExecutelogDAO();
				if ( exeLogDAO.hasError(command)){
					errorResult="error";
				}
				
			}catch (Exception e) {
			}
			root.addAttribute("errorResult", errorResult);
			root.addAttribute("frames",frames.toString());
			root.addAttribute("renderedBy", nodeName);
			root.addAttribute("nodeIp",nodeIp);
			root.addAttribute("startingTime",start);
			root.addAttribute("endingTime", end);
			root.addAttribute("timeSpan",timeSpan);
			return root;
		}
		return null;
	}
}