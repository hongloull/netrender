package com.webrender.axis.beanxml;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;

public final class CommandmodelUtils {
	private   static final Log LOG = LogFactory.getLog(CommandmodelUtils.class);
	public Element bean2xml(Commandmodel model)
	{
		LOG.debug("bean2xml commandModelId:"+ model.getCommandModelId());
		Element root  = new Element("Commandmodel");
		root.addAttribute("commandModelId", model.getCommandModelId().toString());
		root.addAttribute("commandModelName",model.getCommandModelName());
		if(model.getType()!=null) root.addAttribute("type",model.getType());
		LOG.debug("bean2xml success");
		return root;
	}
	
	public  Commandmodel xml2bean(Element element)
	{
		LOG.debug("xml2bean");
		Commandmodel commandModel = null;
		String commandModelName = element.getAttributeValue("commandModelName");
		String type = element.getAttributeValue("type");
		if (commandModelName == null){
			LOG.error("xml2bean error : commandModelName null Error");
			return null;
		}
		
		CommandmodelDAO dao = new CommandmodelDAO();

//		if (commandModelId != null ){
//			commandModel = dao.findById(Integer.parseInt(commandModelId));
//		}
		if (commandModel==null){
			commandModel  = dao.findByCommandModelName(commandModelName);
			if (commandModel==null ){
				LOG.info("New Model Name: "+commandModelName);
				commandModel = new Commandmodel();
			}
			
		}
		if( type!=null){
			commandModel.setType(type);
		}
		commandModel.setCommandModelName(commandModelName);
		LOG.debug("xml2bean success");
		return commandModel;
	}
}
