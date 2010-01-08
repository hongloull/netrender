package com.webrender.axis.operate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.bean.nodeconfig.NodeConfigUtils;
import com.webrender.config.GenericConfig;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Operatelog;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class ConfigOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(ConfigOperateImpl.class);
	private XMLOut xmlOut = new XMLOut();
	private ServerMessages serverMessages = new ServerMessages();
	private NodeConfigUtils nodeConfigUtils = new NodeConfigUtils();
	public String getPathConfig(){		
		LOG.debug("getPathConfig");
		try{
		String mapDir = GenericConfig.getInstance().getFile("mapDir.xml");
		File file = new File(mapDir);
		if(!file.exists()) return ACTIONFAILURE+"FileNotExistError";
		SAXBuilder sb =  new SAXBuilder();
		Document doc = null;
		doc = sb.build(file);
		LOG.debug("getPathConfig success");
		return xmlOut.outputToString(doc);
		}catch(JDOMException e){
			LOG.error("getPathConfig ParsError", e);
			return ACTIONFAILURE+"XMLParseError"+e.getMessage();
		}
		catch(Exception e){
			LOG.error("getPathConfig fail",e);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String setPathConfig(String questXML,int regUserId){				
		LOG.debug("setPathConfig");
		
		Transaction tx = null;
		try {
			String mapDir = GenericConfig.getInstance().getFile("mapDir.xml");
			File file = new File(mapDir);
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML
					.getBytes());
			Document doc = builder.build(inputStream);
			xmlOut.outputToFile(doc, file);
			LOG.debug("setPathConfig success");
			tx = getTransaction();
			logOperate(regUserId,Operatelog.MOD,"configMapDir");
			tx.commit();
			return ACTIONSUCCESS;
		}catch(NullPointerException e){
			LOG.debug("setPathConfig NullPointerException questXML:"+questXML);
			return ACTIONFAILURE+e.getMessage();
		}catch (JDOMException e) {
			LOG.error("setPathConfig JDOMException questXML:"+questXML);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("setPathConfig fail",e);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String getNodeConfig(String nodeId){
		LOG.debug("getNodeConfig nodeId:"+nodeId);
		NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(Integer.parseInt(nodeId));
		Short pri = nodeMachine.getPri();
		nodeMachine.updateConfig(null);
		try {
			if (nodeMachine.execute(serverMessages.createWantConfigPkt()) ){
				String configInfo = nodeMachine.getConfigInfo();
				nodeMachine.updateConfig(null);
				if( configInfo==null) {
					return ACTIONFAILURE+"configInfo=null";
				}else{
//					NodeConfig config = nodeConfigUtils.xmlString2bean(configInfo);
//					config.getGeneralConfig().setPriority(pri.toString());
//					return nodeConfigUtils.bean2xmlString(config);
					return nodeConfigUtils.setPriorityToConfig(configInfo, pri);	
				}
			}
			else{
				return ACTIONFAILURE+" getNodeConfig fail node not response.";
			}
		} catch (Exception e) {
			LOG.error("getNodeConfig fail",e);
			return ACTIONFAILURE+e.getMessage();			
		}
	}
	
	public String setNodeConfig(String nodeId,String config,int regUserId){
		
		Transaction tx = null;
		try {
			int id = Integer.parseInt(nodeId);
			NodeMachine nodeMachine  = NodeMachineManager.getInstance().getNodeMachine(id);
			
//			NodeConfig nodeConfig = nodeConfigUtils.xmlString2bean(config);
			Short pri = nodeConfigUtils.getPrioritFromConfig(config);
			tx = HibernateSessionFactory.getSession().beginTransaction();
			NodeDAO nodeDAO = new NodeDAO();
			Node node =nodeDAO.findById(id);
//			short pri = Short.parseShort(nodeConfig.getGeneralConfig().getPriority());
			
			node.setPri(pri);
			tx.commit();
			nodeMachine.setPri(pri);
			if( nodeMachine.execute( serverMessages.createSetConfigPkt(config)) ){
				this.logOperate(regUserId,Operatelog.MOD,"set node "+ node.getNodeName()+" config success");
				return ACTIONSUCCESS;
			}
			else{
				return ACTIONFAILURE+"setNodeConfig fail: node not response";
			}
		}catch(NumberFormatException e){
			LOG.error("setNodeConfig NumberFormatException nodeId:"+nodeId+" config:"+config);
			return ACTIONFAILURE+e.getMessage();
		}catch(NullPointerException e){
			LOG.error("setNodeConfig NullPointerException nodeId:"+nodeId+" config:"+config);
			return ACTIONFAILURE+e.getMessage();
		}catch(org.jdom.JDOMException e){
			LOG.error("setNodeConfig org.jdom.JDOMException nodeId:"+nodeId+" config:"+config);
			return ACTIONFAILURE+e.getMessage();
		}
		catch (Exception e) {
			LOG.error("setNodeConfig fail "+ config ,e);
			if(tx!=null){
				tx.rollback();
			}
			return ACTIONFAILURE+e.getMessage();
		}finally{
			HibernateSessionFactory.closeSession();			
		}
	}
	
}
