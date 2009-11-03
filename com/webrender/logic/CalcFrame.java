package com.webrender.logic;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.CommandargDAO;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.Quest;
import com.webrender.dao.Questarg;
import com.webrender.dao.StatusDAO;
import com.webrender.tool.NameMap;

public class CalcFrame implements CalcCommands {
	
	private BigDecimal startFrame=null,endFrame=null,byFrame=null,framePerNode=null;
	private Commandmodelarg startTag = null,endTag=null,byTag=null;
//	private Set<String> results = null;
	
	public int calc(Quest quest)
	{
		Short packetSize = quest.getPacketSize();
		Iterator questArgs = quest.getQuestargs().iterator();
		try {
			while( questArgs.hasNext() )
			{
				Questarg questArg = (Questarg)questArgs.next();
				int typeValue = questArg.getCommandmodelarg().getStatus().getStatusId();
				switch (typeValue)
				{
				case 61:	startFrame  = new BigDecimal(questArg.getValue());
							startTag = questArg.getCommandmodelarg();
							break;
				case 62:    endFrame   = new BigDecimal(questArg.getValue());
							endTag  = questArg.getCommandmodelarg();
							break;
				case 63:    byFrame   = new BigDecimal(questArg.getValue());
							byTag = questArg.getCommandmodelarg();
							break;
				}
			}
			if(packetSize==null) packetSize = 1;
			framePerNode = new BigDecimal(packetSize.toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return CalcCommands.NumberFormatException;
		}
		
	
		CommandDAO commandDAO = new CommandDAO();
		CommandargDAO commandargDAO = new CommandargDAO();
		StatusDAO statusDAO = new StatusDAO();
		Commandarg commandArg =null;
		Command command = null;
		
		if(framePerNode==null){
			return CalcCommands.NEEDARGS;
		}
		
		
		
		if (startFrame==null || endFrame==null || byFrame==null )
		{
			command = new Command(quest);
			command.setType(NameMap.GETFRAME);
			command.setStatus(statusDAO.findById(70));
			commandDAO.save(command);
			return CalcCommands.LACKFRAME;
		}
		else if( (startFrame.compareTo(endFrame)==1) )
		{
			return CalcCommands.NEEDARGS;
		}
		BigDecimal distance = byFrame.multiply(framePerNode);

		BigDecimal currentStartFrame = startFrame;
		BigDecimal currentEndFrame = startFrame.add(distance).subtract(byFrame);
		boolean endFlag = false;
		while(true)
		{
			if (currentEndFrame.compareTo(endFrame)>=0)
			{
				currentEndFrame=endFrame ;
				endFlag = true;
			}
		//	results.add(" "+startTag+" "+currentStartFrame+" "+endTag+" "+currentEndFrame+" "+byTag+" "+byFrame+" ");
			
			command = new Command(quest);
			command.setStatus(statusDAO.findById(70));
			commandDAO.save(command);
			
			commandArg = new Commandarg();
			commandArg.setCommand(command);
			commandArg.setCommandmodelarg(startTag);
			commandArg.setValue(currentStartFrame.toString());
			commandargDAO.save(commandArg);
			
			commandArg = new Commandarg();
			commandArg.setCommand(command);
			commandArg.setCommandmodelarg(endTag);
			commandArg.setValue(currentEndFrame.toString());
			commandargDAO.save(commandArg);
			
			commandArg = new Commandarg();
			commandArg.setCommand(command);
			commandArg.setCommandmodelarg(byTag);
			commandArg.setValue(byFrame.toString());
			commandargDAO.save(commandArg);
			
			currentStartFrame = currentStartFrame.add(distance);
			currentEndFrame   = currentEndFrame.add(distance);
			
			if ( endFlag ) break;
		}
		
		return CalcCommands.SUCCESS;
	}
	
	public String getTotalFrames(Quest quest){
		Iterator questArgs = quest.getQuestargs().iterator();
		while( questArgs.hasNext() )
		{
			Questarg questArg = (Questarg)questArgs.next();
			int typeValue = questArg.getCommandmodelarg().getStatus().getStatusId();
			switch (typeValue)
			{
			case 61:	startFrame  = new BigDecimal(questArg.getValue());
//						startTag = questArg.getCommandmodelarg();
						break;
			case 62:    endFrame   = new BigDecimal(questArg.getValue());
//						endTag  = questArg.getCommandmodelarg();
						break;
			case 63:    byFrame   = new BigDecimal(questArg.getValue());
						break;
			}
		}
		if (startFrame==null || endFrame==null || byFrame==null )
		{
			return "";			
		}
		BigDecimal temp = endFrame.subtract(startFrame).divide(byFrame);
		BigDecimal totalFrames = ( temp.setScale(0, BigDecimal.ROUND_DOWN)).add(new BigDecimal("1"));
		return totalFrames.toString();
	}
}
