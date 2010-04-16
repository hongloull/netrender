package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.dao.Operatelog;


public final class OperatelogUtils {
	private static final Log LOG = LogFactory.getLog(OperatelogUtils.class);
	public Element bean2xml(Operatelog opeLog){
		LOG.debug("bean2xml For Event operateLogId:"+ opeLog.getOperateLogId());
		Element root = new Element("Event");
		SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logTime = dateFormat.format(opeLog.getLogTime());
		root.addAttribute("time", logTime);
		root.addAttribute("message",opeLog.getOperateInformation()+"");
		root.addAttribute("user",opeLog.getReguser().getRegName());
		root.addAttribute("ip","");
		root.addAttribute("machine","");
		String type = "";
		switch (opeLog.getType()){
		case 1: type="LOGIN"; break;
		case 3: type="MODIFY";break;
		case 4: type="ADD";   break;
		case 5: type="DELETE";break;
		case 7: type="ERROR"; break;
		}
		
		root.addAttribute("type",type);
		LOG.debug("bean2xml For Event success operateLogId:"+ opeLog.getOperateLogId());
		return root;
	}
	public String bean2XMLString(Operatelog opeLog){
		Element element = bean2xml(opeLog);
		Document doc = new Document(element);
		LOG.debug("bean2XMLString success");
		return (new XMLOut()).outputToString(doc);
	}
}
