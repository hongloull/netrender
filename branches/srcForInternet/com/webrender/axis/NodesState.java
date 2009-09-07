package com.webrender.axis;

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


public class NodesState extends BaseAxis{
	private static final Log LOG = LogFactory.getLog(NodesState.class);
	public String getNodeStatus(String nodeId)
	{
		LOG.debug("getNodeStatus nodeId:"+nodeId);
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		
		try {
			NodeDAO nodeDAO = new NodeDAO();
			Element root = NodeUtils.bean2xmlWithState(nodeDAO.findById(Integer.parseInt(nodeId)) );
			Document doc = new Document(root);
			LOG.debug("getNodeStatus success");
			return XMLOut.outputToString(doc);
		} catch(Exception e){
			LOG.error("getNodeStatus fail nodeId: "+nodeId );
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
	}
	public String getNodesStatus()
	{
		LOG.debug("get AllNodes Status begin");
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		
		try{
			Element root = new Element("Nodes");
			Document doc = new Document(root);
			NodeDAO nodeDAO = new NodeDAO();
			Iterator ite_Nodes = nodeDAO.findAll().iterator();
			Set<Integer> ids = NodeMachineManager.getNodeMachines();
			while (ite_Nodes.hasNext()) {
				Node node = (Node) ite_Nodes.next();
				if(ids.contains(node.getNodeId())){
					Element ele_Node = NodeUtils.bean2xmlWithState(node);
					if (ele_Node != null)
						root.addContent(ele_Node);					
				}
			}
			String result = XMLOut.outputToString(doc);
			LOG.debug("get Nodes Status success "+result);
			return result;
		}catch(Exception e)
		{
			LOG.error("get all nodes status fail",e);
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
	}
}
