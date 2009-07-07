package com.webrender.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectTest extends Thread {
	private final static Log log = LogFactory.getLog(ConnectTest.class);
	private NodeMachine nodeMachine = null;
	public ConnectTest(NodeMachine nodeMachine)
	{
		this.nodeMachine =nodeMachine; 
	}
	
	public void run()
	{
		if (nodeMachine.testConnect() && nodeMachine.isBusy()==false && nodeMachine.isPause()==false && NodeMachineManager.idleMachines.contains(nodeMachine)==false)
		{
			NodeMachineManager.idleMachines.add(nodeMachine);
		}
		log.info("IdleMachines Size: "+NodeMachineManager.idleMachines.size());
	}

}
