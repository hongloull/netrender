package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.BaseOperate;
import com.webrender.axis.operate.ConfigOperateImpl;

public class ConfigOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(ConfigOperate.class);
	
	public String getPathConfig(){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new ConfigOperateImpl()).getPathConfig();
		
	}
	
	public String setPathConfig(String questXML){
		
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		return (new ConfigOperateImpl()).setPathConfig(questXML,getLoginUserId());
		
	}
	
	public String getNodeConfig(String nodeId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new ConfigOperateImpl()).getNodeConfig(nodeId);
	}
	
	public String setNodeConfig(String[] nodeIds,String config){
		if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
		ConfigOperateImpl configOperateImpl = new ConfigOperateImpl();
		StringBuffer result = new StringBuffer();
		String subResult = null;
		for(String nodeId:nodeIds){
			subResult = configOperateImpl.setNodeConfig(nodeId,config,this.getLoginUserId());
			if(subResult.startsWith(BaseOperate.ACTIONFAILURE)){
				result.append(nodeId).append(":").append(subResult).append("\n\r");
			}
		}
		if(result.length()==0){
			result.append(BaseOperate.ACTIONSUCCESS);
		}
		return result.toString();
	}
	
}
