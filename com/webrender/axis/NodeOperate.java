package com.webrender.axis;

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.operate.BaseOperate;
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
	
	public String pauseNode(String[] nodeIds)
	{

		if ( !this.canVisit(0) && !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.pauseNode(nodeId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
		
	}
	public String resumeNode(String[] nodeIds)
	{

		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.resumeNode(nodeId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String setRealLog(String[] nodeIds,String isOpen){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.setRealLog(nodeId,isOpen,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String getPriority(String nodeId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodeOperateImpl()).getPriority(nodeId);
	}
	public String setPriority(String[] nodeIds,String priority){
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.setPriority(nodeId,priority,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String killCommand(String[] nodeIds)
	{
		
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.killCommand(nodeId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String  shutdownNode(String[] nodeIds ,String isReboot)
	{
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.shutdownNode(nodeId,isReboot,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	public String  softRestart(String[] nodeIds){
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.softRestart(nodeId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	/**
	 * 
	 * @param nodeId 
	 * @param FLAG   SYSTEMCODE
	 * @param needFeedBack  0 notNeed  1 need
	 * @return
	 */
	public String exeSystemCommand(String[] nodeIds,String FLAG,String needFeedBack)
	{
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.exeSystemCommand(nodeId, FLAG, needFeedBack, this.getLoginUserId()); 
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
		
	}
	
	public String delNode(String[] nodeIds){
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.delNode(nodeId,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	
}
