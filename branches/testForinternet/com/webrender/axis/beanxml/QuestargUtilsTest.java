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

import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;

public class QuestargUtilsTest {
	private QuestargDAO dao = null;
	private QuestargUtils utils = null;
	private XMLOut out = null;
	@Before
	public void setUp() throws Exception {
		dao = new QuestargDAO();
		utils = new QuestargUtils();
		out = new XMLOut();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBean2xml() {
		List<Questarg> list = dao.findAll();
		for(Questarg questarg : list){
			Element element = utils.bean2xml(questarg);
			Document doc = new Document(element);
			System.out.println(out.outputToString(doc));
			assertTrue( out.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

	@Test
	public void testXml2bean() throws JDOMException {
		String xml = "<Questarg commandModelArgId=\"1\" value=\"1-20\" type=\"0\" />";
		SAXBuilder builder = new SAXBuilder();
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		Document doc = builder.build(inputStream);
		Element root = doc.getRootElement();
		assertTrue( utils.xml2bean(root).getQuestArgId()==null);
		
	}



}
