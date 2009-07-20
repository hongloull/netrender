package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;

public class ExecutelogUtils {
	public static Element bean2xml(Executelog exeLog)
	{
		Element root = new Element("Executelog");
		root.addAttribute("executeLogId",exeLog.getExecuteLogId().toString());
		CDATA note = new CDATA(exeLog.getNote());
		root.addContent(note);
		SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logTime = dateFormat.format(exeLog.getLogTime());
		root.addAttribute("logTime", logTime);
		root.addAttribute("nodeIp", exeLog.getNode().getNodeIp());
		root.addAttribute("status",exeLog.getStatus().getValue());
		return root;
	}
	public static String bean2xmlString(Executelog exeLog)
	{
		Element element = bean2xml(exeLog);
		Document doc = new Document(element);
		return XMLOut.outputToString(doc);
	}
	public static String exeLog2xml()
	{
		Element root = new Element("Executelogs");
		Document doc =new Document(root);
		ExecutelogDAO executeLogDAO = new ExecutelogDAO();
		List<Executelog> list_ExeLogs = executeLogDAO.findAll();
		for(int i = list_ExeLogs.size()-1 ;i>=0;i--)
		{
			if(list_ExeLogs.get(i).getStatus().getStatusId()>=90){
				root.addContent( bean2xml(list_ExeLogs.get(i)) );				
			}
		}
		return XMLOut.outputToString(doc);
	}
}
