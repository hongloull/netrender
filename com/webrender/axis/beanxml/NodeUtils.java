package com.webrender.axis.beanxml;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.remote.NodeMachine.NodeStatus;

public final class NodeUtils {
	private static final Log LOG = LogFactory.getLog(NodeUtils.class);
	
	public static Element bean2xml(Node node)
	{
		Element root = new Element("Node");
		root.addAttribute("nodeId",node.getNodeId().toString());
		if(node.getNodeName()!=null) root.addAttribute("nodeName",node.getNodeName());
		if(node.getNodeIp()!=null) root.addAttribute("nodeIp",node.getNodeIp()+"");
		if (node.getCoreNum()!=null) root.addAttribute("coreNum",node.getCoreNum().toString());
	//	if (node.getStatus()!=null) root.addAttribute("status",node.getStatus().getValue() );
	//	if (node.getOs()!=null)root.addAttribute("plantform",node.getOs());
		
		return root;
	}

	public static Node xml2bean(Element element) {
		Node node = null;
		String nodeId = element.getAttributeValue("nodeId");
		String nodeName = element.getAttributeValue("nodeName");
		String nodeIp = element.getAttributeValue("nodeIp");
		NodeDAO dao = new NodeDAO();
		if (nodeId != null)
		{
			node = dao.findById(Integer.parseInt(nodeId));
		}
		if (node==null)
		{
			node = new Node();			
		}
		if (nodeName!=null)node.setNodeName(nodeName);
		if (nodeIp!=null)  node.setNodeIp(nodeIp);
		return node;
	}
	
	public static Element bean2xmlWithState(Node node)
	{
		LOG.debug("bean2xml_State nodeId: "+node.getNodeId());
		Element root = NodeUtils.bean2xml(node);
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeId());
		if (nodeMachine.isConnect())
		{
			if (nodeMachine.isBusy())
			{
				root.addAttribute("status","InProgress");
			}
			else if (nodeMachine.isPause())
			{
				root.addAttribute("status","Pause");
			}
			else {
				root.addAttribute("status","Idle");
			}
			NodeStatus nodeStatus = nodeMachine.getStatus();
			root.addAttribute("cpu",nodeStatus.getCpuUsage()+"");
			root.addAttribute("ramUsage", nodeStatus.getRamUsage()+"");
			root.addAttribute("frames", "null");
			root.addAttribute("priority","1");
			root.addAttribute("procNum","1");
			root.addAttribute("platform", nodeStatus.getPlatform()+"");
			root.addAttribute("jobName",nodeStatus.getJobName()+"");
			root.addAttribute("realTime",nodeMachine.isRealTime()?"Enable":"Disable");	
			LOG.debug("bean2xml_State success nodeId: "+node.getNodeId());
			return root;
		}
		else{
			LOG.debug("bean2xml_State disconnect nodeId: "+node.getNodeId());
			return null;
		}
	}

}
