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
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.tool.FileCopy;



public class UserOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(UserOperate.class);
	
	public String getUsersList()
	{
		LOG.debug("getUserConfig");
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
				Element ele = new Element("User");
				ele.addAttribute("name", user.getRegName());
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
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());			
			Document doc = builder.build(inputStream);
			XMLOut.outputToFile(doc, file);
			UserXMLConfig loadConfig = new UserXMLConfig();
			loadConfig.loadFromXML(file);
			return BaseAxis.ACTIONSUCCESS;
		} catch (JDOMException e) {
			LOG.warn("moduserConfig fail:questXMLParseError userName:"+userName);
			return "XMLParseError";
		}finally{
			this.closeSession();
		}
	}
	public String delUser(String regName){
		LOG.debug("delUser: "+regName);
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findByRegName(regName);
		if (regUser==null) return "UserNotExistError";
		String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
		File file_User = new File(userFile);
		boolean result = file_User.delete();
		if(result == false){
			this.closeSession();
			return "Del"+regName+"FileError";
		}
		else{
			Transaction tx = null;
			try{
				tx = getTransaction();
				regUserDAO.delete(regUser);
				tx.commit();
				LOG.debug("delUser success");
				return BaseAxis.ACTIONSUCCESS;
			}catch(Exception e){
				if(tx!=null){
					tx.rollback();
				}
				LOG.error("delUser from database fail",e);
				return "Del"+regName+"FromDBError";
			}finally{
				this.closeSession();
			}
		}
	}
	public String addUser(String regName ,String passWord){
		LOG.debug("addUser: "+regName);
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
			LOG.error("addUser fail",e);
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}

}
