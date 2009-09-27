package com.webrender.axis;

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

public class ModelOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(ModelOperate.class);
	
	/**
	 * 查看某个命令模板的详细信息
	 * @param commandModelId  模板Id
	 * @return XML形式的模板详细信息
	 */
	public String getModel(String commandModelId)
	{
		// 权限判断
		
//		try{
//			if ( ! this.canVisit(7)){
//				LOG.debug("VisitRight error");
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
		LOG.debug("getCommandModel");
		try {
			CommandmodelDAO cMDAO = new CommandmodelDAO();
			Commandmodel cm = cMDAO.findById(Integer.parseInt(commandModelId));
			Element root = CommandmodelUtils.bean2xml(cm);
			Document doc = new Document(root);
			Iterator ite_CMA = cm.getCommandmodelargs().iterator();
			while (ite_CMA.hasNext()) {
				Commandmodelarg cma = (Commandmodelarg) ite_CMA.next();
				Element ele_CMA = CommandmodelargUtils.bean2xml(cma);
				root.addContent(ele_CMA);
			}
			LOG.debug("getCommandModel success");
			return XMLOut.outputToString(doc);
		}catch(Exception e)
		{
			LOG.error("getCommandModel error",e);
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
	}
	
	/**
	 * 所有模板简单列表
	 * @return XML的模板列表
	 */
	public String getModels()
	{
		LOG.debug("getCommandModels");
		
		// 权限判断
//		try{
//			if ( ! this.canVisit(7)){
//				return BaseAxis.RightError;
//			}			
//		}catch(Exception e){
//			log.error("RightVisit error",e);
//			return BaseAxis.RightError;
//		}		
		try{
			
			int regUserId = this.getLoginUserId();
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findById(regUserId);
			if(regUser==null) return RIGHTERROR;
			
			Element root = new Element("Commandmodels");
			Document doc = new Document(root);
			
			Iterator ite_CMS = (new CommandmodelDAO()).findAll().iterator(); 
				
			
			while(ite_CMS.hasNext())
			{
				Commandmodel cM = (Commandmodel)ite_CMS.next();
				root.addContent(CommandmodelUtils.bean2xml(cM));
			}
			LOG.debug("getCommandModels success");
			return XMLOut.outputToString(doc);
		}catch(Exception e)
		{
			LOG.error("getCommandModels failure",e);
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
	}
}
