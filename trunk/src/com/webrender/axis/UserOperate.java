package com.webrender.axis;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.ReguserUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.Role;
import com.webrender.dao.RoleDAO;

public class UserOperate extends BaseAxis {
	private static final Log log = LogFactory.getLog(UserOperate.class);
	/**
	 * 添加用户并同步XML配置文件
	 * 
	 * @param regName
	 * @param passWord
	 * @param groupName
	 * @return
	 */
	public String addUser(String regName,String passWord,String groupName){
		log.debug("addUser: "+ regName);
		try{
			if (!this.canVisit(8)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findByRegName(regName);
		if (regUser!=null) return BaseAxis.NameExist;
		
		
		Transaction tx = null;
		try{
			String usersFile = GenericConfig.getInstance().getFile("right/users.xml");
			if(usersFile==null) return "NoFileUsersXMLError";
			File file = new File(usersFile);
			if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
			SAXBuilder sb =  new SAXBuilder();
			Document doc = sb.build(file);
			
			tx = getTransaction();
			regUser = new Reguser(regName,passWord+"");
			RoleDAO roleDAO = new RoleDAO();
			Role role = roleDAO.findByRoleName(groupName);
			if (role==null) throw new NullPointerException("UserGroupNotExistError");
			regUser.setRole(role);
			regUserDAO.save(regUser);
			Element element = ReguserUtils.bean2Xml(regUser);
			if (element==null) throw new NullPointerException("bean2XMLError");
			Element root = doc.getRootElement(); 
			
			Iterator<Element> ite_Users = root.getChildren().iterator();
			while(ite_Users.hasNext() ){
				Element ele = ite_Users.next();
				if ( regName.equals(  ele.getAttributeValue("name") ) ){
					log.debug("find "+regName+" from users.xml");
					root.getMixedContent().remove(ele);
				}
			}
			root.addContent(element);
			tx.commit();
			XMLOut.outputToFile(doc,file);
		}catch(NullPointerException e){
			if(tx!=null){
				tx.rollback();
			}
			log.error("addUser error: "+regName,e);
			return e.getLocalizedMessage();	
		}catch(Exception e){
			log.error("addUser error: "+regName,e);
			if(tx!=null){
				tx.rollback();
			}
			return e.getLocalizedMessage();
		}finally
		{
			closeSession();
		}
		log.debug("addUser success");
		return BaseAxis.ActionSuccess;
	}
	
	public String modUser(String regName,String modName,String passWord,String groupName){
		log.debug("modUser");
		
//		try{
//			if (!this.canVisit(8)){
//				return BaseAxis.RightError;
//			}			
//		}catch(Exception e){
//			log.error("RightVisit error",e);
//			return BaseAxis.RightError;
//		}
//		
		Transaction tx = null;
		try{
			String usersFile = GenericConfig.getInstance().getFile("right/users.xml");
			if(usersFile==null) return "NoFileUsersXMLError";
			File file = new File(usersFile);
			if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
			SAXBuilder sb =  new SAXBuilder();
			Document doc = sb.build(file);
			
			tx = getTransaction();
			
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if(regUser==null) return "UserNotExistError";
			if(modName!=null) regUser.setRegName(modName);
			if(passWord!=null)regUser.setPassWord(passWord);
			
			if (groupName!=null && (! groupName.equals(regUser.getRole().getRoleName() )) ) {
				RoleDAO roleDAO = new RoleDAO();
				Role role = roleDAO.findByRoleName(groupName);
				if(role!=null) regUser.setRole(role);			
			}
			regUserDAO.save(regUser);
		
			Element element = ReguserUtils.bean2Xml(regUser);
			if (element==null) throw new NullPointerException("bean2XMLError");
			Element root = doc.getRootElement(); 
			
			Iterator<Element> ite_Users = root.getChildren().iterator();
			while(ite_Users.hasNext() ){
				Element ele = ite_Users.next();
				if ( regName.equals(  ele.getAttributeValue("name") ) ){
					log.debug("find "+regName+" from users.xml");
					root.getMixedContent().remove(ele);
				}
			}
			root.addContent(element);
			tx.commit();
			XMLOut.outputToFile(doc,file);
					
			
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			log.error("modUser failure" ,e);
			return e.getLocalizedMessage();
			
		}finally{
			closeSession();
		}
		log.debug("modUser success");
		return BaseAxis.ActionSuccess;
	}
	
	public String delUser(String regName){
		log.debug("delUser");
		
		try{
			if (!this.canVisit(8)){
				log.debug("delUser RightError");
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findByRegName(regName);
		if(regUser==null) return "UserNotExistError";
		Transaction tx = null;
		try{
			String usersFile = GenericConfig.getInstance().getFile("right/users.xml");
			if(usersFile==null) return "NoFileUsersXMLError";
			File file = new File(usersFile);
			if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
			SAXBuilder sb =  new SAXBuilder();
			Document doc = sb.build(file);
			Element root = doc.getRootElement();
			Element ele_User = null;
			Iterator<Element> ite_Users = root.getChildren().iterator();
			while(ite_Users.hasNext() ){
				Element ele = ite_Users.next();
				if ( regName.equals(  ele.getAttributeValue("name") ) ){
					log.debug("find "+regName+" from users.xml");
					ele_User = ele;
				}
			}
			if (ele_User!=null) root.getMixedContent().remove(ele_User);
						
			tx = getTransaction();
			regUserDAO.delete(regUser);
			
			tx.commit();
			XMLOut.outputToFile(doc,file);
			
		}catch(Exception e)
		{
			if(tx!=null){
				tx.rollback();
			}
			log.error("delUser failure" ,e);
			return e.getLocalizedMessage();
		}finally{
			closeSession();
		}
		log.equals("delUser success");
		return BaseAxis.ActionSuccess;
	}
	
	public String getUsers(){
		log.debug("getUsers");
		
		
		try{
			ReguserDAO regUserDAO = new ReguserDAO();
			regUserDAO.findAll();
		}catch(Exception e){
			
		}finally{
			closeSession();
		}
		return BaseAxis.ActionFailure;
	}
	
	public String addGroup(String name){
		return BaseAxis.ActionFailure; 
	}
	
	public String modGroup(String questXML){
		return BaseAxis.ActionFailure;
	}
	
	public String delGroup(String name){
		return BaseAxis.ActionFailure;
	}
}
