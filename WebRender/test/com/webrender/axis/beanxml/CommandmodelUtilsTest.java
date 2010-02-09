package com.webrender.axis.beanxml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;

public class CommandmodelUtilsTest {
	private CommandmodelDAO dao = null;
	private CommandmodelUtils utils = null;
	private Commandmodel cm = null;
	private XMLOut xmlOut = null;
	@Before
	public void setUp() throws Exception {
		dao = new CommandmodelDAO();
		utils = new CommandmodelUtils();
		xmlOut = new XMLOut();		
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
		utils = null;
	}

	@Test
	public void testBean2xml() {
		Iterator ite = dao.findAll().iterator();
		while(ite.hasNext()){
			cm = (Commandmodel) ite.next();
			Element root = new Element("Test");
			Document doc = new Document(root);
			root.addContent( utils.bean2xml(cm) );
//			System.out.println( xmlOut.outputToString(doc) );
			assertTrue( xmlOut.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

	@Test
	public void testXml2bean() throws Exception {
		String xml = "<Test>" +
				"<Commandmodel commandModelName=\"Maya_Software\" type=\"render\" />" +
				"</Test>";
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		List<Element> lis_args = root.getChildren("Commandmodel");
		for (Element element :lis_args){
			assertTrue(utils.xml2bean(element)!=null);
		}
	}
	@Test
	public void testXml2beanNull() throws Exception{
		String xml = "<Test>" +
		"<Commandmodel  type=\"render\" />" +
		"</Test>";
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		List<Element> lis_args = root.getChildren("Commandmodel");
		for (Element element :lis_args){
			assertTrue(utils.xml2bean(element)==null);
		}
	}

}
