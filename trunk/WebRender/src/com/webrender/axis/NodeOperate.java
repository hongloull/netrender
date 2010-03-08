package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.BaseOperate;
import com.webrender.axis.operate.NodeOperateImpl;

public class NodeOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(NodeOperate.class);
	
	public String pauseNode(String nodeId){
		String[] nodeIds = {nodeId};
		return pauseNode(nodeIds);
	}
	public String resumeNode(String nodeId){
		String[] nodeIds = {nodeId};
		return resumeNode(nodeIds);
	}
	public String setRealLog(String nodeId,String isOpen){
		String[] nodeIds = {nodeId};
		return setRealLog(nodeIds,isOpen);
	}
	public String setPriority(String nodeId,String priority){
		String[] nodeIds = {nodeId};
		return setPriority(nodeIds,priority);
	}
	public String killCommand(String nodeId){
		String[] nodeIds = {nodeId};
		return killCommand(nodeIds);
	}
	public String  shutdownNode(String nodeId ,String isReboot){
		String[] nodeIds = {nodeId};
		return shutdownNode( nodeIds ,isReboot);
	}
	public String  softRestart(String nodeId){
		String[] nodeIds = {nodeId};
		return softRestart(nodeIds);
	}
	public String softStop(String nodeId){
		String[] nodeIds = {nodeId};
		return softStop(nodeIds);
	}
	public String exeSystemCommand(String nodeId ,String FLAG,String needFeedBack){
		String[] nodeIds = {nodeId};
		return exeSystemCommand(nodeIds,FLAG,needFeedBack);
	}
	
	
	public String delNode(String nodeId){
		String[] nodeIds = {nodeId};
		return delNode(nodeIds);
	}
	
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
		if (  !this.canVisit(0) &&  !this.canVisit(21) ) return BaseAxis.RIGHTERROR;
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
	
	public String softStop(String[] nodeIds){
		if (  !this.canVisit(0) &&  !this.canVisit(21) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.exeSystemCommand(nodeId, "S_SOFTSTOP", "0", this.getLoginUserId()); 
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
		if (  !this.canVisit(0) &&  !this.canVisit(21) ) return BaseAxis.RIGHTERROR;
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
	
	public String exeCommand(String[] nodeIds , String command){
		if (  !this.canVisit(0) &&  !this.canVisit(21) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.exeCommand(nodeId,command,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String getExeLogList(String nodeId){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		return (new NodeOperateImpl()).getExeLogList(nodeId);
	}
	
	public String wakeUpNode(String[] nodeIds){
		if( !this.canVisit(0)&&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = nodeOperateImpl.wakeUpNode(nodeId);
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
	public String wakeUpMAC(String MAC){
		if( !this.canVisit(0)&&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		NodeOperateImpl nodeOperateImpl = new NodeOperateImpl();
		return nodeOperateImpl.wakeUpMac(MAC);
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
