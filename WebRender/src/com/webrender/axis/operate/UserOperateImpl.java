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

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.config.UserXMLConfig;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.tool.FileCopy;



public class UserOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(UserOperateImpl.class);
	
	public String getUsersList()
	{	
		LOG.debug("getUsersList");
		try {
			
			this.closeSession();
			String users = GenericConfig.getInstance().getFile("users");
			File path = new File(users);
			if(!path.exists()){
				LOG.error("All UserConfig Not Exist Error");
				return  ACTIONFAILURE+"UserConfigNotExistError";
			}
			Element root = new Element("Users");
			Document doc = new Document(root);
			ReguserDAO regUserDAO = new ReguserDAO();
			Iterator<Reguser> ite_Users = regUserDAO.findAll().iterator();
			while (ite_Users.hasNext()) {
				Reguser user = ite_Users.next();
				String regName = user.getRegName();
//				if("admin".equalsIgnoreCase(regName)) continue;
				Element ele = new Element("User");
				ele.addAttribute("name", regName);
				root.addContent(ele);
			}
			LOG.debug("getUsersList success");
			return (new XMLOut()).outputToString(doc);
		} catch (Exception e) {
			LOG.error("getUsersList fail ",e);
			return  ACTIONFAILURE + e.getMessage();
		}finally{
			this.closeSession();
		}
	}

	
	public String getUserConfig(String regName){
		LOG.debug("getUserConfig regName: "+regName);
		
		String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
		

//		if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
		File file = new File(userFile);
		if(!file.exists()){
			LOG.error("getUserCOnfig not exist regName:"+ regName);
			return  ACTIONFAILURE+" UserNotExistError";
		}
		SAXBuilder sb =  new SAXBuilder();
		Document document = null;
		try {
			document = sb.build(file);
			LOG.debug("getUserConfig success regName: "+regName);
			return (new XMLOut()).outputToString(document);
		} catch (Exception e) {
			LOG.error("getUserConfig fail regName: "+regName, e);
			return  ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String modUserConfig(String userName,String questXML,int regUserId){		
		LOG.debug("modUserConfig regName: "+ userName);
		String userFile = GenericConfig.getInstance().getFile("users/"+userName+".xml");
		File file = new File(userFile);
		if(!file.exists()){
			LOG.error(userName+".xml not exist in config!");
			return  ACTIONFAILURE+" UserNameNotExistError";
		}
		if(!file.canWrite()){
			LOG.error(userName+".xml cannot write ");
			return  ACTIONFAILURE+" UserConfigCannotWrite";
		}
		Transaction tx = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());			
			Document doc = builder.build(inputStream);
			(new XMLOut()).outputToFile(doc, file);
			UserXMLConfig loadConfig = new UserXMLConfig();
			loadConfig.loadFromXML(file);
			tx = getTransaction();
			logOperate(regUserId,Operatelog.MOD,"mod UserConfig:"+userName);
			tx.commit();
			return  ACTIONSUCCESS;
		} catch (JDOMException e) {
			LOG.error("moduserConfig fail : questXMLParseError userName:"+userName);
			return  ACTIONFAILURE+" :"+userName+".xml parse error";
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("moduserConfig fail userName: "+userName,e);
			return  ACTIONFAILURE +"moduserConfig fail userName: "+userName+" "+e.getMessage();
		}
		finally{
			this.closeSession();
		}
	}
	public String delUser(String regName,int regUserId){
		
		LOG.debug("delUser: "+regName);
		Transaction tx = null;
		try{
			if("admin".equalsIgnoreCase(regName) ){
				LOG.error("cannot delete admin!");
				return  ACTIONFAILURE+" DelAdminError";
			}
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser==null){
				LOG.warn(regName+" issn't exist in database");
				return  ACTIONFAILURE +" UserNotExistInDatabase";
			}
			String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
			File file_User = new File(userFile);
			boolean result = file_User.delete();
			if(file_User.exists() && result == false){
				LOG.error("del "+file_User+" error");
				return   ACTIONFAILURE+" Del"+regName+"FileError";
			}
			else{
				tx = getTransaction();
				regUserDAO.delete(regUser);
				logOperate(regUserId,Operatelog.DEL,"DelUser:"+regName);
				tx.commit();
				LOG.debug("delUser "+regName+" success.");
				return  ACTIONSUCCESS;
			}			
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("delUser fail",e);
			return  ACTIONFAILURE+" Del"+regName+"Error: "+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String addUser(String regName ,String passWord,int regUserId){		
		LOG.debug("addUser: "+regName);
		Transaction tx = null;
		try{
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser!=null){
				LOG.warn(regName+" exist, can't be added.");
				return  ACTIONFAILURE+" UserExistError";
			}
			String defConfig = GenericConfig.getInstance().getFile("users/default");
			File defFile = new File(defConfig);
			if(defFile.exists() && defFile.canRead())
			{
				String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
				try {
					regUser = new Reguser(regName,passWord+"");
					regUserDAO.save(regUser);
					(new FileCopy()).copy(defConfig,userFile);
					File file_User = new File(userFile);
					if( file_User.exists() ){
						UserXMLConfig loadConfig = new UserXMLConfig();
						loadConfig.loadFromXML(file_User);
						tx = getTransaction();
						logOperate(regUserId,Operatelog.ADD,"AddUser:"+regName);
						tx.commit();
					}
				}catch (IOException e) {
					LOG.error("addUser "+regName+" fail: DefaultFileCopyError", e);
					return  ACTIONFAILURE+" DefaultConfigCopyError";
				} catch (JDOMException e) {
					LOG.error("addUser "+regName+" fail: DefaultFileParseError", e);
					return  ACTIONFAILURE+" DefaultFileParseError";
				}
				LOG.info("add User: "+regName+" success");
				return  ACTIONSUCCESS;
			}
			else{		
				LOG.error("add regUser: "+regName +"fail: DefaultConfigNotExistError");
				return  ACTIONFAILURE+" DefaultConfigNotExistError";
			}
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("addUser fail",e);
			return  ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String setPassWord(String regName ,String passWord,int regUserId){
		
		LOG.debug("setPassWord: "+ regName);
		if (regName == null || passWord == null){
			LOG.debug("args can't be null regName: "+regName+" passWord: "+passWord);
			return  ACTIONFAILURE+" Args is null";
		}
		Transaction tx = null;
		try{
			tx = getTransaction();
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser==null){
				return  ACTIONFAILURE+" UserNotExistError";
			}
			regUser.setPassWord(passWord);
			regUserDAO.save(regUser);
			logOperate(regUserId,Operatelog.MOD,"changePassWord: success.");
			tx.commit();
			LOG.debug("setPassWord success Name: "+regName);
			return  ACTIONSUCCESS;
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("setPassWord fail Name: "+regName,e);
			return  ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
}
