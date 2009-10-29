package com.webrender.server;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public final class Dispatcher extends Thread {
	private static Dispatcher instance = new Dispatcher();
	private static final Log LOG = LogFactory.getLog(Dispatcher.class);
	private NodeDAO nodeDAO=new NodeDAO();
	private CommandDAO commandDAO = new CommandDAO();
	private StatusDAO statusDAO = new StatusDAO();
//	private boolean isRunning = false;
	private boolean noUsableNode = false;
	private Dispatcher(){
	}
	
	public static Dispatcher getInstance()
	{
		return instance;
		
	}
	
	public  void run(){
		LOG.info("Dispatcher runs..");
//		isRunning = true;
		while(true){
			try{
				Iterator ite_Commands =commandDAO.getWaitingCommands().iterator();
				if(ite_Commands.hasNext()==false || NodeMachineManager.getInstance().isIdleEmpty()){
//					isRunning = false;
					LOG.info("Dispatcher ends..");
					return;
				}
				while(ite_Commands.hasNext()){
					Command command = (Command)ite_Commands.next();
					if(NodeMachineManager.getInstance().isIdleEmpty()){
//						isRunning = false;
						LOG.info("Dispatcher ends..");
						return;
					}
					NodeMachine nodeMachine = NodeMachineManager.getInstance().canExeCommand(command);
					if(nodeMachine !=null){
						noUsableNode = false;
						Node node = nodeDAO.findById(nodeMachine.getId());
						Transaction tx = null;
						if( nodeMachine.execute(command) ){
							try{
								tx = HibernateSessionFactory.getSession().beginTransaction();
								statusDAO = new StatusDAO();
								command.setNode(node);
								command.setStatus(statusDAO.findById(71));
								commandDAO.attachDirty(command);
								Executelog executelog = new  Executelog(command,statusDAO.findById(90),node,commandDAO.getNoteWithID(command).toString(),new Date()); 
								ExecutelogDAO exeDAO = new ExecutelogDAO();
								exeDAO.save(executelog);
								tx.commit();
								LOG.debug(node.getNodeId()+" :executeOperate Success save to database");									
							}catch(Exception e){
								LOG.error("save database fail",e);
								if (tx != null){
									tx.rollback();
								}
							}finally{
								HibernateSessionFactory.closeSession();
							}
							break;
						}else{
							try{
								tx = HibernateSessionFactory.getSession().beginTransaction();
								statusDAO = new StatusDAO();
								Executelog executelog = new  Executelog(command,statusDAO.findById(99),node,"SendError: " +commandDAO.getNoteWithID(command),new Date()); 
								ExecutelogDAO exeDAO = new ExecutelogDAO();
								exeDAO.save(executelog);
								tx.commit();
								LOG.debug(node.getNodeId()+" :executeOperate fail  save to database");
							}
							catch(Exception e)
							{
								LOG.error("save database fail", e);
								if (tx != null) 
								{
									tx.rollback();
								}
								// kill command
							}finally{
								HibernateSessionFactory.closeSession();
							}
							continue; 
						}
					}
					else{
						noUsableNode = true;
					}
				}
				// 判定是否跳出循环的结束代码
				if(noUsableNode == true){
//					isRunning = false;
					LOG.info("Dispatcher ends..");
					return;
				}
			}catch(Exception e){
				LOG.error("exeCommands total fail",e);
				HibernateSessionFactory.closeSession();
//				isRunning = false;
				LOG.info("Dispatcher ends..");
				return;
			}
		}//end while(true)
	}// end exeCommands()
	
	
	private void UpdateVersion(String message) 
	{
		StatusDAO statusDAO = new StatusDAO();
		Transaction tx = null;
		try {
			tx = HibernateSessionFactory.getSession().beginTransaction();
			statusDAO.updateSystemVersion();
			tx.commit();							
		}catch (Exception e){					
			LOG.error(message+" UpdateVersion Error", e);
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			HibernateSessionFactory.closeSession();			
		}
	}
}
