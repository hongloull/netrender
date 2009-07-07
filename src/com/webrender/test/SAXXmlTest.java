package com.webrender.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SAXXmlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SAXBuilder builder=new SAXBuilder(false);
		try {
			String xml = "<ResultSet Status=\"未登录\" />";
			InputStream   inputStream   =   new   ByteArrayInputStream(xml.getBytes());
			System.out.println(xml);
			File file = new File("E:\\workspace\\WebRender\\src\\com\\webrender\\axis\\bean\\ClientQuest.xml");
			Document document = builder.build(file);
			Element root  = document.getRootElement();
			Element model = root.getChild("Model");
			System.out.println( root.getAttributeValue("Status") );
		} catch (JDOMException e) {
			
			e.printStackTrace();
		}
	}

}
