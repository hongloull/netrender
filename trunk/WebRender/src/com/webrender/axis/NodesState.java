package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.NodesStateImpl;


public class NodesState extends BaseAxis{
	private static final Log LOG = LogFactory.getLog(NodesState.class);
	public String getNodeStatus(String nodeId)
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new NodesStateImpl()).getNodeStatus(nodeId);
	}
	
	public String getNodesStatus(String groupName){
		int regUserId = this.getLoginUserId();
		if ( regUserId == 0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodesStateImpl()).getNodesStatus(groupName,regUserId,canVisit(0));
	}
	public String getAllNodes(){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodesStateImpl()).getAllNodes();
	}
}
