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

public class NodeUtils {
	private static final Log log = LogFactory.getLog(NodeUtils.class);
	
	public static Element bean2xml(Node node)
	{
		Element root = new Element("Node");
		root.addAttribute("nodeId",node.getNodeId().toString());
		if(node.getNodeName()!=null) root.addAttribute("nodeName",node.getNodeName());
		root.addAttribute("nodeIp",node.getNodeIp());
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
			node = dao.findByNodeIp(nodeIp);
			if ( node==null) node = new Node();
			
		}
		if (nodeName!=null)node.setNodeName(nodeName);
		if (nodeIp!=null)  node.setNodeIp(nodeIp);
		return node;
	}
	
	public static Element bean2xml_State(Node node)
	{
		log.debug("bean2xml_State nodeIp: "+node.getNodeIp());
		Element root = NodeUtils.bean2xml(node);
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeIp());
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
			root.addAttribute("questName","null" );
			root.addAttribute("frames", "null");
			root.addAttribute("platform", nodeStatus.getPlatform()+"");
			root.addAttribute("jobName",nodeStatus.getJobName()+"");
			root.addAttribute("realTime",nodeMachine.isRealTime()?"Enable":"Disable");	
			log.debug("bean2xml_State success nodeIp: "+node.getNodeIp());
			return root;
		}
		else{
			log.debug("bean2xml_State disconnect nodeIp: "+node.getNodeIp());
			return null;
		}
	}

}
