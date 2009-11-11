package com.webrender.axis.beanxml;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Quest;

public class TestNodes {
	@Test
	public void testBean2XML()
	{
		Element root = new Element("Nodes");
		NodeDAO nodeDAO = new NodeDAO();
		Iterator ite_nodes =  nodeDAO.findAll().iterator();
		while( ite_nodes.hasNext())
		{
			Node node = (Node)ite_nodes.next();
			Element ele_node = (new NodeUtils()).bean2xml(node);
			root.addContent(ele_node);
		}
		Document doc = new Document(root);
		System.out.println( (new XMLOut()).outputToString(doc) );
	}
}
