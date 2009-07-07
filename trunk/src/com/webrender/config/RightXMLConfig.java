package com.webrender.config;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.webrender.axis.beanxml.RightUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Right;
import com.webrender.dao.RightDAO;

public class RightXMLConfig extends XMLConfig {
	private static final Log log = LogFactory.getLog(RightXMLConfig.class);
	@Override
	public void deleteExtraData() {
	}

	@Override
	public void loadFromXML(File file) throws JDOMException {
		log.debug("loadFromXML");
		if(file.canWrite()){
			log.info(file.getAbsoluteFile()+": canWrite");
			RightDAO rightDAO = new RightDAO();
			Iterator ite_Rights = rightDAO.findAll().iterator();
			Element root = new Element("Rights");
			Document doc = new Document(root);
			while (ite_Rights.hasNext()){
				Element element = RightUtils.bean2xml(   (Right)ite_Rights.next() ) ;
				root.addContent(element);
			}
			XMLOut.outputToFile(doc,file);
			log.debug("loadFromXML success");
		}
		else  log.error(file.getAbsoluteFile()+": cannot Write");
		
	}

}
