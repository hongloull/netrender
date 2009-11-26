package com.webrender.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.CommandargDAO;
import com.webrender.dao.Quest;
import com.webrender.dao.Questarg;
import com.webrender.dao.Status;
import com.webrender.dao.StatusDAO;
import com.webrender.tool.NameMap;

public class CalcManyToMany implements CalcCommands {
	private static final Log LOG = LogFactory.getLog(CalcManyToMany.class);
	private int totalSize = 0;
	public int calc(Quest quest) {		
		LOG.debug("calc manytomany commands");
		int currentmodelArgId =0 ;
		int packetSize = quest.getPacketSize();
		int currentPacketPosition =0;
		
		int currentCommandPosition =0;
		Set<Questarg> set_Questargs = quest.getQuestargs();
		CommandDAO commandDAO  = new CommandDAO();
		CommandargDAO commandargDAO = new CommandargDAO();
		Command command = null;
		Commandarg commandArg = null;
		List<Command> list_Commands = null;
		Status status = (new StatusDAO()).findById(70);
		int count = 0;
		for(Questarg questArg : set_Questargs){			
			if (currentmodelArgId != questArg.getCommandmodelarg().getCommandModelArgId()){
				count=0;
				currentmodelArgId = questArg.getCommandmodelarg().getCommandModelArgId();
				if(list_Commands==null){
					list_Commands = new ArrayList<Command>();
				}else{
					currentCommandPosition =0;
					currentPacketPosition = 0;
				}
			}
			count++;
			if( currentCommandPosition < list_Commands.size()){  // Don't need new Command
				command=list_Commands.get(currentCommandPosition);	
			}
			else{ // exact Command
				command = new Command(quest);
				command.setType(NameMap.MANYTOMANY);
				command.setStatus(status);
				commandDAO.save(command);
				list_Commands.add(command);
			}
			commandArg = new Commandarg(command,questArg.getCommandmodelarg(),questArg.getValue()+"");
			commandargDAO.save(commandArg);
			command.getCommandargs().add(commandArg);
			
			currentPacketPosition++;	
			if(currentPacketPosition==packetSize){
				currentPacketPosition = 0;
				currentCommandPosition++;
			}
			if(count>this.totalSize){
				totalSize = count;
			}
		}
		return CalcCommands.SUCCESS;
	}
	public int getTotalSize() {
		return totalSize;
	}

}
