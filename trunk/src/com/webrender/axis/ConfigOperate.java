package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;

public class ConfigOperate extends BaseAxis {
	private static final Log log = LogFactory.getLog(ConfigOperate.class);
	public String getPathConfig(){
		try{
		String mapDir = GenericConfig.getInstance().getFile("mapDir.xml");
		File file = new File(mapDir);
		if(!file.exists()) return "FileNotExistError";
		SAXBuilder sb =  new SAXBuilder();
		Document doc = null;
			doc = sb.build(file);
			return XMLOut.outputToString(doc);
		}catch(JDOMException e){
			return "XMLParseError";
		}
		catch(Exception e){
			return BaseAxis.ActionFailure;
		}finally{
		}
	}
	
	public String setPathConfig(String questXML){

		try {
			String mapDir = GenericConfig.getInstance().getFile("mapDir.xml");
			File file = new File(mapDir);
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML
					.getBytes());
			Document doc = builder.build(inputStream);
			XMLOut.outputToFile(doc, file);
			return BaseAxis.ActionSuccess;
		} catch (JDOMException e) {
			return "XMLParseError";
		}catch(Exception e){
			return BaseAxis.ActionFailure;
		}
	}
	
	
}
