package com.webrender.axis.operate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.StatusDAO;
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
	public String getNodesStatus(String groupName,int regUserId,boolean isAdmin) {
		LOG.debug("getNodesStatus groupName:"+groupName);
		NodegroupDAO nGDAO = new NodegroupDAO();
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findById(regUserId);
		Nodegroup group = nGDAO.findByNodeGroupName(groupName);
		if(group==null){
			return ACTIONFAILURE+" Pool "+groupName+" not exist!";
		}else if(regUser == null){
			return ACTIONFAILURE+" Reguser "+regUserId+" not exist!";
		}
		else{
			Set<Integer> nodeMachinesIds = NodeMachineManager.getInstance().getNodeMachines();
//			List<Integer> listGroupIds = null ; 
			if( isAdmin || regUser.getNodegroups().contains(group)){
//				listGroupIds = nodeDAO.getNodeGroupIds(group);
				nodeMachinesIds.retainAll( nodeDAO.getNodeGroupIds(group) );
			}else{
				Set<Integer> set_GroupsIds = new HashSet<Integer>();
				Iterator<Nodegroup> ite_Groups = regUser.getNodegroups().iterator();
				while(ite_Groups.hasNext()){
					set_GroupsIds.addAll( nodeDAO.getNodeGroupIds( ite_Groups.next() ) );
				}
				nodeMachinesIds.retainAll( set_GroupsIds);
			}
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
		
//		StatusDAO statusDAO = new StatusDAO();
//		Transaction tx = null;
//		try {
//			tx = getSession().beginTransaction();
//			statusDAO.updateSystemVersion();
//			tx.commit();					
//		}catch (Exception e){					
//			LOG.error("UpdateSystemVersion Error", e);
//			if(tx!=null){
//				tx.rollback();
//			}
//		}finally{		
//		}
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
//			LOG.info("getAllNodes success:"+result);
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
