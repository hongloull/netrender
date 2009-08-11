package com.webrender.config;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public final class GenericConfig {
//	private String  configPath = GenericConfig.class.getResource("/").getPath();
	private static String configPath = "Y:/NetRender/Server";
	private static GenericConfig instance = new GenericConfig();
	private static final String CONF_FILE = "config.properties";
	private static final Log LOG = LogFactory.getLog(GenericConfig.class);
	private Configuration config = null;

	
	private GenericConfig() {
		
		try {
			Configuration conf = new PropertiesConfiguration(CONF_FILE);
			
			CompositeConfiguration cc = new CompositeConfiguration();
			cc.addConfiguration(conf);
			config = cc;
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		int index = configPath.indexOf("NetRender/Server");
		if (index!=-1){
			configPath = configPath.substring(0,index);
			configPath = configPath+"NetRender/config/";
		}
		else{
			configPath = configPath+"config/";
		}
	}

	/**
	 * 方法：getInstance<br/>. 获得Singlton的一个类实例
	 * 
	 * @return static的类实例
	 */
	public static final GenericConfig getInstance() {
		return instance;
	}


	
	
	public int getStatusPort()
	{
		String sport =  config.getString("StatusPort");
		try{
			return  Integer.parseInt(sport);			
		}
		catch(Exception e)
		{
			return 10061;
		}
		
	}
	
	public int getCommandPort()
	{
		String sport =  config.getString("CommandPort");
		try{
			return  Integer.parseInt(sport);			
		}
		catch(Exception e)
		{
			return 10051;
		}
		
	}
	
	public int getServerPort()
	{
		String sport =  config.getString("ServerPort");
		try{
			return  Integer.parseInt(sport);			
		}
		catch(Exception e)
		{
			return 10061;
		}
	}
	
	public String getMainServer()
	{
		LOG.debug("getMainServer");
		File file = new File( getFile("server.xml") );
		if( file.exists() ){
			String address = "";
			SAXBuilder sb =  new SAXBuilder();
			try {
				Document doc = sb.build(file);
				address = doc.getRootElement().getAttributeValue("primary");
			} catch (JDOMException e) {
				LOG.error("getMainServer fail", e);
				e.printStackTrace();
				return null;
			}
			LOG.debug("getMainServer success");
			return address;				
		}
		else{
			LOG.error("getMainServer readFile NotExist return localHostIP");
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public String getDatabaseServer()
	{
		LOG.debug("getDatabaseServer");
		File file = new File( getFile("server.xml") );
		if( file.exists() ){
			String address = "";
			SAXBuilder sb =  new SAXBuilder();
			try {
				Document doc = sb.build(file);
				address = doc.getRootElement().getAttributeValue("database");
			} catch (JDOMException e) {
				LOG.error("getDatabaseServer fail",e);
				e.printStackTrace();
				return null;
			}
			LOG.debug("getDatabaseServer success");
			return address;				
		}
		else{
			LOG.error("getDatabaseServer readFile NotExist return localHostIP");
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public String getSubServer()
	{
		LOG.debug("getSubServer");
		File file = new File( getFile("server.xml") );
		if( file.exists() ){
			String address = "";
			SAXBuilder sb =  new SAXBuilder();
			try {
				Document doc = sb.build(file);
				address = doc.getRootElement().getAttributeValue("slate");
			} catch (JDOMException e) {
				LOG.error("getSubServer fail",e);
				e.printStackTrace();
				return null;
			}
			LOG.debug("getSubServer success");
			return address;				
		}
		else{
			LOG.error("getSubServer readFile NotExist return localHostIP");
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public String getFile(String path)
	{
		try {
			String file = URLDecoder.decode(configPath+path,"utf-8");
			return file;			
			
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
			return null;
		}
	}
//	public Configuration getConfig() {
//		return config;	
//	}

}
