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

import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;

/**
 * @author asp
 *
 */
public class QuestUtilsTest {
	private QuestUtils utils = null;
	private QuestDAO dao = null;
	private XMLOut out = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		utils = new QuestUtils();
		dao = new QuestDAO();
		out = new XMLOut();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.QuestUtils#bean2xml(com.webrender.dao.Quest)}.
	 */
	@Test
	public void testBean2xml() {
		List<Quest> list = dao.findAll();
		for(Quest quest : list){
			Element element = utils.bean2xml(quest);
			Document doc = new Document(element);
//			System.out.println( out.outputToString(doc) );
			assertTrue( out.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.QuestUtils#xml2bean(org.jdom.Element)}.
	 * @throws JDOMException 
	 */
	@Test
	public void testXml2bean() throws JDOMException {
		String xml = "<Quest questName=\"1_TEST\" pri=\"20\" regName=\"admin\" maxNodes=\"50\" commitTime=\"2009-12-11  21:08:45\" packetSize=\"2\" Nodes=\"All\" />";
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		assertTrue( utils.xml2bean(root).getQuestId()==null);
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.QuestUtils#bean2xmlWithState(com.webrender.dao.Quest)}.
	 */
	@Test
	public void testBean2xmlWithState() {
		List<Quest> list = dao.findAll();
		for(Quest quest : list){
			Element element = utils.bean2xmlWithState(quest);
			Document doc = new Document(element);
//			System.out.println( out.outputToString(doc) );
			assertTrue( out.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

}
