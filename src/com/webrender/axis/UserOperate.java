package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	private static final Log log = LogFactory.getLog(UserOperate.class);
	
	public String getUsersList()
	{
		log.debug("getUserConfig");
		try {
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
			log.debug("getUsersList success");
			return XMLOut.outputToString(doc);
		} catch (Exception e) {
			log.error("getUsersList ",e);
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	
	public String getUserConfig(String userName){
		log.debug("getUserConfig");
		
		String userFile = GenericConfig.getInstance().getFile("users/"+userName+".xml");
		

//		if (!file.canWrite()) throw new Exception("UsersXML ReadOnly");
		File file = new File(userFile);
		if(!file.exists()) return "UserNameNotExistError";
		SAXBuilder sb =  new SAXBuilder();
		Document document = null;
		try {
			document = sb.build(file);
			return XMLOut.outputToString(document);
		} catch (Exception e) {
			log.error("getUserConfig fail userName: "+userName, e);
			return BaseAxis.ActionFailure;
		}finally{
			this.closeSession();
		}
	}
	public String modUserConfig(String userName,String questXML){
		log.debug("modUserConfig");
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
		} catch (JDOMException e) {
			log.warn("moduserConfig fail:questXMLParseError userName:"+userName);
			return "XMLParseError";
		}finally{
			this.closeSession();
		}
		return BaseAxis.ActionSuccess;
	}
	
	public String addUser(String regName ,String passWord){
		log.debug("addUser");
		
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findByRegName(regName);
		if (regUser!=null) return "UserExistError";
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
				log.error("addUser fail: DefaultFileCopyError", e);
				return "DefaultConfigCopyError";
			} catch (JDOMException e) {
				log.error("addUser fail: DefaultFileParseError", e);
				e.printStackTrace();
			}finally{
				this.closeSession();
			}
			return BaseAxis.ActionSuccess;
		}
		else{		
			this.closeSession();
			return "DefaultConfigNotExistError";
		}
	}

}
