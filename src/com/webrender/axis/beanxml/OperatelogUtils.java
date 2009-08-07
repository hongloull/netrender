package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.dao.Operatelog;


public final class OperatelogUtils {
	public static Element bean2xml(Operatelog opeLog){
		Element root = new Element("Event");
		SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logTime = dateFormat.format(opeLog.getLogTime());
		root.addAttribute("time", logTime);
		root.addAttribute("message",opeLog.getOperateInformation()+"");
		root.addAttribute("user","Service");
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
		return root;
	}
	public static String bean2XMLString(Operatelog opeLog){
		Element element = bean2xml(opeLog);
		Document doc = new Document(element);
		return XMLOut.outputToString(doc);
	}
}
