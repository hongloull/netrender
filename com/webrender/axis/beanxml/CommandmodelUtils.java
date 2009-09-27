package com.webrender.axis.beanxml;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;

public final class CommandmodelUtils {
	private   static final Log LOG = LogFactory.getLog(CommandmodelUtils.class);
	public static Element bean2xml(Commandmodel model)
	{
		LOG.debug("bean2xml commandModelId:"+ model.getCommandModelId());
		Element root  = new Element("Commandmodel");
		root.addAttribute("commandModelId", model.getCommandModelId().toString());
		root.addAttribute("commandModelName",model.getCommandModelName());
		LOG.debug("bean2xml success");
		return root;
	}
	
	public static Commandmodel xml2bean(Element element)
	{
		LOG.debug("xml2bean");
		Commandmodel commandModel = null;
		String commandModelId = element.getAttributeValue("commandModelId");
		String commandModelName = element.getAttributeValue("commandModelName");
		if (commandModelName == null){
			LOG.error("xml2bean error : commandModelName null Error");
			return null;
		}
		CommandmodelDAO dao = new CommandmodelDAO();
//		if (commandModelId != null )
//		{
//			commandModel = dao.findById(Integer.parseInt(commandModelId));
//			if(commandModel!=null)
//			{
//				if(! commandModel.getCommandModelName().equalsIgnoreCase(commandModelName) )// 查到的model名称 与xml不等
//				{
//					commandModel.setCommandModelName(commandModelName);
//				}				
//			}
//			else{
//				commandModel = new Commandmodel();
//				commandModel.setCommandModelId(Integer.parseInt(commandModelId));
//				commandModel.setCommandModelName(commandModelName);
//			}
//		}
//		else if (commandModelName!=null)
//		{
//			List<Commandmodel> lis_CM  = dao.findByCommandModelName(commandModelName);
//			if ( lis_CM.isEmpty() ){
//				commandModel = new Commandmodel();
//				commandModel.setCommandModelName(commandModelName);
//			}
//			// hasExist
//			else
//			{
//				commandModel = (Commandmodel)lis_CM.get(0);
//			}
//		}
//		else {
//			return null;
//		}
		if (commandModelId != null ){
			commandModel = dao.findById(Integer.parseInt(commandModelId));
		}
		if (commandModel==null){
			commandModel  = dao.findByCommandModelName(commandModelName);
			if (commandModel==null ){
				LOG.info("New Model Name: "+commandModelName);
				commandModel = new Commandmodel();
			}
			
		}
		
		commandModel.setCommandModelName(commandModelName);
		LOG.debug("xml2bean success");
		return commandModel;
	}
}
