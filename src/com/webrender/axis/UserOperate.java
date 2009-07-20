package com.webrender.axis;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;



public class UserOperate extends BaseAxis {
	private static final Log log = LogFactory.getLog(UserOperate.class);
	public String getUserConfig(String userName){
		log.debug("getUserConfig");
		
		String userFile = GenericConfig.getInstance().getFile("users/"+userName+".xml");
		if(userFile == null) return "UserNameNotExistError";
		

//		if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
		File file = new File(userFile);
		SAXBuilder sb =  new SAXBuilder();
		Document document = null;
		try {
			document = sb.build(file);
			return XMLOut.outputToString(document);
		} catch (Exception e) {
			log.error("getUserConfig fail userName: "+userName, e);
			return null;
		}
	}

}
