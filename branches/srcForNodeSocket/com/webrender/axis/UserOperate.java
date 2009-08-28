package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.config.UserXMLConfig;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.tool.FileCopy;



public class UserOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(UserOperate.class);
	
	public String getUsersList()
	{
		LOG.debug("getUsersList");
		try {
			this.closeSession();
			String users = GenericConfig.getInstance().getFile("users");
			File path = new File(users);
			if(!path.exists()) return "UserConfigNotExistError";
			Element root = new Element("Users");
			Document doc = new Document(root);
			ReguserDAO regUserDAO = new ReguserDAO();
			Iterator<Reguser> ite_Users = regUserDAO.findAll().iterator();
			while (ite_Users.hasNext()) {
				Reguser user = ite_Users.next();
				String regName = user.getRegName();
				if("admin".equalsIgnoreCase(regName)) continue;
				Element ele = new Element("User");
				ele.addAttribute("name", regName);
				root.addContent(ele);
			}
			LOG.debug("getUsersList success");
			return XMLOut.outputToString(doc);
		} catch (Exception e) {
			LOG.error("getUsersList ",e);
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}
	
	public String getUserConfig(String regName){
		LOG.debug("getUserConfig");
		
		String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
		

//		if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
		File file = new File(userFile);
		if(!file.exists()) return "UserNotExistError";
		SAXBuilder sb =  new SAXBuilder();
		Document document = null;
		try {
			document = sb.build(file);
			return XMLOut.outputToString(document);
		} catch (Exception e) {
			LOG.error("getUserConfig fail userName: "+regName, e);
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}
	public String modUserConfig(String userName,String questXML){
		LOG.debug("modUserConfig");
		String userFile = GenericConfig.getInstance().getFile("users/"+userName+".xml");
		File file = new File(userFile);
		if(!file.exists()) return "UserNameNotExistError";
		if(!file.canWrite()) return "UserConfigReadOnlyError";
		Transaction tx = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());			
			Document doc = builder.build(inputStream);
			XMLOut.outputToFile(doc, file);
			UserXMLConfig loadConfig = new UserXMLConfig();
			loadConfig.loadFromXML(file);
			tx = getTransaction();
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"ModUserConfig:"+userName);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		} catch (JDOMException e) {
			LOG.warn("moduserConfig fail:questXMLParseError userName:"+userName);
			return "XMLParseError";
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("moduserConfig fail userName: "+userName,e);
			return BaseAxis.ACTIONFAILURE;
		}
		finally{
			this.closeSession();
		}
	}
	public String delUser(String regName){
		LOG.debug("delUser: "+regName);
		Transaction tx = null;
		try{
			if("admin".equalsIgnoreCase(regName) ) return "DelAdminError";
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser==null) return "UserNotExistError";
			String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
			File file_User = new File(userFile);
			boolean result = file_User.delete();
			if(file_User.exists() && result == false){
				return "Del"+regName+"FileError";
			}
			else{
				tx = getTransaction();
				regUserDAO.delete(regUser);
				logOperate(getLoginUserId(),Operatelog.DEL,"DelUser:"+regName);
				tx.commit();
				LOG.debug("delUser success");
				return BaseAxis.ACTIONSUCCESS;				
			}			
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("delUser fail",e);
			return "Del"+regName+"Error";
		}finally{
			this.closeSession();
		}
	}
	public String addUser(String regName ,String passWord){
		LOG.debug("addUser: "+regName);
		Transaction tx = null;
		try{
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser!=null){
				return "UserExistError";
			}
			String defConfig = GenericConfig.getInstance().getFile("users/default");
			File defFile = new File(defConfig);
			if(defFile.exists() && defFile.canRead())
			{
				String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
				try {
					regUser = new Reguser(regName,passWord+"");
					regUserDAO.save(regUser);
					FileCopy.copy(defConfig,userFile);
					File file_User = new File(userFile);
					if( file_User.exists() ){
						UserXMLConfig loadConfig = new UserXMLConfig();
						loadConfig.loadFromXML(file_User);
						tx = getTransaction();
						logOperate(getLoginUserId(),Operatelog.ADD,"AddUser:"+regName);
						tx.commit();
					}
				}catch (IOException e) {
					LOG.error("addUser fail: DefaultFileCopyError", e);
					return "DefaultConfigCopyError";
				} catch (JDOMException e) {
					LOG.error("addUser fail: DefaultFileParseError", e);
					return "DefaultFileParseError";
				}
				return BaseAxis.ACTIONSUCCESS;
			}
			else{		
				return "DefaultConfigNotExistError";
			}
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("addUser fail",e);
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}

}