package com.webrender.axis.beanxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.StatusDAO;

public final class CommandmodelargUtils {
	private   static final Log LOG = LogFactory.getLog(CommandmodelargUtils.class);
	public static Element bean2xml(Commandmodelarg arg)
	{
		LOG.debug("bean2xml(Commandmodelarg argId "+arg.getCommandModelArgId()+" )");
		Element root  = new Element("Commandmodelarg");
		root.addAttribute("commandModelArgId", arg.getCommandModelArgId().toString());
		root.addAttribute("argName",arg.getArgName());
		root.addAttribute("type", (arg.getType()!=null)?arg.getType().toString():"0");
		root.addAttribute("argInstruction", arg.getArgInstruction());
		LOG.debug("bean2xml(Commandmodelarg argId "+arg.getCommandModelArgId()+" ) success");
		return root;
	}
//	public static Element bean2xml_Status(Commandmodelarg arg)
//	{
//		Element root = bean2xml(arg);
//		root.addAttribute("statusId",(arg.getStatus()!=null) ?arg.getStatus().getStatusId().toString() :"60");
//		return root;
//	}
	public static  Commandmodelarg xml2bean(Element element,boolean newFlag){
		LOG.debug("xml2bean");
		Commandmodelarg instance =null;
		CommandmodelargDAO dao = new CommandmodelargDAO();
		String commandModelArgId = null;
		// 新模板标志为false时，表示新建模板，其中的commandModelArg都为新建参数，不读取Id
		if(newFlag==false){
			commandModelArgId = element.getAttributeValue("commandModelArgId"); 
			LOG.debug("xml2bean old commandModelArgId:"+commandModelArgId);
		}
			
		
		String argName = element.getAttributeValue("argName");
		String argInstruction = element.getAttributeValue("argInstruction");
		String type = element.getAttributeValue("type");
		String statusId = element.getAttributeValue("statusId");
		if( commandModelArgId != null){
			instance = dao.findById(Integer.parseInt(commandModelArgId));
		}
		if(instance==null){
			instance = new Commandmodelarg();
			LOG.info("xml2bean new commandModelArgName:"+argName+" instrction:"+argInstruction);
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
		LOG.debug("xml2bean success");
		return instance;
	}
}
