
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
	private static ControlThreadServer instance = new ControlThreadServer();
	private  boolean   threadStop   =   false;   
	private static final Log LOG = LogFactory.getLog(ControlThreadServer.class);

	private boolean noUsableNode = true;
	private ControlThreadServer()
	{
		super();
		super.setName("ControlThread");
		setDaemon(true);
	}
	
	public static ControlThreadServer getInstance()
	{
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
		Node tempNode = null;
		Executelog executelog = null;
		while(true)
		{
			if (!threadStop)
			{
//				根据优先级获取未完成的Quest
//				将其子命令中待执行的命令取出来
				try
				{
					List<Command> waitingCommands = commandDAO.getWaitingCommands();

					Iterator ite_Commands =waitingCommands.iterator();
					
					
					if (!ite_Commands.hasNext()) 
					{
						sleepUpdateVersion("NoWaitingCommand",30000);
						noUsableNode = false;
					}
					while(ite_Commands.hasNext())
					{
						if (threadStop){
							break;
						}
						command = (Command)ite_Commands.next();
						
						if(NodeMachineManager.getInstance().isIdleEmpty())
						{
							if (threadStop){
								break;
							}
							noUsableNode = true;
							break;
						}
						
						try{
							commandDAO.attachClean(command);
							Object[] nodeMachines = NodeMachineManager.getInstance().getIdleArray();
							int length = nodeMachines.length;
							if(NameMap.ONETOMANY.equalsIgnoreCase( command.getType() ) ){
								nodeMachine = NodeMachineManager.getInstance().getNodeMachine( command.getNode().getNodeId() );
								node = command.getNode();
							}
							for(int i = 0 ; i<length ; i++){
								tempNodeMachine = (NodeMachine) nodeMachines[i];
								tempNode = nodeDAO.findById(tempNodeMachine.getId());
								if( command.getQuest().getNodegroup().getNodes().contains(tempNode) ){
									// 该节点保包含在任务执行池中，可以渲染
									nodeMachine = tempNodeMachine;
									node = tempNode;
									noUsableNode = false;
									break;
								}
								noUsableNode = true;
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
									
									if(result==true) executelog = new  Executelog(command,statusDAO.findById(90),node,commandDAO.getNoteWithID(command).toString(),new Date()); 
									else { // onetomany 执行错误
										command.setStatus(statusDAO.findById(72));
										command.setSendTime(new Date());
										executelog = new  Executelog(command,statusDAO.findById(99),node,commandDAO.getNoteWithID(command).toString(),new Date());
									}
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									tx.commit();
									LOG.debug(node.getNodeId()+" :executeOperate Success save to database");									
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
						sleepUpdateVersion("NoUsabelNode",20000);
					}
//				循环上述操作：到所有任务全部完成  停止该线程
				}
				catch(org.hibernate.exception.JDBCConnectionException e)
				{
					LOG.info("Cann't connect database !");
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
				LOG.info("ControlThreadServer threadStop Suspend");
				HibernateSessionFactory.closeSession();
				this.suspend();
				try {
					
					sleepUpdateVersion("ControlThreadServer resume",10000);
					
				}catch (InterruptedException e) {
					LOG.error("ControlThread Sleep", e);
				}
			}
		}
	}
	
	
//	暂停某Quest的时候 可能需要先将 分发线程暂停，再运行
	public void threadSuspend(final String reason)
	{
		LOG.info(reason);
		threadStop = true;
	}
	
	private void sleepUpdateVersion(String message,long millis) throws InterruptedException
	{
//		LOG.info(message);
		HibernateSessionFactory.closeSession();	
		Thread.sleep(millis);
		StatusDAO statusDAO = new StatusDAO();
		Transaction tx = null;
		try {
			tx = HibernateSessionFactory.getSession().beginTransaction();
			statusDAO.updateSystemVersion();
			tx.commit();							
		}catch (Exception e){					
			LOG.error(message+" UpdateSystemVersion Error", e);
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			HibernateSessionFactory.closeSession();			
		}
	}
	
}