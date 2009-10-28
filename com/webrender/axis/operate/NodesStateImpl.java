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
import com.webrender.remote.NodeMachineManager;


public class NodesStateImpl extends BaseOperate{
	private static final Log LOG = LogFactory.getLog(NodesStateImpl.class);
	private NodeUtils nodeUtils = new NodeUtils();
	private XMLOut xmlOut = new XMLOut();
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
			NodeDAO nodeDAO = new NodeDAO();
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
	public String getNodesStatus()
	{
		LOG.debug("getNodesStatus");
//		try{
//			if ( ! this.canVisit(7)){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
		
		try{
			Element root = new Element("Nodes");
			Document doc = new Document(root);
			NodeDAO nodeDAO = new NodeDAO();
			Iterator ite_Nodes = nodeDAO.findAll().iterator();
			Set<Integer> ids = NodeMachineManager.getInstance().getNodeMachines();
			while (ite_Nodes.hasNext()) {
				Node node = (Node) ite_Nodes.next();
				if(ids.contains(node.getNodeId())){
					Element ele_Node = nodeUtils.bean2xmlWithState(node);
					if (ele_Node != null)
						root.addContent(ele_Node);					
				}
			}
			String result = xmlOut.outputToString(doc);
			LOG.debug("getNodesStatus success ");
			return result;
		}catch(Exception e)
		{
			LOG.error("getNodesStatus fail",e);
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
	
	public String getAllNodes(){
		try{
			LOG.debug("getAllNodes");
			Element root = new Element("Nodes");
			Document doc = new Document(root);
			NodeDAO nodeDAO = new NodeDAO();
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
