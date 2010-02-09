package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.ModelOperateImpl;

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

		return (new ModelOperateImpl()).getModels(regUserId,canVisit(0));
	}
}
