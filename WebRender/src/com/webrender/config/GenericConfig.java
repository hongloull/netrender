package com.webrender.config;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public final class GenericConfig {
	// For animationsp.com
	private String  configPath = GenericConfig.class.getResource("/").getPath();
	// For Lan
//	private static String configPath = "Y:/NetRender/Server";
	
	
	private static GenericConfig instance = new GenericConfig();
//	private static final String CONF_FILE = "config.properties";
	private static final Log LOG = LogFactory.getLog(GenericConfig.class);
//	private Configuration config = null;
	
	private String mainServer = null;
	private String subServer= null;
	private String databaseServer = null;
	private String nodePort = null;
	private String realLogPort = null;
	private String eventLogPort = null;
	
	private GenericConfig() {
		
//		try {
//			Configuration conf = new PropertiesConfiguration(CONF_FILE);
//			
//			CompositeConfiguration cc = new CompositeConfiguration();
//			cc.addConfiguration(conf);
//			config = cc;
//		} catch (ConfigurationException e) {
//			e.printStackTrace();
//		}
		int index = configPath.lastIndexOf("Server");
		if (index!=-1){
			configPath = configPath.substring(0,index);
			configPath = configPath+"config/";
		}
		else{
			configPath = configPath+"config/";
		}
//		LOG.info("ConfigPath :" + configPath);
	}

	/**
	 * 方法：getInstance<br/>. 获得Singlton的一个类实例
	 * 
	 * @return static的类实例
	 */
	public static synchronized GenericConfig getInstance() {
		return instance;
	}


//	
//	
//	public int getStatusPort()
//	{
//		String sport =  config.getString("StatusPort");
//		try{
//			return  Integer.parseInt(sport);			
//		}
//		catch(Exception e)
//		{
//			return 10061;
//		}
//		
//	}
//	
//	public int getCommandPort()
//	{
//		String sport =  config.getString("CommandPort");
//		try{
//			return  Integer.parseInt(sport);			
//		}
//		catch(Exception e)
//		{
//			return 10051;
//		}
//		
//	}
//	
//	public int getServerPort()
//	{
//		String sport =  config.getString("ServerPort");
//		try{
//			return  Integer.parseInt(sport);			
//		}
//		catch(Exception e)
//		{
//			return 10061;
//		}
//	}
	private void readServerConfig(){
		File file = new File( getFile("server.xml") );
		if( file.exists() ){
			SAXBuilder sb =  new SAXBuilder();
			try {
				Document doc = sb.build(file);
				this.mainServer = doc.getRootElement().getAttributeValue("primary");
				this.subServer  = doc.getRootElement().getAttributeValue("slate");
				this.databaseServer  = doc.getRootElement().getAttributeValue("database");
			} catch (JDOMException e) {
				LOG.error("readServerConfig fail", e);
			}	
		}
		else{
			LOG.error("readServerConfig "+file.getAbsolutePath()+" NotExist!");
		}
	}
	
	public String getMainServer()
	{
		if (this.mainServer==null){
			readServerConfig();
		}
		return this.mainServer;
	}
	
	public String getDatabaseServer()
	{
		if (this.databaseServer==null){
			readServerConfig();
		}
		return this.databaseServer;
	}
	
	public String getSubServer()
	{
		if(this.subServer==null){
			readServerConfig();
		}
		return this.subServer;
	}
	
	private void readPortConfig(){
		File file = new File( getFile("port.xml") );
		if( file.exists() ){
			SAXBuilder sb =  new SAXBuilder();
			try {
				Document doc = sb.build(file);
				this.nodePort = doc.getRootElement().getAttributeValue("serverPort");
				this.realLogPort  = doc.getRootElement().getAttributeValue("realLog");
				this.eventLogPort  = doc.getRootElement().getAttributeValue("eventLog");
			} catch (JDOMException e) {
				LOG.error("readPortConfig fail", e);
			}	
		}
		else{
			LOG.error("readPortConfig "+file.getAbsolutePath()+" NotExist!");
		}
	}
	
	public int getNodePort()
	{
		if (this.nodePort==null){
			readPortConfig();
		}
		try{
			int port = Integer.parseInt(this.nodePort);
			return port;			
		}catch(NumberFormatException e){
			LOG.error("nodeport format error",e);
			return 10061;
		}
	}
	
	public int getRealLogPort()
	{
		if (this.realLogPort==null){
			readPortConfig();
		}
		try{
			int port = Integer.parseInt(this.realLogPort);
			return port;			
		}catch(NumberFormatException e){
			LOG.error("realLogPort format error",e);
			return 10062;
		}
	}
	public int getEventLogPort()
	{
		if (this.eventLogPort==null){
			readPortConfig();
		}
		try{
			int port = Integer.parseInt(this.eventLogPort);
			return port;			
		}catch(NumberFormatException e){
			LOG.error("eventLogPort format error",e);
			return 10063;
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
