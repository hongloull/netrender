package com.webrender.axis;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

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

public class NodeOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(NodeOperate.class);
	
	public String pauseNode(String nodeId)
	{
		LOG.debug("pauseNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR+e.getMessage();
		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			String result = this.killCommand(nodeId);
			nodeMachine.setPause(true);
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"Pause nodeId:"+nodeId);
			tx.commit();
			if(BaseAxis.ACTIONSUCCESS.equals(result)){
				LOG.debug("pauseNode success");
				return BaseAxis.ACTIONSUCCESS;							
			}
			else{
				LOG.error("pauseNode fail killCommandError");
				nodeMachine.setPause(true);
				return "KillCommandError";
			}
		}catch(Exception e){
			LOG.error("pauseNode fail", e);
			if(tx!=null){
				tx.rollback();
			}
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String resumeNode(String nodeId)
	{
		LOG.debug("resumeNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR+e.getMessage();
		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			nodeMachine.setPause(false);
			LOG.debug("resumeNode success");
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"Resume nodeId:"+nodeId);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;			
		}catch(Exception e){
			LOG.error("resumeNode fail");
			if(tx!=null){
				tx.rollback();
			}
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String setRealLog(String nodeId,String isOpen){
		LOG.debug("setRealLog");
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR+e.getMessage();
		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			nodeMachine.setRealTime(Integer.parseInt(isOpen)==1?true:false);
			LOG.debug("setRealLog success");
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"SetRealTime "+(Integer.parseInt(isOpen)==1?"open":"close") );
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		}catch(Exception e)
		{
			LOG.error("setRealLog fail" ,e);
			if(tx!=null){
				tx.rollback();
			}
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String killCommand(String nodeId)
	{
		LOG.info("killCommand");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR+e.getMessage();
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findById(Integer.parseInt(nodeId)) ;
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			boolean flag = nodeMachine.execute(ServerMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_KILL")));
			//CommandDAO commandDAO = new CommandDAO();
			logOperate(getLoginUserId(),Operatelog.MOD,"kill Commands in nodeId:"+nodeId );
			StatusDAO statusDAO = new StatusDAO();
			if (flag)
			{
				nodeMachine.cleanRunCommands("Service kill "+nodeId+"'s Commands");
			}
			else
			{
				Executelog executelog = new  Executelog(null,statusDAO.findById(99),node,"killError",new Date());
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				exeDAO.save(executelog);
				nodeMachine.setPause(true);
				return BaseAxis.ACTIONFAILURE;
			}
			tx.commit();
			nodeMachine.setBusy(false);
//			ControlThreadServer.getInstance().resume();
			LOG.debug("killCommand success");
			return BaseAxis.ACTIONSUCCESS;
		}
		
		catch(Exception e)
		{
			LOG.error("killCommand error",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  shutdownNode(String nodeId ,String isReboot)
	{
		// isReboot  0 shutdown  1 reboot 
		LOG.info("shutdownNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR+e.getMessage();
		}
		Transaction tx =null;
		boolean exeFlag = false;
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			if (Integer.parseInt(isReboot)==0)  //shutdown
			{
//				if( nodeMachine.execute(ServerMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_SHUTDOWN"))))
//				{
//					exeFlag =true;
//					message = "shutdown "+nodeId;
//				}
//				else{
//					message = "shutdown "+nodeId+" fail!";
//				}
			}
			else if (Integer.parseInt(isReboot)==1) // reboot
			{
//				if( nodeMachine.execute(ServerMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_RESTART")))){
//					exeFlag = true;
//					message = "reboot "+nodeId;
//				}
//				else{
//					message = "reboot "+nodeId+" fail!";
//				}
			}
			logOperate(getLoginUserId(),exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE;
		}catch(Exception e){
			
			return ACTIONFAILURE+e.getMessage();
		}finally{
			closeSession();
		}
	}
	public String  softRestart(String nodeId){
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR+e.getMessage();
		}
		Transaction tx =null;
		boolean exeFlag = false;
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			try{
				if( nodeMachine.execute(ServerMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_SOFTRESTART")))){
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
			
			logOperate(getLoginUserId(),exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE;
		}
		catch(Exception e){
			LOG.error("softRestart fail nodeId:"+ nodeId,e);
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
	public String exeSystemCommand(String nodeId,String FLAG,String needFeedBack)
	{
		Transaction tx =null;
		boolean exeFlag = false;
		boolean feedBackFlag = true;
		if("0".endsWith(needFeedBack)){
			feedBackFlag = false;
		}
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			try{
				CODE code = EOPCODES.getInstance().get("S_SYSTEM").getSubCode(FLAG);
				if (code == null){
					return BaseAxis.ACTIONFAILURE+": "+FLAG+" doesn't exist in head.xml" ;
				}
				else if( nodeMachine.execute(ServerMessages.createSystemPkt(code)) || !feedBackFlag ){
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
			
			logOperate(getLoginUserId(),exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE;
		}
		catch(Exception e){
			LOG.error("softRestart fail nodeId:"+ nodeId,e);
			return ACTIONFAILURE+e.getMessage();
		}
		
	}
	
	
}
