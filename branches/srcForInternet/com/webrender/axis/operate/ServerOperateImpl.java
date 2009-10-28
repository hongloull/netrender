package com.webrender.axis.operate;


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
import com.webrender.dao.Operatelog;

public class ServerOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(ServerOperateImpl.class);
	private XMLOut xmlOut = new XMLOut();
	public String restartServer(int regUserId){
		LOG.debug("restart Server");
		
		try {
						
			String path = ServerOperateImpl.class.getResource("/").getPath();
			int index = path.indexOf("webapps");
			String tomcatHome =  path.substring(0,index) ;
			Process process = Runtime.getRuntime().exec(tomcatHome+"bin/restart.bat");
			logOperate(regUserId,Operatelog.MOD,"restart success");
			return  ACTIONSUCCESS;
			
			
//			
//			BufferedReader in = new BufferedReader(  
//					new InputStreamReader(process.getInputStream()));  
//			StringBuffer  result = new StringBuffer();
//			String line = null;
//			while ((line = in.readLine()) != null) {  
//				LOG.info(line);
//				logOperate(regUserId,Operatelog.MOD,"restart console:"+line);
//				result.append(line).append("\n");
//			}  
//			LOG.info("restart Server finish");
//			return result.toString();
		} catch (Exception e) {
			LOG.error("restartServer failed",e);
//			return  ACTIONFAILURE+e.getMessage();
			return  ACTIONFAILURE+e.getMessage();
		}   
	}
	public String getPortsConfig() {
		LOG.debug("getPortsConfig");
		File file = new File(GenericConfig.getInstance().getFile("port.xml") );
		if( file.exists() ){
			SAXBuilder sb =  new SAXBuilder();
			try {
				Document doc = sb.build(file);
				LOG.debug("getPortsConfig success");
				return xmlOut.outputToString(doc);
			} catch (JDOMException e) {
				LOG.error("readPortConfig fail", e);
				return ACTIONFAILURE+e.getMessage();
			}	
		}
		else{
			LOG.error("readPortConfig "+file.getAbsolutePath()+" NotExist!");
			return ACTIONFAILURE+file.getAbsolutePath()+" NotExist!";
		}
	}
	public String setPortsConfig(String portsXML,int regUserId){
		LOG.debug("setPortsConfig");
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(portsXML.getBytes());
			Document doc = builder.build(inputStream);
			String portFile = GenericConfig.getInstance().getFile("port.xml");
			File file = new File(portFile);
			if(!file.exists()) return ACTIONFAILURE+"PortFileNotExistError";
			if(!file.canWrite()) return ACTIONFAILURE+"PortFileReadOnlyError";
			xmlOut.outputToFile(doc, file);
			logOperate(regUserId,Operatelog.MOD,"set config/port.xml success");
			LOG.debug("setPortsConfig success");
			return ACTIONSUCCESS;
		} catch (JDOMException e) {
			LOG.error("setPortsConfig parse fail  portsXML="+portsXML);
			return ACTIONFAILURE+e.getMessage();
		}
	}
}
