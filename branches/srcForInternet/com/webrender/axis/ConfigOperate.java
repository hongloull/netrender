package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.dao.Operatelog;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class ConfigOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(ConfigOperate.class);
	
	public String getPathConfig(){
		LOG.debug("getPathConfig");
		try{
		String mapDir = GenericConfig.getInstance().getFile("mapDir.xml");
		File file = new File(mapDir);
		if(!file.exists()) return "FileNotExistError";
		SAXBuilder sb =  new SAXBuilder();
		Document doc = null;
		doc = sb.build(file);
		LOG.debug("getPathConfig success");
		return XMLOut.outputToString(doc);
		}catch(JDOMException e){
			LOG.error("getPathConfig ParsError", e);
			return "XMLParseError"+e.getMessage();
		}
		catch(Exception e){
			LOG.error("getPathConfig fail",e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String setPathConfig(String questXML){
		LOG.debug("setPathConfig");
		Transaction tx = null;
		try {
			String mapDir = GenericConfig.getInstance().getFile("mapDir.xml");
			File file = new File(mapDir);
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML
					.getBytes());
			Document doc = builder.build(inputStream);
			XMLOut.outputToFile(doc, file);
			LOG.debug("setPathConfig success");
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"configMapDir");
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		} catch (JDOMException e) {
			LOG.error("setPathConfig ParseError",e);
			return "XMLParseError"+e.getMessage();
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("setPathConfig fail",e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String getNodeConfig(String nodeId){
		NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
		nodeMachine.updateConfig(null);
		try {
			if (nodeMachine.execute(ServerMessages.createWantConfigPkt()) ){
				String configInfo = nodeMachine.getConfigInfo();
				nodeMachine.updateConfig(null);
				return configInfo==null?BaseAxis.ACTIONFAILURE+" configInfo=null":configInfo;
			}
			else{
				return BaseAxis.ACTIONFAILURE+" getNodeConfig fail";
			}
		} catch (Exception e) {
			LOG.error("getNodeConfig fail",e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();			
		}
	}
	
	public String setNodeConfig(String nodeId,String config){
		try {
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(Integer.parseInt(nodeId));
			if( nodeMachine.execute( ServerMessages.createSetConfigPkt(config)) ){
				return BaseAxis.ACTIONSUCCESS;
			}
		} catch (Exception e) {
			LOG.error("setNodeConfig fail "+ config ,e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		return BaseAxis.ACTIONFAILURE;
	}
	
}
