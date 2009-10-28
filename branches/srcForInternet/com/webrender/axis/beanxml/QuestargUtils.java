package com.webrender.axis.beanxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;

public final class QuestargUtils {
	private static final Log LOG = LogFactory.getLog(QuestargUtils.class);	
	public Element bean2xml(Questarg questarg)
	{
		LOG.debug("bean2xml questArgId:"+ questarg.getQuestArgId());
		Element root = new Element("Questarg");
		root.addAttribute("questArgId", questarg.getQuestArgId().toString());
		root.addAttribute("commandModelArgId",questarg.getCommandmodelarg().getCommandModelArgId().toString() );
		if (questarg.getValue()!=null) root.addAttribute("value", questarg.getValue());
		root.addAttribute("type",questarg.getCommandmodelarg().getType()+"");
		LOG.debug("bean2xml success questArgId:"+questarg.getQuestArgId());
		return root;
	}

	public Questarg xml2bean(Element element) {
		String questArgId = element.getAttributeValue("questArgId");
		LOG.debug("xml2bean questArgId:"+questArgId);
		String commandModelArgId = element.getAttributeValue("commandModelArgId");
		String value = element.getAttributeValue("value");
		Questarg questarg = null; 
		if( questArgId!=null)
		{
			QuestargDAO dao = new QuestargDAO();
			questarg = dao.findById(Integer.parseInt(questArgId));
		}
		else
		{
			questarg = new Questarg(); 
		}
		if (value!=null)questarg.setValue(value);
		
		if(commandModelArgId != null)
		{
			CommandmodelargDAO commandmodelargDAO = new CommandmodelargDAO();
			Commandmodelarg commandmodelarg = commandmodelargDAO.findById(Integer.parseInt(commandModelArgId));
			questarg.setCommandmodelarg(commandmodelarg);
		}
		LOG.debug("xml2bean success questArgId:"+questArgId);
		return questarg;
	}
	
	public void xml2beans(Element element){
		String questArgId = element.getAttributeValue("questArgId");
		LOG.debug("xml2bean2 questArgId:"+questArgId);
		String commandModelArgId = element.getAttributeValue("commandModelArgId");
		String value = element.getAttributeValue("value");
		Questarg questarg = null; 
		if( questArgId!=null)
		{
			QuestargDAO dao = new QuestargDAO();
			questarg = dao.findById(Integer.parseInt(questArgId));
		}
		else
		{
			questarg = new Questarg(); 
		}
		if (value!=null)questarg.setValue(value);
		
		if(commandModelArgId != null)
		{
			CommandmodelargDAO commandmodelargDAO = new CommandmodelargDAO();
			Commandmodelarg commandmodelarg = commandmodelargDAO.findById(Integer.parseInt(commandModelArgId));
			questarg.setCommandmodelarg(commandmodelarg);
		}
		LOG.debug("xml2bean success questArgId:"+questArgId);
		
	}
}
