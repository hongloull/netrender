package com.webrender.server;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;

import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;


public class NodeThreadServer extends Thread {
	
	private static NodeThreadServer instance = new NodeThreadServer();
	private static boolean threadStop = false;
	private static final Log log = LogFactory.getLog(NodeThreadServer.class);
	private NodeThreadServer()
	{
	}
	
	private final static NodeThreadServer getInstance()
	{
		return instance;
	}
	
	public void run()
	{
		log.debug("NodeThreadServer Run");
		
		NodeDAO nodeDAO = new NodeDAO();
		List list = nodeDAO.findAll();
		int size = list.size();
		for(int i=0 ;i<size;i++)
		{
			Node node = (Node)list.get(i);
			//	System.out.println(node.getNodeIp() +": 判断状态" );
			NodeMachineManager.getNodeMachine(node.getNodeIp());
		//	Thread thread = new Thread();		
			
			
		}
		try {
			Thread.sleep(10000);
			HibernateSessionFactory.closeSession();
		} catch (InterruptedException e) {
			log.error("", e);
		}
		
		while (true)
		{
			if (threadStop!=true)
			{
				NodeMachineManager.resetIdleMachines();
				try {
					Thread.sleep(10000);
					HibernateSessionFactory.closeSession();
				} catch (InterruptedException e) {
				}
			}
			else{
				threadStop = false; 
				log.debug("NodeThread Suspend");
				HibernateSessionFactory.closeSession();
				this.suspend();
			}
		} // end while(true);
		
	}
	public void threadStop(String reason)
	{
		log.debug("threadStop : "+reason);
		threadStop = true;
	}	
}
