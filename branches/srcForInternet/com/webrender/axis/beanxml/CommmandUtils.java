package com.webrender.axis.beanxml;

import java.util.HashSet;
import java.util.Iterator;

import com.webrender.dao.Command;
import com.webrender.dao.Commandarg;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.Questarg;

public final class CommmandUtils {
	public String commandToXMLForExe(Command command)
	{
		org.jdom.Element root = new org.jdom.Element("Cmd");
		root.addAttribute("cmdModelName",command.getQuest().getCommandmodel().getCommandModelName());
		root.addAttribute("cmdId",command.getCommandId()+"");
		root.addAttribute("questId",command.getQuest().getQuestId()+"");
		root.addAttribute("questName",command.getQuest().getQuestName()+"");
		
		HashSet<Integer> set_ids = new HashSet<Integer>();
		
		Iterator ite_CommandArgs = command.getCommandargs().iterator();
		while(ite_CommandArgs.hasNext())
		{
			
			Commandarg commandArg = (Commandarg)ite_CommandArgs.next();
			Commandmodelarg cMDArg = commandArg.getCommandmodelarg();
			set_ids.add(cMDArg.getCommandModelArgId());
			org.jdom.Element element = null;
			if (commandArg.getCommandmodelarg().getStatus().getStatusId()==64){
				element = new org.jdom.Element("Endarg");
			}
			else element = new org.jdom.Element("Cmdarg");
//			element.addAttribute("cmdModelArgId", cMDArg.getCommandModelArgId().toString());
//			element.addAttribute("argInstruction", cMDArg.getArgInstruction());
			element.addAttribute("argName", cMDArg.getArgName());
//			element.addAttribute("type",cMDArg.getType().toString());
			element.addAttribute("value", commandArg.getValue());
//	        element.addAttribute("statusId",commandArg.getCommandmodelarg().getStatus().getStatusId().toString());
			root.addContent(element);
		}
		Iterator ite_Questargs = command.getQuest().getQuestargs().iterator();
		Questarg questArg = null;
		Commandmodelarg cMDArg = null;
		org.jdom.Element element = null;
		while(ite_Questargs.hasNext())
		{
			questArg = (Questarg)ite_Questargs.next();
			cMDArg = questArg.getCommandmodelarg();
			if ( set_ids.contains( cMDArg.getCommandModelArgId() ) )
			{
				continue;
			}
			else {
				if (questArg.getCommandmodelarg().getStatus().getStatusId()==64){
					element = new org.jdom.Element("Endarg");
				}
				else element = new org.jdom.Element("Cmdarg");
//				element.addAttribute("commandModelArgId", cMDArg.getCommandModelArgId().toString());	
//				element.addAttribute("argInstruction", cMDArg.getArgInstruction());
				element.addAttribute("argName", cMDArg.getArgName());
//				element.addAttribute("type",cMDArg.getType().toString());
				element.addAttribute("value", questArg.getValue());	
				root.addContent(element);
			}
		}
		
		Iterator ite_Commandmodelargs = (new CommandmodelargDAO()).findFinalArgs(command.getQuest().getCommandmodel()).iterator();
		while(ite_Commandmodelargs.hasNext()){			
			cMDArg = (Commandmodelarg) ite_Commandmodelargs.next();
			element = new org.jdom.Element("Cmdarg");
			element.addAttribute("argName", cMDArg.getArgName());
			element.addAttribute("value", cMDArg.getValue());	
			root.addContent(element);
		}
		
		
		
//		StringBuilder result = new StringBuilder();
//		result.append(" ").append( command.getCommand() ).append(" ");
//		QuestargDAO questArgDAO = new QuestargDAO();
//		Iterator<Questarg> constantArgs = questArgDAO.getConstantArgs(command.getQuest()).iterator();
//		while(constantArgs.hasNext())
//		{
//			Questarg arg = constantArgs.next();
//			String argName = arg.getCommandmodelarg().getArgName();
//			String argValue = arg.getValue();
//			result.append(argName).append(" ").append(argValue).append(" ");
//		}
//		root.addAttribute("content",result.toString() );
		org.jdom.Document doc = new org.jdom.Document(root);
//		System.out.println(XMLOut.outputToString(doc));
		return (new XMLOut()).outputToString(doc);
	}
}
