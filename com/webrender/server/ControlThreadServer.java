
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
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.tool.NameMap;




//	任务分发主线程
//	单一模式，防止出现分发命令冲突。
//  
//
//
public final class ControlThreadServer extends Thread {
	private static ControlThreadServer instance = null; 
	private  boolean   threadStop   =   false;   
	private static final Log LOG = LogFactory.getLog(ControlThreadServer.class);
	private int  NewNotify = 0; // 防止有新的节点插入Idles中，或新的Command加入到WaitingCommands中，二次建议。
	private boolean noUsableNode = true;
	private ControlThreadServer()
	{
		super();
		super.setName("ControlThread");
		setDaemon(true);
	}
	
	public static ControlThreadServer getInstance()
	{
		if (instance == null){
			LOG.warn("A new controlthread server");
			instance = new ControlThreadServer();
		}
		return instance;
	}
	
	public void run()
	{
		NodeDAO nodeDAO = new NodeDAO();
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
//				LOG.info("ControlThread run..");
				try
				{
					Iterator ite_Commands  = commandDAO.getWaitingCommands().iterator();
					
					if(!ite_Commands.hasNext()) 
					{
//						LOG.info("NoWaitingCommands...");
						if( NewNotify >= 5){
							noUsableNode = false;
							synchronized(this){
								try {
									LOG.info("Dispather server waiting: no commands");
									wait();
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}							
							}
//						sleepUpdateVersion("NoWaitingCommand",30000);
							continue;
						}
						else{
							NewNotify++;
							continue;
						}
					}
					
					
					while(ite_Commands.hasNext())
					{
						if (threadStop){
							break;
						}
						command = (Command)ite_Commands.next();
//						LOG.info("Waiting commandId: "+command.getCommandId());
						if(NodeMachineManager.getInstance().isIdleEmpty())
						{
							if (threadStop){
								break;
							}
							noUsableNode = true;
							break;
						}
						
						try{
							nodeMachine = null;
							node = null;
							commandDAO.attachClean(command);
							Object[] nodeMachines = NodeMachineManager.getInstance().getIdleArray();
							int length = nodeMachines.length;
							if(NameMap.ONETOMANY.equalsIgnoreCase( command.getType() ) ){
								nodeMachine = NodeMachineManager.getInstance().getNodeMachine( command.getNode().getNodeId() );
								if(NodeMachineManager.getInstance().containIdles(nodeMachine))
								{
									node = command.getNode();
									noUsableNode = false;
								}
								else{
									nodeMachine = null;
									node = null;
									noUsableNode = true;
								}
							}
							else{
								List list_NodeGroupIds = nodeDAO.getNodeGroupIds(command.getQuest().getNodegroup());
								for(int i = 0 ; i<length ; i++){
									tempNodeMachine = (NodeMachine) nodeMachines[i];
									if(list_NodeGroupIds.contains(tempNodeMachine.getId()) ){
										// 该节点保包含在任务执行池中，可以渲染
										nodeMachine = tempNodeMachine;
										node = nodeDAO.findById(tempNodeMachine.getId());
										noUsableNode = false;
										break;
									}
									noUsableNode = true;
								}
							}
						}
						catch(Exception e){
							LOG.error("getIdleMaichine fail",e);
						}
						if(nodeMachine!=null && node!=null){ ///   111111111111
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
										command.setSendTime(new Date());
										executelog = new  Executelog(command,statusDAO.findById(99),node,commandDAO.getNoteWithID(command).toString(),new Date());
									}
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									tx.commit();
									LOG.debug("NodeId: "+ node.getNodeId()+" :execute commandId:"+command.getCommandId()+ " Success save to database");									
								}
								catch(Exception e){
									LOG.error("save commandStatus fail",e);
									if (tx != null){
										tx.rollback();
									}
								}finally{
									HibernateSessionFactory.closeSession();
								}
								break; //跳出此循环，分发下一个命令
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
						} // end If 1111111111
					} // end while commands
					if(noUsableNode){
						if(NewNotify >=5){
							synchronized(this){
								try {
									LOG.info("Dispather server waiting: no idle nodes");
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
//				循环上述操作：到所有任务全部完成  停止该线程
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
		if (this.getState() == Thread.State.WAITING){
			synchronized(this){
				NewNotify = 0;
				notify();
			}
//			LOG.info("Dispather server notify..");
		}
	}
}