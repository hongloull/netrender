package com.webrender.axis;

import java.util.Iterator;

import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.CommandmodelargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.Commandmodelarg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

public class CommandmodelAxis extends BaseAxis {
	private static final Log log = LogFactory.getLog(CommandmodelAxis.class);
	
	/**
	 * 查看某个命令模板的详细信息
	 * @param commandModelId  模板Id
	 * @return XML形式的模板详细信息
	 */
	public String getCommandModel(String commandModelId)
	{
		// 权限判断
		
		try{
			if ( ! this.canVisit(7)){
				log.debug("VisitRight error");
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		log.debug("getCommandModel");
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
			log.debug("getCommandModel success");
			return XMLOut.outputToString(doc);
		}catch(Exception e)
		{
			log.error("getCommandModel error",e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
	}
	
	/**
	 * 所有模板简单列表
	 * @return XML的模板列表
	 */
	public String getCommandModels()
	{
		log.debug("getCommandModels");
		// 权限判断
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}		
		
		try{
			CommandmodelDAO cMDAO = new CommandmodelDAO();
			Element root = new Element("Commandmodels");
			Document doc = new Document(root);
			
			Iterator ite_CMS = cMDAO.findAll().iterator();
			while(ite_CMS.hasNext())
			{
				Commandmodel cM = (Commandmodel)ite_CMS.next();
				root.addContent(CommandmodelUtils.bean2xml(cM));
			}
			log.debug("getCommandModels success");
			return XMLOut.outputToString(doc);
		}catch(Exception e)
		{
			log.error("getCommandModels failure",e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
	}
}
