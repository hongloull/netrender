package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;

public final class ExecutelogUtils {
	private   static final Log LOG = LogFactory.getLog(ExecutelogUtils.class);
	public static Element bean2xml(Executelog exeLog)
	{
		LOG.debug("bean2xml executeLogId: "+ exeLog.getExecuteLogId());
		Element root = new Element("Executelog");
		root.addAttribute("executeLogId",exeLog.getExecuteLogId().toString());
		CDATA note = new CDATA(exeLog.getNote()+"");
		root.addContent(note);
		SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logTime = dateFormat.format(exeLog.getLogTime());
		root.addAttribute("logTime", logTime);
		root.addAttribute("nodeIp", exeLog.getNode().getNodeIp());
		root.addAttribute("status",exeLog.getStatus().getValue());
		LOG.debug("bean2xml success executeLogId: "+ exeLog.getExecuteLogId());
		return root;
	}
	
	private static Element bean2xml2 (Executelog exeLog){
		LOG.debug("bean2xml For event exeLogId: "+exeLog.getExecuteLogId() );
		Element root = new Element("Event");
		SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logTime = dateFormat.format(exeLog.getLogTime());
		root.addAttribute("time", logTime);
		root.addAttribute("message",exeLog.getNote()+"");
		root.addAttribute("user","Service");
		root.addAttribute("ip", exeLog.getNode().getNodeIp()+"");
		root.addAttribute("machine",exeLog.getNode().getNodeName());
		root.addAttribute("type",exeLog.getStatus().getValue());
		LOG.debug("bean2xml For event success exeLogId: "+exeLog.getExecuteLogId() );
		return root;
	}
	public static String bean2XMLString(Executelog exeLog)
	{
		Element element = bean2xml2(exeLog);
		Document doc = new Document(element);
		LOG.debug("bean2XMLString success");
		return XMLOut.outputToString(doc);
		
	}
//	public static String exeLog2xml()
//	{
//		ExecutelogDAO executeLogDAO = new ExecutelogDAO();
//		List<Executelog> list_ExeLogs = executeLogDAO.findAll();
//		for(int i = list_ExeLogs.size()-1 ;i>=0;i--)
//		{
//			if(list_ExeLogs.get(i).getStatus().getStatusId()>=90){
//// 	  	  		root.addContent( bean2xml(list_ExeLogs.get(i)) );
//				System.out.println( bean2XMLString(list_ExeLogs.get(i)) );
//			}
//		}
//		return null;
////		return XMLOut.outputToString(doc);
//	}
}
