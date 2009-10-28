package com.webrender.axis;


import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.ExecutelogUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.axis.operate.CommandOperateImpl;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.Operatelog;
import com.webrender.server.Dispatcher;

public class CommandOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(CommandOperate.class);
	
	public String getRealLogs(String commandId){
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return  (new CommandOperateImpl()).getRealLogs(commandId);
	}
	public String getRealLogFile(String commandId){
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		return BaseAxis.ACTIONFAILURE;
	}
	
	public String reinitCommand(String commandId){
			
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) )
			return BaseAxis.RIGHTERROR;
		
		return  (new CommandOperateImpl()).reinitCommand(commandId,getLoginUserId());
		
	}
	
	public String setFinish(String commandId){
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) )
			return BaseAxis.RIGHTERROR;
		
		return  (new CommandOperateImpl()).setFinish(commandId,getLoginUserId());
		
	}
	
	
	@Override
	protected boolean isSelf(int commandId) {
		CommandDAO commandDAO = new CommandDAO();
		if ( this.getLoginUserId() == commandDAO.findById(commandId).getQuest().getReguser().getRegUserId() ){
			return true;
		}
		return false;
	}
	
}
