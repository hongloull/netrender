package com.webrender.remote;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final  class  NodeMachineManager {
	private NodeMachineManager(){}
	
	private static Map<Integer,NodeMachine> machines= new Hashtable<Integer,NodeMachine>();
	
	public static Set<NodeMachine> idleMachines = Collections.synchronizedSet( new HashSet<NodeMachine>() ); 
	
	public static NodeMachine getNodeMachine(int nodeId){
		if(machines.get(nodeId)==null){
			NodeMachine nodeMachine =new NodeMachine(nodeId);
			machines.put(nodeId,nodeMachine );
		}
		return machines.get(nodeId);
	}
	public static Set<Integer> getNodeMachines(){
		return machines.keySet();
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
