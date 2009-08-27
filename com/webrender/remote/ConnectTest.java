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
		while(true){
			try {
				nodeMachine.testStatus();
				Thread.sleep(20000);
			} catch (InterruptedException e) {
			}
		}
	}

}
