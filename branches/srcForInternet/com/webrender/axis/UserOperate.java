package com.webrender.axis;

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



public class UserOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(UserOperate.class);
	
	public String getUsersList()
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		LOG.debug("getUsersList");
		try {
			
			this.closeSession();
			String users = GenericConfig.getInstance().getFile("users");
			File path = new File(users);
			if(!path.exists()){
				LOG.error("All UserConfig Not Exist Error");
				return BaseAxis.ACTIONFAILURE+" UserConfigNotExistError";
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
			return XMLOut.outputToString(doc);
		} catch (Exception e) {
			LOG.error("getUsersList fail ",e);
			return BaseAxis.ACTIONFAILURE + e.getMessage();
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
			return BaseAxis.ACTIONFAILURE+" UserNotExistError";
		}
		SAXBuilder sb =  new SAXBuilder();
		Document document = null;
		try {
			document = sb.build(file);
			LOG.debug("getUserConfig success regName: "+regName);
			return XMLOut.outputToString(document);
		} catch (Exception e) {
			LOG.error("getUserConfig fail regName: "+regName, e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String modUserConfig(String userName,String questXML){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		LOG.debug("modUserConfig regName: "+ userName);
		String userFile = GenericConfig.getInstance().getFile("users/"+userName+".xml");
		File file = new File(userFile);
		if(!file.exists()){
			LOG.error(userName+".xml not exist in config!");
			return BaseAxis.ACTIONFAILURE+" UserNameNotExistError";
		}
		if(!file.canWrite()){
			LOG.error(userName+".xml cannot write ");
			return BaseAxis.ACTIONFAILURE+" UserConfigCannotWrite";
		}
		Transaction tx = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());			
			Document doc = builder.build(inputStream);
			XMLOut.outputToFile(doc, file);
			UserXMLConfig loadConfig = new UserXMLConfig();
			loadConfig.loadFromXML(file);
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"mod UserConfig:"+userName);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		} catch (JDOMException e) {
			LOG.error("moduserConfig fail : questXMLParseError userName:"+userName);
			return BaseAxis.ACTIONFAILURE+" :"+userName+".xml parse error";
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("moduserConfig fail userName: "+userName,e);
			return BaseAxis.ACTIONFAILURE +"moduserConfig fail userName: "+userName+" "+e.getMessage();
		}
		finally{
			this.closeSession();
		}
	}
	public String delUser(String regName){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		LOG.debug("delUser: "+regName);
		Transaction tx = null;
		try{
			if("admin".equalsIgnoreCase(regName) ){
				LOG.error("cannot delete admin!");
				return BaseAxis.ACTIONFAILURE+" DelAdminError";
			}
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser==null){
				LOG.warn(regName+" issn't exist in database");
				return BaseAxis.ACTIONFAILURE +" UserNotExistInDatabase";
			}
			String userFile = GenericConfig.getInstance().getFile("users/"+regName+".xml");
			File file_User = new File(userFile);
			boolean result = file_User.delete();
			if(file_User.exists() && result == false){
				LOG.error("del "+file_User+" error");
				return  BaseAxis.ACTIONFAILURE+" Del"+regName+"FileError";
			}
			else{
				tx = getTransaction();
				regUserDAO.delete(regUser);
				logOperate(getLoginUserId(),Operatelog.DEL,"DelUser:"+regName);
				tx.commit();
				LOG.debug("delUser "+regName+" success.");
				return BaseAxis.ACTIONSUCCESS;
			}			
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("delUser fail",e);
			return BaseAxis.ACTIONFAILURE+" Del"+regName+"Error: "+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String addUser(String regName ,String passWord){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		LOG.debug("addUser: "+regName);
		Transaction tx = null;
		try{
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser!=null){
				LOG.warn(regName+" exist, can't be added.");
				return BaseAxis.ACTIONFAILURE+" UserExistError";
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
					LOG.error("addUser "+regName+" fail: DefaultFileCopyError", e);
					return BaseAxis.ACTIONFAILURE+" DefaultConfigCopyError";
				} catch (JDOMException e) {
					LOG.error("addUser "+regName+" fail: DefaultFileParseError", e);
					return BaseAxis.ACTIONFAILURE+" DefaultFileParseError";
				}
				LOG.info("add User: "+regName+" success");
				return BaseAxis.ACTIONSUCCESS;
			}
			else{		
				LOG.error("add regUser: "+regName +"fail: DefaultConfigNotExistError");
				return BaseAxis.ACTIONFAILURE+" DefaultConfigNotExistError";
			}
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
			LOG.error("addUser fail",e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally{
			this.closeSession();
		}
	}
	public String setPassWord(String regName ,String passWord){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		LOG.debug("setPassWord: "+ regName);
		if (regName == null || passWord == null){
			LOG.debug("args can't be null regName: "+regName+" passWord: "+passWord);
			return BaseAxis.ACTIONFAILURE+" Args is null";
		}
		Transaction tx = null;
		try{
			tx = getTransaction();
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findByRegName(regName);
			if (regUser==null){
				return BaseAxis.ACTIONFAILURE+" UserNotExistError";
			}
			regUser.setPassWord(passWord);
			regUserDAO.save(regUser);
			tx.commit();
		}catch(Exception e){
			LOG.error("setPassWord fail Name: "+regName,e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		LOG.debug("setPassWord success Name: "+regName);
		return BaseAxis.ACTIONSUCCESS;
	}
}
