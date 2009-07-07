package com.webrender.remote;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NodeMachineManager {
	private NodeMachineManager(){}
	
	private static Map<String,NodeMachine> machines= new Hashtable<String,NodeMachine>();
	
	public static Set<NodeMachine> idleMachines = Collections.synchronizedSet( new HashSet<NodeMachine>() ); 
	
	public static NodeMachine getNodeMachine(String ip){
		if(machines.get(ip)==null){
			NodeMachine nodeMachine =new NodeMachine(ip);
			machines.put(ip,nodeMachine );
		}
		return machines.get(ip);
	}
	
	public static void resetIdleMachines()
	{
		Iterator ite_Machines = machines.values().iterator();
		while(ite_Machines.hasNext())
		{
			NodeMachine nodeMachine = ((NodeMachine)ite_Machines.next() );
			ConnectTest cTest = new ConnectTest(nodeMachine);
			cTest.start();
//			if (nodeMachine.testConnect() && nodeMachine.isBusy()==false && nodeMachine.isPause()==false && NodeMachineManager.idleMachines.contains(nodeMachine)==false)
//			{
//				NodeMachineManager.idleMachines.add(nodeMachine);
//			}
		}
	}
}
