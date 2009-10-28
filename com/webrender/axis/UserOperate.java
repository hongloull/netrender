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
import com.webrender.axis.operate.UserOperateImpl;
import com.webrender.config.GenericConfig;
import com.webrender.config.UserXMLConfig;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.tool.FileCopy;



public class UserOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(UserOperate.class);
	
	public String getUsersList(){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		return (new UserOperateImpl()).getUsersList();
	}

	
	public String getUserConfig(String regName){		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		return (new UserOperateImpl()).getUserConfig(regName);
	}
	public String modUserConfig(String userName,String questXML){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		return (new UserOperateImpl()).modUserConfig(userName, questXML,this.getLoginUserId() );
	}
	public String delUser(String regName){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		return (new UserOperateImpl()).delUser(regName, this.getLoginUserId() );
	}
	public String addUser(String regName ,String passWord){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		return (new UserOperateImpl()).addUser(regName, passWord,this.getLoginUserId() );
	}
	public String setPassWord(String regName ,String passWord){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		return (new UserOperateImpl()).setPassWord(regName, passWord,this.getLoginUserId());
	}
}
