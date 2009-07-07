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
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;

public class NodeXMLConfig extends XMLConfig {
	private static List lis_NGs = (new NodegroupDAO()).findAll();
	private static final Log log = LogFactory.getLog(NodeXMLConfig.class);
	@Override
	public void loadFromXML(File file) throws JDOMException {
		log.debug("loadFromXML");
		SAXBuilder sb =  new SAXBuilder();
		Document doc = sb.build(file);
		int index = file.getName().lastIndexOf(".xml");
		String nodeGroupName = file.getName().substring(0, index);
		NodegroupDAO nodeGroupDAO = new NodegroupDAO();
		Nodegroup nodeGroup = nodeGroupDAO.findByNodeGroupName(nodeGroupName);
		NodeDAO nodeDAO = new NodeDAO();

		Transaction tx = null;
		try{
			tx = getTransaction();
			
			if(nodeGroup==null){
				nodeGroup = new Nodegroup();
				nodeGroup.setNodeGroupName(nodeGroupName);
				nodeGroupDAO.save(nodeGroup);
			}
			Set set_Nodes = nodeGroup.getNodes();
			HashSet<Node> set_RetainNodes = new HashSet<Node>();
			Element root = doc.getRootElement();
			Iterator ite_Nodes = root.getChildren().iterator();
			
			
			while(ite_Nodes.hasNext())
			{
				Element element = (Element)ite_Nodes.next();
				Node node = NodeUtils.xml2bean(element);
				nodeDAO.save(node);
				set_RetainNodes.add(node);
				if(set_Nodes.contains(node)){
//					System.out.println("set_Nodes  contains node");
				}
				else{
					System.out.println("set_Nodes containsnot node");
					set_Nodes.add(node);
					node.getNodegroups().add(nodeGroup);
				}
			}
			set_Nodes.retainAll(set_RetainNodes);
			
			tx.commit();
			lis_NGs.remove(nodeGroup);
			log.debug("loadFromXML success");
		}
		catch(Exception e)
		{
			log.error("loadFromXML fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}			
		
	}
	
	public void deleteExtraData(){
		Transaction tx = null;
		log.debug("deleteExtraNodeGroup");
		try{
			tx = getTransaction();
			Iterator ite_NGs = lis_NGs.iterator();
			NodegroupDAO nGDAO = new NodegroupDAO();
			while(ite_NGs.hasNext()){
				nGDAO.delete( (Nodegroup)ite_NGs.next() );
			}
			tx.commit();
			log.debug("deleteExtraNodeGroup success");
			
		}catch(Exception e){
			log.error("deleteExtraNodeGroup fail", e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}
	}

}
