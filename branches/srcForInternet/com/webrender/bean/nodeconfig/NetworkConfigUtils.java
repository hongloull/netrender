package com.webrender.bean.nodeconfig;

import org.jdom.Element;

public class NetworkConfigUtils {
	private static final String NETWORK = "network";
	private static final String SERVERPORT = "serverPort";
	
	public NetworkConfig xml2bean(Element root){
		NetworkConfig config = new NetworkConfig();
		config.setServerport(root.getAttributeValue(SERVERPORT));
		return config;
	}
	
	public Element bean2xml(NetworkConfig config ){
		Element root = new Element(NETWORK);
		root.addAttribute(SERVERPORT,config.getServerport()+"");
		return root;
	}
}
