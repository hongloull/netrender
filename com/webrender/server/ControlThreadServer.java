
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




//	任务分发主线程
//	单一模式，防止出现分发命令冲突。
//  
//
//
public final class ControlThreadServer extends Thread {
	private static ControlThreadServer instance = new ControlThreadServer();
	private static  boolean   threadStop   =   false;   
	private static final Log LOG = LogFactory.getLog(ControlThreadServer.class);
	private static boolean noUsableNode = true;
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
//		NodeDAO nodeDAO = new NodeDAO();
//		Iterator nodes= nodeDAO.findAll().iterator();
//		while (nodes.hasNext())
//		{
//			NodeMachineManager.getNodeMachine(((Node)nodes.next() ).getNodeIp());			
//		}
		NodeDAO nodeDAO = new NodeDAO();
		CommandDAO commandDAO = new CommandDAO();
		StatusDAO statusDAO = new StatusDAO();
		while(true)
		{
//			LOG.info("ControlThreadServerRun");
//				获取未完成的Quest ，得到相关命令，交给空闲的节点
			if (!threadStop)
			{
//				根据优先级获取未完成的Quest
//				将其子命令中待执行的命令取出来
				try
				{
					List<Command> waitingCommands = commandDAO.getWaitingCommands();
//					int waitingCommandsSize = waitingCommands.size();
//					StringBuilder commandsInfo = new StringBuilder();
//					for(int i=0;i<waitingCommandsSize;i++){
//						commandsInfo.append(waitingCommands.get(i).getCommandId()).append(" ");
//					}
//					LOG.info("WaitingCommands size: "+ waitingCommandsSize+":commandsInfo:"+commandsInfo);
					
					
				//	System.out.println("CommandsSize : " + .size());
					Iterator ite_Commands =waitingCommands.iterator();
					
					// 无待执行命令。
					if (!ite_Commands.hasNext()) 
					{
						// 停顿半分钟
						sleepUpdateVersion("NoWaitingCommand",60000);
						noUsableNode = false;
												
					}
					// 按序执行命令
					while(ite_Commands.hasNext())
					{
						if (threadStop){
							break;
						}
						
						Command command = (Command)ite_Commands.next();
						
//					取1子命令，取1节点机 逻辑处理命令格式（考虑节点机具体参数不同）
//					将子命令发送给该节点机	
//					获取空闲节点机  （需要判断是否有该渲染引擎）
						//	Iterator ite_Nodes = nodeDAO.getIdleNodes(command).iterator();//从数据库中读取状态
						//	Iterator ite_Nodes = nodeDAO.findAll().iterator();
					
				
						if(NodeMachineManager.isIdleEmpty())
						{
							if (threadStop){
								break;
							}
							noUsableNode = true;
							break;
						}
						NodeMachine nodeMachine = null;
						Node node = null;
						try{
							Object[] nodeMachines = NodeMachineManager.getIdleArray();
							int length = nodeMachines.length;
							NodeMachine tempNodeMachine = null;
							Node tempNode = null;
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
							//  将游离的Command 变成 持久化
						}
						catch(Exception e){
							LOG.error("getIdleMaichine fail",e);
						}
						if(nodeMachine!=null && node!=null) ///   111111111111
						{
//						Node node = (Node)ite_Nodes.next();
//						NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeIp());
							
							//  将游离的Command 变成 持久化
							commandDAO.attachClean(command);
							
							// 交给nodeMachine 执行 一条command
							boolean result = nodeMachine.execute(command);
							if (result){
							/*
							 * 节点执行Command成功
							 * 修改command状态为Inprogress 71 
							 * 添加日志。
							 *  成功Break 跳出寻找节点机的循环，找下一个Command
							 *  失败一般为保存数据库出错。如 Command状态没改。可能会再执行一遍。
							 */
								//节点状态变成InProgress（42） &&  Command状态为InProgress（71）
								Transaction tx = null;
								try{
									tx = HibernateSessionFactory.getSession().beginTransaction();
									statusDAO = new StatusDAO();
									//		node.setStatus(statusDAO.findById(42));
									//		nodeDAO.attachDirty(node);
									command.setNode(node);
									command.setStatus(statusDAO.findById(71));
									commandDAO.attachDirty(command);	
									// add LOG
									
									Executelog executelog = new  Executelog(command,statusDAO.findById(90),node,commandDAO.getNote(command).toString(),new Date()); 
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									
									tx.commit();
									
									LOG.debug(node.getNodeId()+" :executeOperate Success save to database");
									//	HibernateSessionFactory.closeSession();
									//	System.out.println ("发送成功：closeSession : " + nodeDAO.getIdleNodes(command).size() );
									
								}
								catch(Exception e)
								{
									LOG.error("save commandStatus fail",e);
									if (tx != null) 
									{
										tx.rollback();
									}
									
								}finally
								{
									HibernateSessionFactory.closeSession();
								}
								//跳出此循环，分发下一个命令
								break;
							}
							else 
							{
								/*
								 * 发送错误。添加出错日志
								 * 保存后，使用Continue交给下个节点继续执行该Command
								 */
								Transaction tx = null;
								try{
									tx = HibernateSessionFactory.getSession().beginTransaction();
									statusDAO = new StatusDAO();
									//		node.setStatus(statusDAO.findById(44)); //disconnect
									//		nodeDAO.attachDirty(node);	
									Executelog executelog = new  Executelog(command,statusDAO.findById(99),node,"SendError: " +commandDAO.getNote(command),new Date()); 
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									tx.commit();
									LOG.debug(node.getNodeId()+" :executeOperate fail  save to database");
									//		HibernateSessionFactory.closeSession();
									//	System.out.println (" 发送错误 : closeSession : " + nodeDAO.getIdleNodes(command).size() );
									
								}
								catch(Exception e)
								{
									LOG.error("", e);
									if (tx != null) 
									{
										tx.rollback();
									}
									// kill command
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