package com.webrender.axis.operate;

import java.util.Iterator;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.remote.NodeMachineManager;


public class NodesStateImpl extends BaseOperate{
	private static final Log LOG = LogFactory.getLog(NodesStateImpl.class);
	private NodeUtils nodeUtils = new NodeUtils();
	private XMLOut xmlOut = new XMLOut();
	private NodeDAO nodeDAO = new NodeDAO();
	public String getNodeStatus(String nodeId)
	{
		LOG.debug("getNodeStatus nodeId:"+nodeId);
//		try{
//			if ( ! this.canVisit(7)){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
		
		try {
			Element root = nodeUtils.bean2xmlWithState(nodeDAO.findById(Integer.parseInt(nodeId)) );
			Document doc = new Document(root);
			LOG.debug("getNodeStatus success");
			return xmlOut.outputToString(doc);
		} catch(Exception e){
			LOG.error("getNodeStatus fail nodeId: "+nodeId );
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
//	public String getNodesStatus()
//	{
//		LOG.debug("getNodesStatus()");
////		try{
////			if ( ! this.canVisit(7)){
////				return BaseAxis.RIGHTERROR;
////			}			
////		}catch(Exception e){
////			LOG.error("RightVisit error",e);
////			return BaseAxis.RIGHTERROR;
////		}
//		
//		try{
//			Element root = new Element("Nodes");
//			Document doc = new Document(root);
////			NodeDAO nodeDAO = new NodeDAO();
//			Set<Integer> ids = NodeMachineManager.getInstance().getNodeMachines();
////			Iterator ite_Nodes = nodeDAO.findAll().iterator();
////			while (ite_Nodes.hasNext()) {
////				Node node = (Node) ite_Nodes.next();
////				if(ids.contains(node.getNodeId())){
////					Element ele_Node = nodeUtils.bean2xmlWithState(node);
////					if (ele_Node != null)
////						root.addContent(ele_Node);					
////				}
////			}
//			Node node = null;
//			for(int nodeId : ids){
//				if(NodeMachineManager.getInstance().getNodeMachine(nodeId).isConnect()==true){
//					node = nodeDAO.findById(nodeId);
//					Element ele_Node = nodeUtils.bean2xmlWithState(node);
//					if (ele_Node != null)
//						root.addContent(ele_Node);
//				}
//			}
//			 
//			LOG.debug("getNodesStatus success ");
//			return xmlOut.outputToString(doc);
//		}catch(Exception e)
//		{
//			LOG.error("getNodesStatus fail",e);
//			return ACTIONFAILURE+e.getMessage();
//		}finally
//		{
//			this.closeSession();
//		}
//	}
	public String getNodesStatus(String groupName) {
		LOG.debug("getNodesStatus groupName:"+groupName);
		NodegroupDAO nGDAO = new NodegroupDAO();
		Nodegroup group = nGDAO.findByNodeGroupName(groupName);
		if(group==null){
			return ACTIONFAILURE+" Pool "+groupName+" not exist!";
		}
		else{
			Set<Integer> nodeMachinesIds = NodeMachineManager.getInstance().getNodeMachines();
			
			nodeMachinesIds.retainAll( nodeDAO.getNodeGroupIds(group) );
			Node node = null;
			Element root = new Element("Nodes");
			Document doc = new Document(root);
			for(int nodeId : nodeMachinesIds){
				if(NodeMachineManager.getInstance().getNodeMachine(nodeId).isConnect()==true){
					node = nodeDAO.findById(nodeId);
					Element ele_Node = nodeUtils.bean2xmlWithState(node);
					if (ele_Node != null)
						root.addContent(ele_Node);
				}
			}
			LOG.debug("getNodesStatus success groupName: "+groupName);
//			LOG.info(xmlOut.outputToString(doc));
			return  xmlOut.outputToString(doc);
		}
		
		
	}
	
	
	public String getAllNodes(){
		try{
			LOG.debug("getAllNodes");
			Element root = new Element("Nodes");
			Document doc = new Document(root);
//			NodeDAO nodeDAO = new NodeDAO();
			Iterator ite_AllNodes = nodeDAO.findAll().iterator();
			while(ite_AllNodes.hasNext()){
				Node node = (Node)ite_AllNodes.next();
				root.addContent(nodeUtils.bean2xml(node));
			}
			String result = xmlOut.outputToString(doc);
			LOG.debug("getAllNodes success");
			return result;
		}catch(Exception e)
		{
			LOG.error("getAllNodes fail",e);
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
	
}
