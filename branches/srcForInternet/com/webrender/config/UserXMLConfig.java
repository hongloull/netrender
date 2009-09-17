package com.webrender.config;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.Right;
import com.webrender.dao.RightDAO;


public class UserXMLConfig extends XMLConfig {
	private static final Log LOG = LogFactory.getLog(UserXMLConfig.class);
	private static List lisUsers = null ;
	static{
		ReguserDAO regUserDAO = new ReguserDAO();
		lisUsers = regUserDAO.findAll();
		Reguser admin = regUserDAO.findByRegName("admin");
		lisUsers.remove(admin);
	}
	@Override
	public void loadFromXML(File file) throws JDOMException {
		LOG.debug("loadFromXML");
		int index = file.getName().lastIndexOf(".xml");
		if (index == -1){
			return;
		}
		SAXBuilder sb =  new SAXBuilder();
		Document doc = sb.build(file);
		String regName = file.getName().substring(0, index);
		if(regName.equalsIgnoreCase("admin")){
			file.delete();
			return;
		}
		ReguserDAO regUserDAO = new ReguserDAO();
		RightDAO rightDAO = new RightDAO();
		CommandmodelDAO modelDAO = new CommandmodelDAO();
		NodegroupDAO nodeGroupDAO = new NodegroupDAO(); 
		Transaction tx = null;
		try{
			tx = getTransaction();
			Reguser reguser = regUserDAO.findByRegName(regName);
			if(reguser==null){
				reguser = new Reguser(regName,"");
			}
			regUserDAO.save(reguser);
			
			Set set_Rights = reguser.getRights();
			HashSet<Right> set_RetainRights = new HashSet<Right>();
			Set set_Models = reguser.getModels();
			HashSet<Commandmodel> set_RetainModels = new HashSet<Commandmodel>();
			Set set_NGroups = reguser.getNodegroups();
			HashSet<Nodegroup> set_RetainNGroups = new HashSet<Nodegroup>();
			
			Element root = doc.getRootElement();
//			root.getAttributeValue("maxInstance")
			
			
//			-------------------Right--------------------------------
			List<Element> lis_Rights = root.getChildren("Right");
			int rightsLength = lis_Rights.size();
			Right right = null;
			for(int i = 0; i<rightsLength; i++){
				try{
					Element element = lis_Rights.get(i);
					String id = element.getAttributeValue("id");
					right = rightDAO.findById(Integer.parseInt(id));
					if(right!=null){
						set_RetainRights.add(right);
						set_Rights.add(right);
					}
				}
				catch(Exception e){
					LOG.warn("loadFromXML rightError. fileName: "+file.getName()+"; rightOrder:"+i);
				}
			}
			set_Rights.retainAll(set_RetainRights);
//			--------------------------Model----------------------------
			List<Element>  lis_Models = root.getChildren("Model");
			int modelsLength = lis_Models.size();
			Commandmodel model = null;
			for(int i = 0 ; i<modelsLength; i++){
				try{
					Element element = lis_Models.get(i);
					String name = element.getAttributeValue("name");
					model = modelDAO.findByCommandModelName(name);
					if(model!=null){
						set_RetainModels.add(model);
						set_Models.add(model);
					}
				}catch(Exception e){
					LOG.warn("loadFromXML modelError. fileName: "+file.getName()+"; modelOrder:"+i);
				}
			}
			set_Models.retainAll(set_RetainModels);
//			-------------------------------------Nodegroup-----------------
			List<Element> lis_NGroups = root.getChildren("Group");
			int nGroupsLength = lis_NGroups.size();
			Nodegroup nodeGroup = null;
			for(int i=0;i<nGroupsLength;i++){
				try{
					Element element = lis_NGroups.get(i);
					String name = element.getAttributeValue("name");
					nodeGroup = nodeGroupDAO.findByNodeGroupName(name);
					if(nodeGroup!=null){
						set_RetainNGroups.add(nodeGroup);
						set_NGroups.add(nodeGroup);
					}
				}catch(Exception e){
					LOG.warn("loadFromXML modelError. fileName: "+file.getName()+"; nodeGroupOrder:"+i);
				}
			}
			set_NGroups.retainAll(set_RetainNGroups);
			
//			-------------commit----------------
			tx.commit();
			lisUsers.remove(reguser);
			LOG.debug("loadFromXML success fileName: "+file.getName());
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("loadFromXML fail fileName: "+file.getName(),e);
		}
		
	
	}
	
	@Override
	public void deleteExtraData() {
		Transaction tx = null;
		LOG.debug("deleteExtraUsers");
		try{
			tx = getTransaction();
			Iterator<Reguser> ite_Users = lisUsers.iterator();
			ReguserDAO regUserDAO = new ReguserDAO();
			while(ite_Users.hasNext()){
				regUserDAO.delete( ite_Users.next());
			}
			tx.commit();
			LOG.debug("deleteExtraUsers ok");
		}catch(Exception e){
			LOG.error("deleteExtraUsers fail", e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}
	}
}
