package com.webrender.axis.beanxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.Status;
import com.webrender.dao.StatusDAO;

public final class CommandmodelargUtils {
	private   static final Log LOG = LogFactory.getLog(CommandmodelargUtils.class);
	private  CommandmodelargDAO dao = new CommandmodelargDAO();
	public  Element bean2xml(Commandmodelarg arg)
	{
		LOG.debug("bean2xml(Commandmodelarg argId "+arg.getCommandModelArgId()+" )");
		Element root  = new Element("Commandmodelarg");
		root.addAttribute("commandModelArgId", arg.getCommandModelArgId().toString());
		root.addAttribute("argName",arg.getArgName());
		root.addAttribute("type", (arg.getType()!=null)?arg.getType().toString():"0");
		root.addAttribute("order", (arg.getOrder()!=null)?arg.getOrder().toString():"100");
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
	
	/***
	 * @param cMId 模板ID   null说明是新模板
	 * @throws Exception 
	 */
	public  Commandmodelarg xml2bean(Element element,Integer cMId) throws Exception{
		LOG.debug("xml2bean");
		Commandmodelarg instance =null;
		
		
//		// 新模板标志为false时，表示老模板，其中的commandModelArg都为老参数，读取Id
//		if(cMId==null){
//			commandModelArgId = element.getAttributeValue("commandModelArgId"); 
//			LOG.debug("xml2bean old commandModelArgId:"+commandModelArgId);
//		}
		
		String argName = element.getAttributeValue("argName");
		String argInstruction = element.getAttributeValue("argInstruction");
		String value = element.getAttributeValue("value");
		String type = element.getAttributeValue("type");
		String order = element.getAttributeValue("order");
		String statusId = element.getAttributeValue("statusId");
		
		if(argInstruction==null){
			LOG.error("commandmodelarg lack argInstruction; argName: "+argName+" value:"+value );
			return null;
		}		
		
		if(cMId != null){
			instance = dao.findByArgInstruction(argInstruction, cMId);
		}
		if(instance==null){
			instance = new Commandmodelarg();
			LOG.info("xml2bean new commandModelArgName:"+argName+" argInstruction:"+argInstruction);
		}
		
		instance.setArgInstruction(argInstruction);
		
		if(argName!=null) instance.setArgName(argName);
		else instance.setArgName("");

		if(value!=null)instance.setValue(value);
		if(type!=null)instance.setType(Short.parseShort(type));
		else{
			short short_Type = 0 ;
			instance.setType(short_Type);
		}
		if(order!=null)instance.setOrder(Integer.parseInt(order));
		Status status = null;		
		StatusDAO statusDAO = new StatusDAO();
		try{
			status = statusDAO.findById(Integer.parseInt(statusId));				
		}catch(Exception e){
			status = statusDAO.findById(60);
		}
		if(status == null){
			status = statusDAO.findById(60);
		}
		instance.setStatus(status);
		
		LOG.debug("xml2bean success");
		return instance;
	}
}
