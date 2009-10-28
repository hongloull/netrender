package com.webrender.axis;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.ServerOperateImpl;
import com.webrender.dao.Operatelog;

public class ServerOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(ServerOperate.class);
	public String restartServer(){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		return (new ServerOperateImpl()).restartServer(this.getLoginUserId());
	}
	
	public String getPortsConfig(){
		if(getLoginUserId()==0) return BaseAxis.NOTLOGIN;	
		return (new ServerOperateImpl()).getPortsConfig();
	}
	public String setPortsConfig(String portsXML){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		return (new ServerOperateImpl()).setPortsConfig(portsXML,getLoginUserId());
	}
}
