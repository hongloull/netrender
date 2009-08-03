package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.config.NodeXMLConfig;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.tool.FileCopy;

public class PoolOperate extends BaseAxis {
	private static final Log log = LogFactory.getLog(PoolOperate.class);
	
	public String addPool(String name){
		log.debug("addPool :"+name);
		try{
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup pool = nGDAO.findByNodeGroupName(name);
			if(pool != null){
				return "PoolExistError";
			}
			
			String defConfig = GenericConfig.getInstance().getFile("nodes/default");
			File defFile = new File(defConfig);
			if(!defFile.exists() || !defFile.canRead() ){
				return "PoolConfigNotExistError";
			}
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			try{
				FileCopy.copy(defConfig,poolFile);
				File file_Pool = new File(poolFile);
				if(file_Pool.exists()){
					NodeXMLConfig loadConfig = new NodeXMLConfig();
					loadConfig.loadFromXML(file_Pool);
				}
			}catch(IOException e){
				log.error("addPool fail: DefaultFileCopyError",e);
				return "DefaultFileCopyError";
			}catch (JDOMException e) {
				log.error("addUser fail: DefaultFileParseError", e);
				return "DefaultFileParseError";
			}
			return BaseAxis.ActionSuccess;
		}catch(Exception e){
			log.error("addPool fail",e);
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	
	public String modPool(String name ,String questXML){
		log.debug("modPoolConfig: "+name);
		try{			
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			File file = new File(poolFile);
			if(!file.exists()) return "PoolFileNotExistError";
			if(!file.canWrite()) return "PoolFileReadOnlyError";
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());			
			Document doc = builder.build(inputStream);
			XMLOut.outputToFile(doc, file);
			NodeXMLConfig loadConfig = new NodeXMLConfig();
			loadConfig.loadFromXML(file);
			return BaseAxis.ActionSuccess;
		}catch(JDOMException e){
			log.warn("modPool fail: XMLParseError name: "+name);
			return "XMLParseError";
		}
		catch(Exception e){
			log.error("modPool fail name: "+name,e);
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	
	public String getPoolConfig(String name){
		log.debug("getPoolConfig: "+name);
		try{
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			File file = new File(poolFile);
			if( !file.exists() ) return "PoolNotExistError";
			
			SAXBuilder sb =  new SAXBuilder();
			Document document = null;
			document = sb.build(file);			
			return XMLOut.outputToString(document);
		}catch(Exception e){
			log.error("getPoolConfig fail poolName: "+name,e);
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	
	public String delPool(String name){
		log.debug("delPool: " +name);
		try{
			if(name.equalsIgnoreCase("All") ) return "DelPoolAllError";
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup pool = nGDAO.findByNodeGroupName(name);
			if(pool==null ) return "PoolNotExistError";
			String poolFile = GenericConfig.getInstance().getFile("nodes/"+name+".xml");
			File file_Pool = new File(poolFile);
			boolean result = file_Pool.delete();
			if(result == false){
				return "Del"+name+"FileError";
			}else{
				Transaction tx =null;
				try{
					tx = getTransaction();
					nGDAO.delete(pool);
					tx.commit();
					log.debug("delPool success");
					return BaseAxis.ActionSuccess;
				}catch(Exception e){
					if(tx!=null){
						tx.rollback();
					}
					log.error("delPool from database fail",e);
					return "Del"+name+"FromDBError";
				}
			}
		}catch(Exception e){
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	public String getAllPools(){
		try {
			NodegroupDAO nGDAO = new NodegroupDAO();
			Iterator<Nodegroup> ite_Pools = nGDAO.findAll().iterator();
			Element root = new Element("Pools");
			Document doc = new Document(root);
			while (ite_Pools.hasNext()) {
				Nodegroup pool = ite_Pools.next();
				Element element = new Element("Pool");
				element.addAttribute("name", pool.getNodeGroupName());
				root.addContent(element);
			}
			return XMLOut.outputToString(doc);
		} catch (Exception e) {
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	public String getPools()
	{
		return getAllPools();
	}
	
	public String getNodes(){
		log.debug("getNodes");
		try{
			Element root = new Element("Nodes");
			Document doc = new Document(root);
			Collection machines = NodeMachineManager.getNodeMachines();
			NodeDAO nodeDAO = new NodeDAO();
			Element element = null;
			Iterator ite_Nodes = nodeDAO.findAll().iterator();
			while(ite_Nodes.hasNext()){
				Node node = (Node) ite_Nodes.next();
				NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeIp());
				if ( nodeMachine.isConnect() ){
					element = new Element("Node");
					element.addAttribute("name", node.getNodeName());
					element.addAttribute("ip", node.getNodeIp());
					root.addContent(element);
				}
			}
			String result = XMLOut.outputToString(doc);
			log.debug("getNodes success");
			return result;
		}catch(Exception e){
			log.error("getNodes fail",e);
			return null;
		}finally{
			this.closeSession();
		}
	}
	
}
