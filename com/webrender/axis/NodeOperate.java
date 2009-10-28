package com.webrender.axis;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.operate.NodeOperateImpl;
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

public class NodeOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(NodeOperate.class);
	
	public String pauseNode(String nodeId)
	{

		if ( !this.canVisit(0) && !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).pauseNode(nodeId,this.getLoginUserId());
	}
	public String resumeNode(String nodeId)
	{

		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).resumeNode(nodeId,this.getLoginUserId());
	}
	public String setRealLog(String nodeId,String isOpen){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodeOperateImpl()).setRealLog(nodeId,isOpen,this.getLoginUserId());
	}
	public String getPriority(String nodeId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodeOperateImpl()).getPriority(nodeId);
	}
	public String setPriority(String nodeId,String priority){
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).setPriority(nodeId, priority, this.getLoginUserId());
	}
	
	public String killCommand(String nodeId)
	{
		
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).killCommand(nodeId, this.getLoginUserId());
	}
	public String  shutdownNode(String nodeId ,String isReboot)
	{
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).shutdownNode(nodeId, isReboot, this.getLoginUserId());
	}
	public String  softRestart(String nodeId){
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).softRestart(nodeId,this.getLoginUserId());
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
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).exeSystemCommand(nodeId, FLAG, needFeedBack, this.getLoginUserId()); 
		
	}
	
	public String delNode(String nodeId){
		
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		return (new NodeOperateImpl()).delNode(nodeId,this.getLoginUserId());
	}
	
	
}
