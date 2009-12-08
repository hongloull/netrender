/**
 * 
 */
package com.webrender.axis.beanxml;


import static org.junit.Assert.assertTrue;

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

import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;

/**
 * @author WAEN
 *
 */
public class CommandmodelargUtilsTest {
	private  CommandmodelargDAO dao = null;
	private Commandmodelarg  arg = null;
	private XMLOut xmlOut = null;
	private CommandmodelargUtils utils = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dao = new CommandmodelargDAO();
		xmlOut = new XMLOut();
		utils = new CommandmodelargUtils();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testbean2xml(){
		Iterator ite = dao.findAll().iterator();
		while(ite.hasNext()){
			arg = (Commandmodelarg) ite.next();
			Element root = new Element("Test");
			Document doc = new Document(root);
			root.addContent(utils.bean2xml(arg));
			System.out.println( xmlOut.outputToString(doc) );
			assertTrue( xmlOut.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}
	@Test
	public void testxml2bean() throws Exception{
		SAXBuilder builder = new SAXBuilder();
		String xml ="<Test>" +
				"<Commandmodelarg argName=\"-r\" type=\"0\" order=\"100\" argInstruction=\"EngineType\" />" +
				"<Commandmodelarg argName=\"\" type=\"1\" order=\"0\" argInstruction=\"frames\" />" +
				"<Commandmodelarg argName=\"-r\" type=\"0\" order=\"100\" argInstruction=\"12\" />" +
				"</Test>";
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		List<Element> lis_args = root.getChildren("Commandmodelarg");
		for (Element element :lis_args){
			assertTrue(utils.xml2bean(element, null)!=null);
		}
	}
	@Test
	public void testxml2beanNull() throws Exception{
		SAXBuilder builder = new SAXBuilder();
		String xml ="<Test>" +
				"<Commandmodelarg argName=\"-r\" type=\"0\" order=\"100\"  />" +
				"</Test>";
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		List<Element> lis_args = root.getChildren("Commandmodelarg");
		for (Element element :lis_args){
			assertTrue(utils.xml2bean(element, null)==null);
		}		
	}
}
