
package com.webrender.server;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.webrender.dao.Status;
import com.webrender.dao.StatusDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;




//	任务分发主线程
//	单一模式，防止出现分发命令冲突。
//  
//
//
public class ControlThreadServer extends Thread {
	private static ControlThreadServer instance = new ControlThreadServer();
	private static  boolean   threadStop   =   false;   
	private static final Log log = LogFactory.getLog(ControlThreadServer.class);
	private ControlThreadServer()
	{
		super.setName("ControlThread");
	}
	
	public final static ControlThreadServer getInstance()
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
		while(true)
		{
			log.info("ControlThreadServer Run");
//				获取未完成的Quest ，得到相关命令，交给空闲的节点
			if (threadStop!=true)
			{
//				根据优先级获取未完成的Quest
//				将其子命令中待执行的命令取出来
				try
				{
					HibernateSessionFactory.closeSession();
					
				//	System.out.println("CommandsSize : " + commandDAO.getWaitingCommands().size());
					Iterator ite_Commands = commandDAO.getWaitingCommands().iterator();
					
					// 无待执行命令。
					if (ite_Commands.hasNext()==false) 
					{
						log.info("No WaitingCommands Thread Suspend");
						HibernateSessionFactory.closeSession();
						this.suspend();
						try {
							log.info("ControlThreadServer resume");
							//关2次，因为遇到有待执行命令查询不到的问题
							HibernateSessionFactory.closeSession();
							Thread.sleep(10000);
							
						} catch (InterruptedException e) {
							log.error("Thread Sleep", e);
						}
						continue ;
					}
					// 按序执行命令
					while(ite_Commands.hasNext())
					{
						if (threadStop == true) break;
						
						Command command = (Command)ite_Commands.next();
						
//					取1子命令，取1节点机 逻辑处理命令格式（考虑节点机具体参数不同）
//					将子命令发送给该节点机	
//					获取空闲节点机  （需要判断是否有该渲染引擎）
						//	Iterator ite_Nodes = nodeDAO.getIdleNodes(command).iterator();//从数据库中读取状态
						//	Iterator ite_Nodes = nodeDAO.findAll().iterator();
					
						int nodes_Size = NodeMachineManager.idleMachines.size();
					// nodes_Size 空闲节点机的个数。
						while(nodes_Size == 0)
						{
							if (threadStop ==true) break;
							
							log.info("NoIdleNode");
							try {
								this.sleep(20000);
								nodes_Size = NodeMachineManager.idleMachines.size();
							} catch (InterruptedException e) {
								log.error("", e);
							}
						}
						
						
						
						Iterator ite_Nodes = NodeMachineManager.idleMachines.iterator();
						while(ite_Nodes.hasNext())
						{
//						Node node = (Node)ite_Nodes.next();
//						NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeIp());
							NodeMachine nodeMachine  = (NodeMachine)ite_Nodes.next();
							Node node = nodeDAO.findByNodeIp(nodeMachine.getIp());
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
									StatusDAO statusDAO = new StatusDAO();
									//		node.setStatus(statusDAO.findById(42));
									//		nodeDAO.attachDirty(node);
									command.setNode(node);
									command.setStatus(statusDAO.findById(71));
									commandDAO.attachDirty(command);	
									// add log
									Executelog executelog = new  Executelog(command,statusDAO.findById(90),node,nodeMachine.getCommand(command),new Date()); 
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									
									tx.commit();
									
									log.debug(node.getNodeIp()+" :executeOperate Success save to database");
									//	HibernateSessionFactory.closeSession();
									//	System.out.println ("发送成功：closeSession : " + nodeDAO.getIdleNodes(command).size() );
									
								}
								catch(Exception e)
								{
									log.error("save commandStatus fail",e);
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
									StatusDAO statusDAO = new StatusDAO();
									//		node.setStatus(statusDAO.findById(44)); //disconnect
									//		nodeDAO.attachDirty(node);	
									Executelog executelog = new  Executelog(command,statusDAO.findById(99),node,"SendError: " +nodeMachine.getCommand(command),new Date()); 
									ExecutelogDAO exeDAO = new ExecutelogDAO();
									exeDAO.save(executelog);
									tx.commit();
									log.debug(node.getNodeIp()+" :executeOperate Error  save to database");
									//		HibernateSessionFactory.closeSession();
									//	System.out.println (" 发送错误 : closeSession : " + nodeDAO.getIdleNodes(command).size() );
									
								}
								catch(Exception e)
								{
									log.error("", e);
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
						}
					}
//				循环上述操作：到所有任务全部完成  停止该线程
				}
				catch(org.hibernate.exception.JDBCConnectionException e)
				{
					log.info("Cann't connect database !");
					try {
						this.sleep(10000);
					} catch (InterruptedException e1) {
						log.error("",e);
					}
				}
				catch(Exception e)
				{
					log.warn("ControlThreadServer Error", e);
					
					try {
						this.sleep(10000);
					} catch (InterruptedException e1) {
						log.error("",e1);
					}
				}
			}//end thread==true;
			else
			{
				// 调用一次threadStop()，只停一次。
				threadStop = false;
				log.info("ControlThreadServer Suspend");
				HibernateSessionFactory.closeSession();
				this.suspend();
			}
		}
	}
	
	
//	暂停某Quest的时候 可能需要先将 分发线程暂停，再运行
	public void threadSuspend(String reason)
	{
		log.info(reason);
		threadStop = true;
	}
	
}