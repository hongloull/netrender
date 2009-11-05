package com.webrender.logic;

import java.util.Iterator;

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
	public int calc(Quest quest) {
		LOG.debug("calc manytomany commands");
		int packetSize = quest.getPacketSize();
		Iterator ite_Questargs = quest.getQuestargs().iterator();
		CommandDAO commandDAO  = new CommandDAO();
		CommandargDAO commandargDAO = new CommandargDAO();
		Command command = null;
		Commandarg commandArg = null;
		Status status = (new StatusDAO()).findById(70);
		int i = 1;
		while(ite_Questargs.hasNext()){
			if(i==1){
				command = new Command(quest);
				command.setType(NameMap.MANYTOMANY);
				command.setStatus(status);
				commandDAO.save(command);
			}
			Questarg arg = (Questarg) ite_Questargs.next();
			commandArg = new Commandarg(command,arg.getCommandmodelarg(),arg.getValue()+"");
			commandargDAO.save(commandArg);
			command.getCommandargs().add(commandArg);
			i++;
			if(i==packetSize || ite_Questargs.hasNext()==false){				
				i=1;
			}
		}
		return CalcCommands.SUCCESS;
	}

}
