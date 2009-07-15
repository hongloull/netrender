package com.webrender.axis.beanxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;


public class ReguserUtils {
	
	private static final Log log = LogFactory.getLog(ReguserUtils.class);
	
	public static Reguser xml2Bean(Element element){
		log.debug("xml2Bean");
		try{
			String name = element.getAttributeValue("name");
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser reguser = regUserDAO.findByRegName(name);
			if(reguser == null){
				reguser = new Reguser(name,"");
			}
			log.debug("xml2Bean success");
			return reguser;			
		}catch(Exception e){
			log.error("xml2Bean fail", e);
			return null;
		}
	}
	
	public static Element bean2Xml(Reguser regUser){
		log.debug("bean2Xml");
		try{
			Element element = new Element("User");
			
			return element;
		}catch(Exception e){
			log.error("bean2Xml fail",e);
			return null;
		}
	}
}
