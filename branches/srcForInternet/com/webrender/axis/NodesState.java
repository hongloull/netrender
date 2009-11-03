package com.webrender.axis;

import java.util.Iterator;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.axis.operate.NodesStateImpl;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.remote.NodeMachineManager;


public class NodesState extends BaseAxis{
	private static final Log LOG = LogFactory.getLog(NodesState.class);
	public String getNodeStatus(String nodeId)
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new NodesStateImpl()).getNodeStatus(nodeId);
	}
	
	public String getNodesStatus(String groupName){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodesStateImpl()).getNodesStatus(groupName);
	}
	public String getAllNodes(){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new NodesStateImpl()).getAllNodes();
	}
}
