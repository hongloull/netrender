package com.webrender.axis.beanxml;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;

public final class ChunkDetailUtils {
	private   static final Log LOG = LogFactory.getLog(ChunkDetailUtils.class);
	public Element getElement(Command command)
	{
		LOG.debug("getCommandChunkDetail commandId:"+command.getCommandId());
		Element root = new Element("Detail");
		ExecutelogDAO executeLogDAO = new ExecutelogDAO();
		int statusId = command.getStatus().getStatusId(); 
		CommandDAO commandDAO = new CommandDAO();
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
			SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			start = dateFormat.format(startTime);
			
			endTime = endLog.getLogTime();
			end   = dateFormat.format(endTime);
			
			long between = (endTime.getTime()-startTime.getTime())/1000;
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
		root.addAttribute("status",command.getStatus().getValue());
		root.addAttribute("errorResult", errorResult);
		root.addAttribute("frames",commandDAO.getNote(command).toString());
		root.addAttribute("renderedBy", nodeName);
		if(statusId!=70){
			root.addAttribute("nodeIp",nodeIp);
			root.addAttribute("startingTime",start);			
		}
		if(statusId==72){
			root.addAttribute("endingTime", end);
			root.addAttribute("timeSpan",timeSpan);				
		}
		LOG.debug("getCommandChunkDetailFinish commandId:"+command.getCommandId());
		return root;
		
		
	}
}
