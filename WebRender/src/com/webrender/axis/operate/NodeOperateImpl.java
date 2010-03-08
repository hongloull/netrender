package com.webrender.axis.operate;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.config.NetRenderLogFactory;
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
import com.webrender.server.ControlThreadServer;
import com.webrender.server.NodeLogServer;

public class NodeOperateImpl extends BaseOperate {
	
	private static final Log LOG = LogFactory.getLog(NodeOperateImpl.class);
	private ServerMessages serverMessages = new ServerMessages();
	private static Map<String,String> macs = new Hashtable<String,String>();
	static{
		macs.put("5",  "00:16:76:E2:77:A3"); //192.168.20.129
		macs.put("19", "00:13:21:C9:88:25"); //192.168.20.21
		macs.put("22", "00:13:21:C9:7C:35"); //192.168.20.56
		macs.put("42", "00:13:21:C9:88:1B"); //192.168.20.20
		macs.put("44", "00:13:21:C9:7B:AF"); //192.168.20.48
		macs.put("62", "00:16:76:E2:78:E4"); //192.168.20.127
		macs.put("66", "00:13:21:C9:93:32"); //192.168.20.22
		macs.put("68", "00:13:21:C9:74:87"); //192.168.20.13
		macs.put("69", "00:13:21:C9:7A:AF"); //192.168.20.29
		macs.put("70", "00:13:21:C9:76:2A"); //192.168.20.34
	}
	
	
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
		}catch(NumberFormatException e){
			LOG.error("pauseNode NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("pauseNode NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("pauseNode fail nodeId:"+nodeId, e);
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
		}catch(NumberFormatException e){
			LOG.error("resumeNode NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("resumeNode NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("resumeNode fail nodeId:"+nodeId,e);
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
			logOperate(regUserId,Operatelog.MOD,"NodeId:"+nodeId+" setRealTime "+(Integer.parseInt(isOpen)==1?"open":"close") );
			tx.commit();
			return ACTIONSUCCESS;
		}catch(NumberFormatException e){
			LOG.error("setRealLog NumberFormatException nodeId:"+nodeId+" isOpen:"+isOpen);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("setRealLog NullPointerException nodeId:"+nodeId+" isOpen:"+isOpen);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e)
		{
			LOG.error("setRealLog fail nodeId:"+nodeId+" isOpen:"+isOpen ,e);
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
		}catch(NumberFormatException e){
			LOG.error("getPriority NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("getPriority NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
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
			NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(id);
			nodeMachine.setPri(pri);
			return ACTIONSUCCESS;
		}catch(NumberFormatException e){
			LOG.error("setPriority NumberFormatException nodeId:"+nodeId+" priority:"+priority);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("setPriority NullPointerException nodeId:"+nodeId+" priority:"+priority);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("setPriority fail nodeId:"+nodeId+" priority:"+priority,e);
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
				nodeMachine.cleanRunCommands("Server kill "+nodeId+" 's Commands");
			}
			
			else
			{
				tx = getTransaction();
				Executelog executelog = new  Executelog(null,statusDAO.findById(99),node,"kill command no response.",new Date());
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				exeDAO.save(executelog);
				tx.commit();
				nodeMachine.setPause(true);
				return ACTIONFAILURE+"kill commands error: node not response.";
			}
			
//			nodeMachine.setBusy(false);
			ControlThreadServer.getInstance().notifyResume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("killCommand success");
			return ACTIONSUCCESS;
		}catch(NumberFormatException e){
			LOG.error("killCommand NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("killCommand NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e)
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
		
//		LOG.info("shutdownNode nodeId:"+nodeId +" isReboot:"+isReboot);
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
					message = "shutdown nodId: "+nodeId;
				}
				else{
					message = "shutdown nodeId: "+nodeId+" fail!";
				}
			}
			else if (Integer.parseInt(isReboot)==1) // reboot
			{
				if( nodeMachine.execute(serverMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_RESTART")))){
					exeFlag = true;
					message = "reboot nodId: "+nodeId;
				}
				else{
					message = "reboot nodId: "+nodeId+" fail!";
				}
			}
			else{
				
				return ACTIONFAILURE+"nodeId:"+nodeId+" isReboot:"+isReboot;
			}
			logOperate(regUserId,exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE+" shutdown node error: node note response.";
		}catch(NumberFormatException e){
			LOG.error("shutdownNode NumberFormatException nodeId:"+nodeId+" isReboot:"+isReboot);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("shutdownNode NullPointerException nodeId:"+nodeId+" isReboot:"+isReboot);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("shutdownNode fail. nodeId:"+nodeId+" isReboot:"+isReboot,e);
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
			if( nodeMachine.execute(serverMessages.createSystemPkt(EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_SOFTRESTART")))){
				exeFlag = true;
				message = "soft restart "+nodeId;
			}else{
				message = "soft restart "+nodeId+" fail!";
			}
			logOperate(regUserId,exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE+"soft restart error :node not response.";
		}catch(NumberFormatException e){
			LOG.error("softRestart NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(NullPointerException e){
			LOG.error("softRestart NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
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
		if("0".equals(needFeedBack)){
			feedBackFlag = false;
		}
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			CODE code = EOPCODES.getInstance().get("S_SYSTEM").getSubCode(FLAG);
			if (code == null){
				return ACTIONFAILURE+":"+FLAG+" doesn't exist in head.xml" ;
			}
			else if( nodeMachine.execute(serverMessages.createSystemPkt(code)) || !feedBackFlag ){
				exeFlag = true;
				message = FLAG + " nodeId:"+nodeId;
			}else{
				message = FLAG + " nodeId:"+nodeId+" fail!";
			}
			logOperate(regUserId,exeFlag?Operatelog.MOD:Operatelog.ERROR,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE+"exe systemcommand error: node not response nodeId:"+nodeId;
		}catch(NumberFormatException e){
			LOG.error("exeSystemCommand NumberFormatException nodeId:"+nodeId+" flag:"+FLAG+" needFeedBack:"+needFeedBack);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(NullPointerException e){
			LOG.error("exeSystemCommand NullPointerException nodeId:"+nodeId+" flag:"+FLAG+" needFeedBack:"+needFeedBack);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("exeSystemCommand fail nodeId:"+ nodeId+" flag:"+FLAG+" needFeedBack:"+needFeedBack,e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			closeSession();
		}
		
	}
	
	public String wakeUpNode(String nodeId) {
		
		String mac = macs.get(nodeId);
		if(mac==null) return ACTIONFAILURE+"NodeId:"+nodeId+" hasn't its MAC Address.";
		return wakeUpMac(mac);
	}
	public String wakeUpMac(String MAC){
		try {
			NodeLogServer.getInstance().wakeUp(MAC);
			return ACTIONSUCCESS;
		} catch (Exception e) {
			LOG.error("wakeUp fail MAC:"+MAC,e);
			return ACTIONFAILURE+e.getMessage();
		}
	}
	
	public String delNode(String nodeId,int regUserId){
		LOG.debug("delete node id:"+nodeId);
		Transaction tx = null;
		try{
			tx = getTransaction();
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findById(Integer.parseInt(nodeId));
			if(node == null) return ACTIONFAILURE+"NodeId:"+nodeId+" not exist";
			nodeDAO.delete(node);
			logOperate(regUserId,Operatelog.MOD,"delete nodeId:"+nodeId +" name:"+node.getNodeName()+" ip:"+node.getNodeIp());
			tx.commit();
			LOG.debug("delNode success nodeId:"+nodeId);
			return ACTIONSUCCESS;
		}catch(NumberFormatException e){
			LOG.error("delNode NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("delNode NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
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
	/**
	 * <?xml version="1.0" encoding="UTF-8"?><Cmd cmdModelName="System_Simple" cmdId="1255" questId="135" questName="frames_test2"><Endarg argName="windows" value="shutdown -s -f -t 1" /><Endarg argName="linux" value="shutdown -s -f -t 1" /></Cmd>
	 * @param nodeId
	 * @param command
	 * @param loginUserId
	 * @return
	 */
	public String exeCommand(String nodeId, String command, int loginUserId) {
		Transaction tx = null;
		try{
			NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
			boolean flag = nodeMachine.exeShellCommand(command);
			tx = getTransaction();
			if(flag) logOperate(loginUserId,Operatelog.ADD,"ExeShellCommand success nodeId:"+nodeId +" command :"+command);
			else logOperate(loginUserId,Operatelog.ERROR,"ExeShellCommand fail nodeId:"+nodeId +" command :"+command);
			tx.commit();
			LOG.debug("exeShellCommand finish nodeId:"+nodeId);
			return ACTIONSUCCESS;
		}catch(NumberFormatException e){
			LOG.error("exeShellCommand NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}
		catch(java.lang.NullPointerException e){
			LOG.error("exeShellCommand NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("exeShellCommand fail nodeId:"+nodeId);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String getExeLogList(String nodeId){
		LOG.debug("getExeLog nodeId: "+nodeId);
		try{
			File logFolder = NetRenderLogFactory.getInstance().getExeLogFolder(Integer.parseInt(nodeId));
			if(logFolder.exists()){
				
			}
			return null;
		}catch(NullPointerException e){
			LOG.error("getExeLogList NullPointerException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(NumberFormatException e){
			LOG.error("getExeLogList NumberFormatException nodeId:"+nodeId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("getExeLogList fail ",e);
			return ACTIONFAILURE+e.getMessage();
		}
	}
	
	
}
