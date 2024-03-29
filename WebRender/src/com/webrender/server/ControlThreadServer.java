
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
import com.webrender.dao.view.Commandnode;
import com.webrender.dao.view.CommandnodeDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.tool.NameMap;

/**
 * Command dispatcher Thread
 * Singleton Pattern
 * 
 * 任务分发主线程
 * 单一模式，防止出现分发命令冲突。
 * 
 * @author WAEN
 */

public final class ControlThreadServer extends Thread {
	private static ControlThreadServer instance = null; 
	private  boolean   threadStop   =   false;   //  thread stop flag
	private static final Log LOG = LogFactory.getLog(ControlThreadServer.class);
	private int  NewNotify = 0; // Double Check  防止有新的节点插入Idles中，或新的Command加入到WaitingCommands中
	private boolean noUsableNode = true; // nodes usable flag 
	private ControlThreadServer()
	{
		super();
		super.setName("ControlThreadServer");
		setDaemon(true);
	}
	
	public static synchronized ControlThreadServer getInstance()
	{
		if (instance == null){
			LOG.warn("A New ControlThreadServer Server");
			instance = new ControlThreadServer();
		}
		return instance;
	}
	
	/**
	 * 负责分发任务，一直运行
	 * 由wait() notify() 控制运行
	 * 
	 */
	public void run()
	{
		NodeDAO nodeDAO = new NodeDAO();
		CommandnodeDAO commandNodeDAO = new CommandnodeDAO();
		CommandDAO commandDAO = new CommandDAO();
		StatusDAO statusDAO = new StatusDAO();
		Command command = null;

		NodeMachine nodeMachine = null;
		Node node = null;
		NodeMachine tempNodeMachine = null;
		Executelog executelog = null;
		while(true)
		{
			if (!threadStop)
			{
//				根据优先级获取未完成的Quest
//				将其子命令中待执行的命令取出来
//				LOG.info("ControlThreadServer run..");
				try
				{
					Iterator ite_Nodes = NodeMachineManager.getInstance().getIdleMachines();
					if(!ite_Nodes.hasNext()) 
					{
//						LOG.info("NoIdleNodes...");
						if( NewNotify >= 5){
							synchronized(this){
								try {
									LOG.info("Dispather server waiting: no idles");
									wait();
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}							
							}
							continue;
						}
						else{
							NewNotify++;
							continue;
						}
					}
					
					while(ite_Nodes.hasNext())
					{
						if (threadStop){
							break;
						}
						nodeMachine = (NodeMachine)ite_Nodes.next();
						int nodeId = nodeMachine.getId();
						LOG.debug("Waiting nodeId: "+nodeId);
						Iterator ite_CommandsForNode = commandNodeDAO.findByNodeId(nodeId).iterator();
						if(ite_CommandsForNode.hasNext()){
							int commandId = ((Commandnode) ite_CommandsForNode.next() ).getId().getCommandId();
							command = commandDAO.findById(commandId);
							node = nodeDAO.findById(nodeId);
							noUsableNode = false;
						}else{
							noUsableNode = true;
							continue;
						}
//						if(NodeMachineManager.getInstance().isIdleEmpty())
//						{
//							if (threadStop){
//								break;
//							}
//
//							break;
//						}
//						try{
//							nodeMachine = null;
//							node = null;
//							commandDAO.attachClean(command);
//							Object[] nodeMachines = NodeMachineManager.getInstance().getIdleArray();
//							int length = nodeMachines.length;
//							if(NameMap.ONETOMANY.equalsIgnoreCase( command.getType() ) ){
//								nodeMachine = NodeMachineManager.getInstance().getNodeMachine( command.getNode().getNodeId() );
//								if(NodeMachineManager.getInstance().containIdles(nodeMachine))
//								{
//									node = command.getNode();
//									noUsableNode = false;
//								}
//								else{
//									nodeMachine = null;
//									node = null;
//									noUsableNode = true;
//								}
//							}
//							else{
//
//								List list_NodeGroupIds = nodeDAO.getNodeGroupIds(commandDAO.getNodegroup(command));
//								if(list_NodeGroupIds == null){
//									noUsableNode = true;
//								}else{
//									for(int i = 0 ; i<length ; i++){
//										tempNodeMachine = (NodeMachine) nodeMachines[i];
//										if(list_NodeGroupIds.contains(tempNodeMachine.getId()) ){
//											// 该节点保包含在任务执行池中，可以渲染
//											LOG.debug("command:"+command.getCommandId()+" 's nodegroup contains nodeId:"+tempNodeMachine.getId());
//											nodeMachine = tempNodeMachine;
//											node = nodeDAO.findById(tempNodeMachine.getId());
//											noUsableNode = false;
//											break;
//										}
//										LOG.debug("NoUsableNode for commandId:"+command.getCommandId());
//										noUsableNode = true;
//									}									
//								}
//							}
//						}
//						catch(Exception e){
//							LOG.error("getIdleMaichine fail",e);
//							try {
//								Thread.sleep(60000);
//							} catch (InterruptedException e1) {
//								LOG.error("",e);
//							}
//						}
						if(nodeMachine!=null && node!=null){ ///   node-command
//							commandDAO.attachClean(command);
							boolean result = nodeMachine.execute(command);
							if (result || NameMap.ONETOMANY.equalsIgnoreCase(command.getType()) ){// ONETOMANY 无论执行结果对错都算完成
								Transaction tx = null;
								try{
									tx = HibernateSessionFactory.getSession().beginTransaction();
									statusDAO = new StatusDAO();
									command.setNode(node);
									command.setStatus(statusDAO.findById(71));
									commandDAO.attachDirty(command);
									
									if(result==true) executelog = new  Executelog(command,statusDAO.findById(90),node,commandDAO.getNoteWithID(command).toString()+" is executed. ",new Date()); 
									else { // onetomany 执行错误
										command.setStatus(statusDAO.findById(73));
//										command.setSendTime(new Date());
										executelog = new  Executelog(command,statusDAO.findById(99),node,commandDAO.getNoteWithID(command).toString(),new Date());
									}
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									tx.commit();
									LOG.debug("NodeId: "+ node.getNodeId()+" :execute commandId:"+command.getCommandId()+ " Success");									
								}
								catch(Exception e){
									LOG.error("save commandStatus fail",e);
									if (tx != null){
										tx.rollback();
									}
								}finally{
									HibernateSessionFactory.closeSession();
								}
								break; //跳出此循环，重新分发命令
							}
							else{
								Transaction tx = null;
								try{
									tx = HibernateSessionFactory.getSession().beginTransaction();
									statusDAO = new StatusDAO();
									executelog = new  Executelog(command,statusDAO.findById(99),node,"SendError: " +commandDAO.getNoteWithID(command),new Date()); 
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									tx.commit();
									LOG.debug(node.getNodeId()+" :executeOperate fail  save to database");
									}
								catch(Exception e){
									LOG.error("save senderror event fail.", e);
									if (tx != null){
										tx.rollback();
									}
								}finally{
									HibernateSessionFactory.closeSession();
								}
								continue; //
							}
						} // end If has node-command
						
						
						
						
					} // end while nodes
					
					if(noUsableNode){
						if(NewNotify >=5){
							synchronized(this){
								try {
									LOG.info("Dispather server waiting:  No Can Execute Command..");
									wait();
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}							
							}
//							sleepUpdateVersion("NoUsableNode",20000);
						}
						else{
							NewNotify ++;
						}
					}
//				
				}
				catch(org.hibernate.exception.JDBCConnectionException e)
				{
					LOG.error("Cann't connect database !");
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e1) {
						LOG.error("",e);
					}
				}
				catch(Exception e)
				{
					LOG.warn("ControlThreadServer Error", e);
					
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e1) {
						LOG.error("",e1);
					}
				}
			}//end thread==true;
			else
			{
				// 调用一次threadStop()，只停一次。
				threadStop = false;
				LOG.info("ControlThreadServer threadStop");
				HibernateSessionFactory.closeSession();
				break;
				
			}
		}
	}
	
	
	private void threadStop(final String reason)
	{
		LOG.info(reason);
		threadStop = true;
		this.notifyResume();
	}
	public void stopServer(){
		threadStop("StopServer");
		try {
			this.join();
			LOG.info("end!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		instance = null;
		
	}
//	private void sleepUpdateVersion(String message,long millis) throws InterruptedException
//	{
//		LOG.debug(message);
//		HibernateSessionFactory.closeSession();	
//		Thread.sleep(millis);
//		StatusDAO statusDAO = new StatusDAO();
//		Transaction tx = null;
//		try {
//			tx = HibernateSessionFactory.getSession().beginTransaction();
//			statusDAO.updateSystemVersion();
//			tx.commit();							
//		}catch (Exception e){					
//			LOG.error(message+" UpdateSystemVersion Error", e);
//			if(tx!=null){
//				tx.rollback();
//			}
//		}finally{
//			HibernateSessionFactory.closeSession();			
//		}
//	}
	public void notifyResume(){
		NewNotify = 0;
		if (this.getState() == Thread.State.WAITING){
			synchronized(this){
				HibernateSessionFactory.closeSession();	
//				try {
//					this.sleep(1000);
//				} catch (InterruptedException e) {
//					LOG.error("notify dispather server error");
//				}
				notify();
			}
//			LOG.info("Dispather server notify..");
		}
	}
}