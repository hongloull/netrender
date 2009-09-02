package com.webrender.config;

import java.io.File;
import java.io.IOException;

public class XMLConfigFactory {

	public static XMLConfig getXMLConfig(File file) throws IOException, ClassNotFoundException {
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".xml");
		if (index == -1){
			throw new java.io.IOException("Not XML");
		}
		String pojoName = file.getParentFile().getName();
		if(pojoName.equalsIgnoreCase("templates")){
			XMLConfig instance=new CommandModelXMLConfig();
			return instance;
		}
		if(pojoName.equalsIgnoreCase("nodes")){
			XMLConfig instance = new NodeXMLConfig();
			return instance;
		}
		if(pojoName.equalsIgnoreCase("users")){
			XMLConfig instance = new UserXMLConfig();
			return instance;
		}
		if(pojoName.equalsIgnoreCase("right")){
			if(fileName.equalsIgnoreCase("right.xml")){
				XMLConfig instance = new RightXMLConfig();				
				return instance;
			}
		}
//		if(pojoName.equalsIgnoreCase("protocol")){
//			if(fileName.equalsIgnoreCase("head.xml")){
//				XMLConfig instance = new HeadXMLConfig();
//				return instance;
//			}
//		}
		
		return null;
	}
	
}
