package com.webrender.test;


public class StringIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String configPath = "D:/Server/net/Server/tomcat/webapps/WebRender/web-inf/classes/";
		int index = configPath.lastIndexOf("Server");
		if (index!=-1){
			configPath = configPath.substring(0,index);
			configPath = configPath+"config/";
		}else{
			configPath = configPath+"config/";
		}
		System.out.println(configPath);
	}
}
