package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.PoolOperateImpl;

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
