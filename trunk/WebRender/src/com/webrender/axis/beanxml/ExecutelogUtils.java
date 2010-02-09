package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.dao.Executelog;

public final class ExecutelogUtils {
	private   static final Log LOG = LogFactory.getLog(ExecutelogUtils.class);
	public Element bean2xml(Executelog exeLog)
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
	public Element arg2xml(String message,String logTime,String nodeIp,String status){
		Element root = new Element("Executelog");
		CDATA note = new CDATA(message+"");
		root.addContent(note);
		root.addAttribute("logTime",logTime);
		root.addAttribute("nodeIp", nodeIp);
		root.addAttribute("status",status);
		return root;
	}
	private Element bean2xml2 (Executelog exeLog){
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
	public String bean2XMLString(Executelog exeLog)
	{
		Element element = bean2xml2(exeLog);
		Document doc = new Document(element);
		LOG.debug("bean2XMLString success");
		return (new XMLOut()).outputToString(doc);
		
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
