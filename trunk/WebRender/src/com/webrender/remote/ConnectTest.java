package com.webrender.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectTest extends Thread {
	private final static Log LOG = LogFactory.getLog(ConnectTest.class);
	private NodeMachine nodeMachine = null;
	private final static int DISCONNECTMAX = 90; // half an hour
	public ConnectTest(NodeMachine nodeMachine)
	{
		this.nodeMachine =nodeMachine; 
	}
	
	public void run()
	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		int disconnectNum = 0;
		while(true){
			try {
				if( nodeMachine.isConnect()==true ){
					nodeMachine.testStatus();
					disconnectNum = 9;
				}
				else{
					disconnectNum++;
					if(disconnectNum>=DISCONNECTMAX){
						NodeMachineManager.getInstance().deleteNodeMachine(nodeMachine.getId());	
					}
				}
//				LOG.info("sendPathConfigTonode "+nodeMachine.getId());
//				nodeMachine.sendPathCongfigToNode();
				Thread.sleep(20000);
			} catch (InterruptedException e) {
			}
		}
	}

}
