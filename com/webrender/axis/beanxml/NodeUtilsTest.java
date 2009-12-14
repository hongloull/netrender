/**
 * 
 */
package com.webrender.axis.beanxml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

/**
 * @author asp
 *
 */
public class NodeUtilsTest {
	private NodeUtils utils = null;
//	private Node node = null;
	private NodeDAO dao = null;
	private NodeMachine nodeMachine = null;
	private XMLOut out = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		utils = new NodeUtils();
		dao = new NodeDAO();
		out = new XMLOut();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.NodeUtils#bean2xml(com.webrender.dao.Node)}.
	 */
	@Test
	public void testBean2xml() {
		List<Node> list = dao.findAll();
		for(Node node : list){
			Element root = new Element("TEST");
			Document doc = new Document(root);
			root.addContent(utils.bean2xml(node));
			System.out.println( out.outputToString(doc) );
			assertTrue( out.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.NodeUtils#xml2bean(org.jdom.Element)}.
	 * @throws JDOMException 
	 */
	@Test
	public void testXml2bean() throws JDOMException {
		String xml = "<TEST>" +
				"<Node nodeId=\"1\" nodeName=\"publicNoteBook\" nodeIp=\"127.0.0.1\" priority=\"5\" />" +
				"</TEST>";
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		List<Element> lis_args = root.getChildren("Node");
		for (Element element :lis_args){
			assertTrue(utils.xml2bean(element)!=null);
		}
		
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.NodeUtils#bean2xmlWithState(com.webrender.dao.Node)}.
	 */
	@Test
	public void testBean2xmlWithState() {
		List<Node> list = dao.findAll();
		for(Node node :list){
			NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(node.getNodeId());
			nodeMachine.setConnect(true);
			Element root = new Element("TEST");
			Document doc = new Document(root);
			root.addContent(utils.bean2xmlWithState(node));
//			System.out.println( out.outputToString(doc) );
			assertTrue( out.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

}
