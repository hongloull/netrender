package com.webrender.logic;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.Status;
import com.webrender.dao.StatusDAO;
import com.webrender.remote.NodeMachineManager;
import com.webrender.tool.NameMap;
public class CalcOneToMany implements CalcCommands {
	private static final Log LOG = LogFactory.getLog(CalcOneToMany.class);
	
	public int  calc(com.webrender.dao.Quest quest){
		
		LOG.debug("calcOneToMany questId :"+quest.getQuestId());
		Set<Integer> nodeMachinesIds = NodeMachineManager.getInstance().getNodeMachines();
		NodeDAO nodeDAO = new NodeDAO();
		nodeMachinesIds.retainAll(nodeDAO.getNodeGroupIds(quest.getNodegroup()));
		Iterator ite_NodeIds = nodeMachinesIds.iterator();
		CommandDAO commandDAO = new CommandDAO();
		QuestDAO questDAO = new QuestDAO();
		questDAO.delAllCommands(quest);
		
		Command command = new Command();
		StatusDAO statusDAO = new StatusDAO();
		Status status = statusDAO.findById(70);
		Node node = null;
		while(ite_NodeIds.hasNext()){
			int nodeId = (Integer) ite_NodeIds.next();
			if ( NodeMachineManager.getInstance().getNodeMachine(nodeId).isConnect() ){
				node = nodeDAO.findById(nodeId);
				command = new Command(quest);
				command.setNode(node);
				command.setType(NameMap.ONETOMANY);
				command.setStatus(status);
				commandDAO.save(command);
			}
		}
		return CalcCommands.SUCCESS;
	}
}
