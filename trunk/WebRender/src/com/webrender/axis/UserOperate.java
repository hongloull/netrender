package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.UserOperateImpl;



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
