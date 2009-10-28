package com.webrender.axis.operate;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Operatelog;
import com.webrender.dao.StatusDAO;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.server.Dispatcher;

public class NodeOperateImpl extends BaseOperate {
	
	private static final Log LOG = LogFactory.getLog(NodeOperateImpl.class);
	private ServerMessages serverMessages = new ServerMessages();
	public String pauseNode(String nodeId,int regUserId)
	{	
		LOG.debug("pauseNode nodeId:"+nodeId);
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
//			String result = this.killCommand(nodeId);
			nodeMachine.setPause(true);
			tx = getTransaction();
			logOperate(regUserId,Operatelog.MOD,"Pause nodeId:"+nodeId);
			tx.commit();
//			if(BaseAxis.ACTIONSUCCESS.equals(result)){
				LOG.debug("pauseNode success");
				return ACTIONSUCCESS;							
//			}
//			else{
//				LOG.error("pauseNode fail killCommandError");
//				nodeMachine.setPause(true);
//				return "KillCommandError";
//			}
		}catch(Exception e){
			LOG.error("pauseNode fail", e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String resumeNode(String nodeId,int regUserId)
	{
		LOG.debug("resumeNode nodeId:"+nodeId);

		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			nodeMachine.setPause(false);
			LOG.debug("resumeNode success");
			tx = getTransaction();
			logOperate(regUserId,Operatelog.MOD,"Resume nodeId:"+nodeId);
			tx.commit();
			return ACTIONSUCCESS;			
		}catch(Exception e){
			LOG.error("resumeNode fail");
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String setRealLog(String nodeId,String isOpen,int regUserId){
				
		LOG.debug("setRealLog nodeId:"+nodeId+" isOpen:"+isOpen);
//		try{
//			if ( ! this.canVisit(7)){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR+e.getMessage();
//		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			nodeMachine.setRealTime(Integer.parseInt(isOpen)==1?true:false);
			LOG.debug("setRealLog success");
			tx = getTransaction();
			logOperate(regUserId,Operatelog.MOD,"SetRealTime "+(Integer.parseInt(isOpen)==1?"open":"close") );
			tx.commit();
			return ACTIONSUCCESS;
		}catch(Exception e)
		{
			LOG.error("setRealLog fail" ,e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String getPriority(String nodeId){
		LOG.debug("getPriority nodeId:"+nodeId);
		try{
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findById(Integer.parseInt(nodeId));
			return node.getPri().toString();
		}catch(Exception e){
			LOG.error("getPriority fail.",e);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String setPriority(String nodeId,String priority,int regUserId){
				
		LOG.debug("setPriority nodeId:"+ nodeId +" prioriy:"+priority);
		Transaction tx = null;
		try{
			int id = Integer.parseInt(nodeId);
			short pri = Short.parseShort(priority);
			NodeDAO nodeDAO = new NodeDAO();
			tx = getTransaction();
			Node node = nodeDAO.findById(id);
			node.setPri(pri);
			logOperate(regUserId,Operatelog.MOD,"set Node:"+node.getNodeName()+" 's priority to "+ pri );
			tx.commit();
			return ACTIONSUCCESS;
		}catch(Exception e){
			LOG.error("setPriority fail.",e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}		
	}
	
	public String killCommand(String nodeId,int regUserId)
	{
		LOG.info("killCommand nodeId:"+nodeId);

		Transaction tx = null;
		try
		{
			
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findById(Integer.parseInt(nodeId)) ;
			NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			boolean flag = nodeMachine.execute(serverMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_KILL")));
			//CommandDAO commandDAO = new CommandDAO();
			
			StatusDAO statusDAO = new StatusDAO();
			if (flag)
			{
				nodeMachine.cleanRunCommands("Service kill "+nodeId+"'s Commands");
				tx = getTransaction();
				logOperate(regUserId,Operatelog.MOD,"kill Commands in nodeId:"+nodeId );
				tx.commit();
			}
			
			else
			{
				tx = getTransaction();
				Executelog executelog = new  Executelog(null,statusDAO.findById(99),node,"killError",new Date());
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				exeDAO.save(executelog);
				tx.commit();
				nodeMachine.setPause(true);
				return ACTIONFAILURE+"kill commands error: node not response.";
			}
			
			nodeMachine.setBusy(false);
//			ControlThreadServer.getInstance().resume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("killCommand success");
			return ACTIONSUCCESS;
		}
		
		catch(Exception e)
		{
			LOG.error("killCommand error",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  shutdownNode(String nodeId ,String isReboot,int regUserId)
	{
		// isReboot  0 shutdown  1 reboot 
		
		LOG.info("shutdownNode nodeId:"+nodeId +" isReboot:"+isReboot);
		Transaction tx =null;
		boolean exeFlag = false;
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			if (Integer.parseInt(isReboot)==0)  //shutdown
			{
				if( nodeMachine.execute(serverMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_SHUTDOWN"))))
				{
					exeFlag =true;
					message = "shutdown "+nodeId;
				}
				else{
					message = "shutdown "+nodeId+" fail!";
				}
			}
			else if (Integer.parseInt(isReboot)==1) // reboot
			{
				if( nodeMachine.execute(serverMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_RESTART")))){
					exeFlag = true;
					message = "reboot "+nodeId;
				}
				else{
					message = "reboot "+nodeId+" fail!";
				}
			}
			logOperate(regUserId,exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE+" shutdown node error: node note response.";
		}catch(Exception e){
			LOG.error("shutdownNode fail. nodeId:"+nodeId,e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			closeSession();
		}
	}
	public String  softRestart(String nodeId,int regUserId){
		
		LOG.debug("softRestart nodeId: "+nodeId);
		Transaction tx =null;
		boolean exeFlag = false;
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			try{
				if( nodeMachine.execute(serverMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_SOFTRESTART")))){
					exeFlag = true;
					message = "soft restart "+nodeId;
				}else{
					message = "soft restart "+nodeId+" fail!";
				}				
			}catch(NullPointerException e1)
			{
				exeFlag = true;
				message = "soft restart "+nodeId;
			}
			
			logOperate(regUserId,exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE+"soft restart error :node not response.";
		}
		catch(Exception e){
			LOG.error("softRestart fail nodeId:"+ nodeId,e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			closeSession();
		}
	}
	
	/**
	 * 
	 * @param nodeId 
	 * @param FLAG   SYSTEMCODE
	 * @param needFeedBack  0 notNeed  1 need
	 * @return
	 */
	public String exeSystemCommand(String nodeId,String FLAG,String needFeedBack,int regUserId)
	{
		LOG.debug("exeSystemCommand nodeId:"+ nodeId);
		Transaction tx =null;
		boolean exeFlag = false;
		boolean feedBackFlag = true;
		if("0".endsWith(needFeedBack)){
			feedBackFlag = false;
		}
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			try{
				CODE code = EOPCODES.getInstance().get("S_SYSTEM").getSubCode(FLAG);
				if (code == null){
					return ACTIONFAILURE+": "+FLAG+" doesn't exist in head.xml" ;
				}
				else if( nodeMachine.execute(serverMessages.createSystemPkt(code)) || !feedBackFlag ){
					exeFlag = true;
					message = FLAG + " "+nodeId;
				}else{
					message = FLAG + " "+nodeId+" fail!";
				}				
			}catch(NullPointerException e)
			{
				if(feedBackFlag == false){
					exeFlag = true;
					message = FLAG + " "+nodeId;					
				}
				else{
					message = FLAG + " "+nodeId+" fail !";
				}				
			}
			
			logOperate(regUserId,exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE+"exe systemcommand error: node not response";
		}
		catch(Exception e){
			LOG.error("exeSystemCommand fail nodeId:"+ nodeId,e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			closeSession();
		}
		
	}
	
	public String delNode(String nodeId,int regUserId){
		LOG.debug("delete node id:"+nodeId);
		Transaction tx = null;
		try{
			tx = getTransaction();
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findById(Integer.parseInt(nodeId));
			nodeDAO.delete(node);
			logOperate(regUserId,Operatelog.MOD,"delete nodeId:"+nodeId +" name:"+node.getNodeName()+" ip:"+node.getNodeIp());
			tx.commit();
			LOG.debug("delNode success nodeId:"+nodeId);
			return ACTIONSUCCESS;
		}catch(Exception e){
			LOG.error("delNode fail nodeId:"+nodeId);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	
}
