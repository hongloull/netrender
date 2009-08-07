package com.webrender.axis.beanxml;

import org.jdom.Element;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.StatusDAO;

public final class CommandmodelargUtils {
	public static Element bean2xml(Commandmodelarg arg)
	{
		Element root  = new Element("Commandmodelarg");
		root.addAttribute("commandModelArgId", arg.getCommandModelArgId().toString());
		root.addAttribute("argName",arg.getArgName());
		root.addAttribute("type", (arg.getType()!=null)?arg.getType().toString():"0");
		root.addAttribute("argInstruction", arg.getArgInstruction());
		return root;
	}
//	public static Element bean2xml_Status(Commandmodelarg arg)
//	{
//		Element root = bean2xml(arg);
//		root.addAttribute("statusId",(arg.getStatus()!=null) ?arg.getStatus().getStatusId().toString() :"60");
//		return root;
//	}
	public static  Commandmodelarg xml2bean(Element element){
		Commandmodelarg instance =null;
		CommandmodelargDAO dao = new CommandmodelargDAO();
		String commandModelArgId = element.getAttributeValue("commandModelArgId");
		String argName = element.getAttributeValue("argName");
		String argInstruction = element.getAttributeValue("argInstruction");
		String type = element.getAttributeValue("type");
		String statusId = element.getAttributeValue("statusId");
		if( commandModelArgId != null){
			instance = dao.findById(Integer.parseInt(commandModelArgId));
		}
		if(instance==null){
			instance = new Commandmodelarg();
		}
		if(argName!=null) instance.setArgName(argName);
		if(argInstruction!=null)instance.setArgInstruction(argInstruction);
		if(type!=null)instance.setType(Short.parseShort(type));
		if(statusId!=null)
		{
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(Integer.parseInt(statusId)));
		}
		else{
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(60));
		}
		return instance;
	}
}
