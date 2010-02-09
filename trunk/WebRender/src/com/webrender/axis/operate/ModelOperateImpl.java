package com.webrender.axis.operate;

import java.util.Iterator;

import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.CommandmodelargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

public class ModelOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(ModelOperateImpl.class);
	private XMLOut xmlOut = new XMLOut();
	private CommandmodelUtils commandmodelUtils = new CommandmodelUtils();
	/**
	 * 查看某个命令模板的详细信息
	 * @param commandModelId  模板Id
	 * @return XML形式的模板详细信息
	 */
	public String getModel(String commandModelId)
	{
		LOG.debug("getCommandModel");
		try {
			CommandmodelDAO cMDAO = new CommandmodelDAO();
			Commandmodel cm = cMDAO.findById(Integer.parseInt(commandModelId));
			Element root = commandmodelUtils.bean2xml(cm);
			Document doc = new Document(root);
			Iterator ite_CMA = cm.getCommandmodelargs().iterator();
			while (ite_CMA.hasNext()) {
				Commandmodelarg cma = (Commandmodelarg) ite_CMA.next();
				// Final参数不显示给Explorer。
				if ( 65 == cma.getStatus().getStatusId()) continue;
				Element ele_CMA = (new CommandmodelargUtils()).bean2xml(cma);
				root.addContent(ele_CMA);
			}
			LOG.debug("getCommandModel success");
			return xmlOut.outputToString(doc);
		}catch(NumberFormatException e){
			LOG.error("getModel NumberFormatException ModelId:"+commandModelId);
			return ACTIONFAILURE+e.getMessage(); 
		}
		catch(Exception e)
		{
			LOG.error("getCommandModel error",e);
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
	
	/**
	 * 所有模板简单列表
	 * @return XML的模板列表
	 */
	public String getModels(int regUserId,boolean isAdmin)
	{
		LOG.debug("getCommandModels");
		try{
			
			
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findById(regUserId);
			if(regUser==null) return ACTIONFAILURE+": Reguser = null";
			
			Element root = new Element("Commandmodels");
			Document doc = new Document(root);
			Iterator ite_CMS = null;
			if(isAdmin){
				ite_CMS = (new CommandmodelDAO()).findAll().iterator();
			}else{
				ite_CMS = regUser.getModels().iterator();
			}
			
			while(ite_CMS.hasNext())
			{
				Commandmodel cM = (Commandmodel)ite_CMS.next();
				root.addContent(commandmodelUtils.bean2xml(cM));
			}
			LOG.debug("getCommandModels success");
			return xmlOut.outputToString(doc);
		}catch(Exception e)
		{
			LOG.error("getCommandModels failure",e);
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
}
