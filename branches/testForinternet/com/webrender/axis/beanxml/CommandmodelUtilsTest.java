package com.webrender.axis.beanxml;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
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
			assertTrue( xmlOut.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

	@Test
	public void testXml2bean() {
		
	}

}
