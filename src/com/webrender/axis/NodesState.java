package com.webrender.axis;

import java.util.Iterator;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;


public class NodesState extends BaseAxis{
	private static final Log log = LogFactory.getLog(NodesState.class);
	public String getNodeStatus(String nodeIp)
	{
		log.debug("getNodeStatus nodeIp:"+nodeIp);
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		try {
			NodeDAO nodeDAO = new NodeDAO();
			Element root = NodeUtils.bean2xml_State(nodeDAO.findByNodeIp(nodeIp) );
			Document doc = new Document(root);
			log.debug("getNodeStatus success");
			return XMLOut.outputToString(doc);
		} catch(Exception e){
			log.error("getNodeStatus fail",e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
	}
	public String getNodesStatus()
	{
		log.debug("get AllNodes Status begin");
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		try{
			Element root = new Element("Nodes");
			Document doc = new Document(root);
			NodeDAO nodeDAO = new NodeDAO();
			Iterator ite_Nodes = nodeDAO.findAll().iterator();
			while (ite_Nodes.hasNext()) {
				Node node = (Node) ite_Nodes.next();
				Element ele_Node = NodeUtils.bean2xml_State(node);
				
				if (ele_Node != null)
					root.addContent(ele_Node);
			}
			String result = XMLOut.outputToString(doc);
			log.info("get all Nodes Status success "+result);
			return result;
		}catch(Exception e)
		{
			log.error("get all nodes status fail",e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
	}
}
