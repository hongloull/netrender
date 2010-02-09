package com.webrender.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectTest extends Thread {
	private final static Log LOG = LogFactory.getLog(ConnectTest.class);
	private NodeMachine nodeMachine = null;
	public ConnectTest(NodeMachine nodeMachine)
	{
		this.nodeMachine =nodeMachine; 
	}
	
	public void run()
	{
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		while(true){
			try {
				nodeMachine.testStatus();
//				LOG.info("sendPathConfigTonode "+nodeMachine.getId());
//				nodeMachine.sendPathCongfigToNode();
				Thread.sleep(20000);
			} catch (InterruptedException e) {
			}
		}
	}

}
