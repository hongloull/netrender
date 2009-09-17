package com.webrender.config;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Timegroup;
import com.webrender.dao.TimegroupDAO;

public class NodeXMLConfig extends XMLConfig {
	private static List lisNGs = null;
	private static final Log LOG = LogFactory.getLog(NodeXMLConfig.class);
	static {
		NodegroupDAO nodeGroupDAO = new  NodegroupDAO();
		lisNGs  = nodeGroupDAO.findAll();
		Nodegroup all = nodeGroupDAO.findByNodeGroupName("All");
		if (all!=null) lisNGs.remove(all);
	}
	@Override
	public void loadFromXML(File file) throws JDOMException {
		LOG.debug("loadFromXML");
		int index = file.getName().lastIndexOf(".xml");
		if (index == -1){
			return;
		}
		SAXBuilder sb =  new SAXBuilder();
		Document doc = sb.build(file);
		String nodeGroupName = file.getName().substring(0, index);
		if(nodeGroupName.equalsIgnoreCase("All")){
			file.delete();
			return;
		}
		NodegroupDAO nodeGroupDAO = new NodegroupDAO();
		Nodegroup nodeGroup = nodeGroupDAO.findByNodeGroupName(nodeGroupName);
		NodeDAO nodeDAO 	= new NodeDAO();
		TimegroupDAO tGroupDAO = new TimegroupDAO();
		Element root = doc.getRootElement();
		Transaction tx = null;
		try{
			tx = getTransaction();
			
			if(nodeGroup==null){
				nodeGroup = new Nodegroup();
				nodeGroup.setNodeGroupName(nodeGroupName);
			}
			String timeGroupName = root.getAttributeValue("timeGroup");
			Timegroup tGroup = tGroupDAO.findByTimeGroupName(timeGroupName);
			if (tGroup!=null){
				nodeGroup.setTimegroup(tGroup);
			}
			nodeGroupDAO.save(nodeGroup);
			
			Set set_Nodes = nodeGroup.getNodes();
			HashSet<Node> set_RetainNodes = new HashSet<Node>();
			Iterator ite_Nodes = root.getChildren().iterator();
			
			
			while(ite_Nodes.hasNext())
			{
				Element element = (Element)ite_Nodes.next();
				Node node = NodeUtils.xml2bean(element);
				nodeDAO.save(node);
				set_RetainNodes.add(node);
				root.removeChild(element);
				root.addContent(NodeUtils.bean2xml(node));
				if( !(set_Nodes.contains(node))){
				//	System.out.println("set_Nodes containsnot node");
					set_Nodes.add(node);
					node.getNodegroups().add(nodeGroup);
				}
			}
			set_Nodes.retainAll(set_RetainNodes);
			
			tx.commit();
			lisNGs.remove(nodeGroup);
			XMLOut.outputToFile(doc,file);
			LOG.debug("loadFromXML success");
		}
		catch(Exception e)
		{
			LOG.error("loadFromXML fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}
	}
	
	public void deleteExtraData(){
		Transaction tx = null;
		LOG.debug("deleteExtraNodeGroup");
		try{
			tx = getTransaction();
			Iterator ite_NGs = lisNGs.iterator();
			NodegroupDAO nGDAO = new NodegroupDAO();
			while(ite_NGs.hasNext()){
				nGDAO.delete( (Nodegroup)ite_NGs.next() );
			}
			tx.commit();
			LOG.debug("deleteExtraNodeGroup success");
		}catch(Exception e){
			LOG.error("deleteExtraNodeGroup fail", e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}
	}

}
