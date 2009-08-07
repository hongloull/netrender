package com.webrender.axis.beanxml;

import org.jdom.Element;

import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;

public final class QuestargUtils {
	public static Element bean2xml(Questarg questarg)
	{
		Element root = new Element("Questarg");
		root.addAttribute("questArgId", questarg.getQuestArgId().toString());
		root.addAttribute("commandModelArgId",questarg.getCommandmodelarg().getCommandModelArgId().toString() );
		if (questarg.getValue()!=null) root.addAttribute("value", questarg.getValue());
		root.addAttribute("type",questarg.getCommandmodelarg().getType()+"");
		return root;
	}

	public static Questarg xml2bean(Element element) {
		String questArgId = element.getAttributeValue("questArgId");
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
		return questarg;
	}
}
