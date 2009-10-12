package com.webrender.remote;

import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.webrender.dao.Command;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.server.Dispatcher;

public final  class  NodeMachineManager {
	private NodeMachineManager(){}
	
	private static Map<Integer,NodeMachine> machines= new Hashtable<Integer,NodeMachine>();
	
	private static Set<NodeMachine> idleMachines = Collections.synchronizedSet( new HashSet<NodeMachine>() ); 
	
	public static NodeMachine getNodeMachine(int nodeId){
		if(nodeId == 0 ) return null;
		if(machines.get(nodeId)==null){
			NodeMachine nodeMachine =new NodeMachine(nodeId);
			machines.put(nodeId,nodeMachine );
		}
		return machines.get(nodeId);
	}
	public static Set<Integer> getNodeMachines(){
		return machines.keySet();
	}
	
	public static void addNodeMachines(NodeMachine nodeMachine){
		idleMachines.add(nodeMachine);
//		Dispatcher.getInstance().exeCommands();
	}
	
	public static boolean containIdles(NodeMachine nodeMachine){
		return idleMachines.contains(nodeMachine);
	}
	public static boolean isIdleEmpty(){
		return idleMachines.isEmpty();
	}
	public static void delNodeMachines(NodeMachine nodeMachine){
		idleMachines.remove(nodeMachine);
	}
	public static Object[] getIdleArray(){
		return idleMachines.toArray();
	}
	
	public static NodeMachine canExeCommand(Command command){
		NodeDAO nodeDAO = new NodeDAO();
		Object[] nodeMachines = getIdleArray();
		int length = nodeMachines.length;
		for(int i = 0 ; i<length ; i++){
			NodeMachine tempNodeMachine = (NodeMachine) nodeMachines[i];
			Node tempNode = nodeDAO.findById(tempNodeMachine.getId());
			if( command.getQuest().getNodegroup().getNodes().contains(tempNode) ){
				// 该节点保包含在任务执行池中，可以渲染
				return tempNodeMachine;	
			}
		}
		return null;
	}
//	public static void resetIdleMachines()
//	{
//		Iterator ite_Machines = machines.values().iterator();
//		while(ite_Machines.hasNext())
//		{
//			NodeMachine nodeMachine = ((NodeMachine)ite_Machines.next() );
//			ConnectTest cTest = new ConnectTest(nodeMachine);
//			cTest.start();
////			if (nodeMachine.testConnect() && nodeMachine.isBusy()==false && nodeMachine.isPause()==false && NodeMachineManager.idleMachines.contains(nodeMachine)==false)
////			{
////				NodeMachineManager.idleMachines.add(nodeMachine);
////			}
//		}
//	}
}
