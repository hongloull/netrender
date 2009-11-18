package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.axis.operate.BaseOperate;
import com.webrender.axis.operate.ConfigOperateImpl;
import com.webrender.bean.nodeconfig.NodeConfig;
import com.webrender.bean.nodeconfig.NodeConfigUtils;
import com.webrender.config.GenericConfig;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Operatelog;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

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
