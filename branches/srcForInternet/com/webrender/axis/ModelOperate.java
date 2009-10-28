package com.webrender.axis;

import java.util.Iterator;

import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.CommandmodelargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.axis.operate.ModelOperateImpl;
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
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return (new ModelOperateImpl()).getModel(commandModelId);
	}
	
	/**
	 * 所有模板简单列表
	 * @return XML的模板列表
	 */
	public String getModels()
	{
		int regUserId = this.getLoginUserId();
		if ( regUserId == 0 )	return BaseAxis.NOTLOGIN;

		return (new ModelOperateImpl()).getModels(regUserId);
	}
}
