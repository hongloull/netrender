package com.webrender.axis.beanxml;

import java.util.List;

import org.jdom.Element;

import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;

public class CommandmodelUtils {
	public static Element bean2xml(Commandmodel model)
	{
		Element root  = new Element("Commandmodel");
		root.addAttribute("commandModelId", model.getCommandModelId().toString());
		root.addAttribute("commandModelName",model.getCommandModelName());
		return root;
	}
	
	public static Commandmodel xml2bean(Element element)
	{
		Commandmodel commandModel = null;
		String commandModelId = element.getAttributeValue("commandModelId");
		String commandModelName = element.getAttributeValue("commandModelName");
		
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
			List<Commandmodel> lis_CM  = dao.findByCommandModelName(commandModelName);
			if ( lis_CM.isEmpty() )	commandModel = new Commandmodel();
			else commandModel = lis_CM.get(0);
			
		}
		
		if (commandModelName!=null) commandModel.setCommandModelName(commandModelName);
				
		return commandModel;
	}
}
