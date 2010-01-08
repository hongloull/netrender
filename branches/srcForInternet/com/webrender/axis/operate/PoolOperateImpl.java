package com.webrender.axis.operate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.config.NodeXMLConfig;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.server.ControlThreadServer;
import com.webrender.tool.FileCopy;

public class PoolOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(PoolOperateImpl.class);
	private XMLOut xmlOut = new XMLOut();
	private NodeUtils nodeUtils = new NodeUtils();
	public String addPool(String name,int regUserId){
		LOG.debug("addPool :"+name);
		if(name==null) return ACTIONFAILURE+"name can not be null";
		Transaction tx = null;
		try{
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup pool = nGDAO.findByNodeGroupName(name);
			if(pool != null){
				return ACTIONFAILURE+"PoolExistError";
			}
			
			String defConfig = GenericConfig.getInstance().getFile("nodes/default");
			File defFile = new File(defConfig);
			if(!defFile.exists() || !defFile.canRead() ){
				return ACTIONFAILURE+"pool default config not exist error";
			}
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			
			try{
				(new FileCopy()).copy(defConfig,poolFile);
				File file_Pool = new File(poolFile);
				if(file_Pool.exists()){
					NodeXMLConfig loadConfig = new NodeXMLConfig();
					loadConfig.loadFromXML(file_Pool);
					tx = getTransaction();
					logOperate(regUserId,Operatelog.ADD,"AddPool:"+name);
					tx.commit();
				}
			}catch(IOException e){
				LOG.error("addPool fail: DefaultFileCopyError",e);
				return ACTIONFAILURE+"DefaultFileCopyError";
			}catch (JDOMException e) {
				LOG.error("addUser fail: DefaultFileParseError", e);
				return ACTIONFAILURE+"DefaultFileParseError";
			}
			return ACTIONSUCCESS;
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("addPool fail",e);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String modPool(String name ,String questXML,int regUserId){
		LOG.debug("modPoolConfig: "+name);
		if(name==null) return ACTIONFAILURE+"name can not be null";
		Transaction tx = null;
		try{			
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			File file = new File(poolFile);
			if(!file.exists()) return ACTIONFAILURE+"PoolFileNotExistError";
			if(!file.canWrite()) return ACTIONFAILURE+"PoolFileReadOnlyError";
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());			
			Document doc = builder.build(inputStream);
			xmlOut.outputToFile(doc, file);
			NodeXMLConfig loadConfig = new NodeXMLConfig();
			loadConfig.loadFromXML(file);
			tx = getTransaction();
			logOperate(regUserId,Operatelog.MOD,"ModPool:"+name);
			tx.commit();
			ControlThreadServer.getInstance().notifyResume();
			return ACTIONSUCCESS;
		}catch(JDOMException e){
			LOG.warn("modPool fail: XMLParseError name: "+name);
			return ACTIONFAILURE+"XMLParseError";
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("modPool fail name: "+name,e);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String getPoolConfig(String name){		
		LOG.debug("getPoolConfig: "+name);
		if(name==null) return ACTIONFAILURE+"name can not be null";
		try{
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			File file = new File(poolFile);
			if( !file.exists() ) return ACTIONFAILURE+"PoolNotExistError";
			
			SAXBuilder sb =  new SAXBuilder();
			Document document = null;
			document = sb.build(file);			
			return xmlOut.outputToString(document);
		}catch(Exception e){
			LOG.error("getPoolConfig fail poolName: "+name,e);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String delPool(String name,int regUserId){
		LOG.debug("delPool: " +name);
		try{
//			if(name.equalsIgnoreCase("All") ) return ACTIONFAILURE+"Del pool All Error";
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup pool = nGDAO.findByNodeGroupName(name);
			if(pool==null ) return ACTIONFAILURE+"PoolNotExistError";
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			File file_Pool = new File(poolFile);
			boolean result = file_Pool.delete();
			if(file_Pool.exists() || result == false){
				return ACTIONFAILURE+"Del "+name+" file error";
			}else{
				Transaction tx =null;
				try{
					tx = getTransaction();
					nGDAO.delete(pool);
					logOperate(regUserId,Operatelog.DEL,"DelPool:"+name);
					tx.commit();
					LOG.debug("delPool success");
					return ACTIONSUCCESS;
				}catch(Exception e){
					if(tx!=null){
						tx.rollback();
					}
					LOG.error("delPool from database fail",e);
					return ACTIONFAILURE+"Del "+name+"f rom db error";
				}
			}
		}catch(Exception e){
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String getPools(int regUserId,boolean isAdmin){
		LOG.debug("getPools");
		try{
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findById(regUserId);		
			Iterator<Nodegroup> ite_Pools = null;
			if(isAdmin){
				ite_Pools = ( new NodegroupDAO() ).findAll().iterator(); 
			}else{
				ite_Pools = regUser.getNodegroups().iterator();				
			}
			Element root = new Element("Pools");
			Document doc = new Document(root);
			while (ite_Pools.hasNext()) {
				Nodegroup pool = ite_Pools.next();
				Element element = new Element("Pool");
				element.addAttribute("name", pool.getNodeGroupName());
				root.addContent(element);
			}
			LOG.debug("getPools success");
			return xmlOut.outputToString(doc);
		} catch (Exception e) {
			LOG.error("getPools fail regUserId:"+regUserId+" isAdmin:"+isAdmin);
			return ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	
	public String getNodes(){
		LOG.debug("getNodes");
		try{
			Element root = new Element("Nodes");
			Document doc = new Document(root);
//			Collection machines = NodeMachineManager.getNodeMachines();
			NodeDAO nodeDAO = new NodeDAO();
			Element element = null;
			Iterator ite_Nodes = nodeDAO.findAll().iterator();
			while(ite_Nodes.hasNext()){
				Node node = (Node) ite_Nodes.next();
				NodeMachine nodeMachine = NodeMachineManager.getInstance().getNodeMachine(node.getNodeId());
				if ( nodeMachine.isConnect() ){
					root.addContent(nodeUtils.bean2xml(node));
				}
			}
			String result = xmlOut.outputToString(doc);
			LOG.debug("getNodes success");
			return result;
		}catch(Exception e){
			LOG.error("getNodes fail",e);
			return ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}
	
}
