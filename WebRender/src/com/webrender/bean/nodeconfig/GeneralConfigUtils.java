package com.webrender.bean.nodeconfig;

import org.jdom.Element;
public class GeneralConfigUtils {
	private static final String GENERAL = "general";
	private static final String PRIORITY = "priority";
	public GeneralConfig xml2bean(Element root){
		String priority = root.getAttributeValue(PRIORITY);
		GeneralConfig config = new GeneralConfig();
		config.setPriority(priority);
		return config;
	}
	
	public Element bean2xml(GeneralConfig config){
		Element root = new Element(GENERAL);
		root.addAttribute(PRIORITY,config.getPriority()+"");
		return root;
	}
}
