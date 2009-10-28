package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.axis.operate.PoolOperateImpl;
import com.webrender.config.GenericConfig;
import com.webrender.config.NodeXMLConfig;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.tool.FileCopy;

public class PoolOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(PoolOperate.class);
	
	public String addPool(String name){
		
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		return (new PoolOperateImpl()).addPool(name,this.getLoginUserId());
	}
	
	public String modPool(String name ,String questXML){
		
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		return (new PoolOperateImpl()).modPool(name, questXML, this.getLoginUserId());
	}
	
	public String getPoolConfig(String name){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		return (new PoolOperateImpl()).getPoolConfig(name);
	}
	
	public String delPool(String name){
		if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
		
		return (new PoolOperateImpl()).delPool(name, this.getLoginUserId());
	}
	
	public String getPools()
	{
		int regUserId = this.getLoginUserId();
		if ( regUserId ==0 )	return BaseAxis.NOTLOGIN;
		return (new PoolOperateImpl()).getPools(regUserId, this.canVisit(0));
	}
	
	public String getNodes(){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new PoolOperateImpl()).getNodes();
		}
	
}
