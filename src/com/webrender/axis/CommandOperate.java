package com.webrender.axis;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.ExecutelogUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.server.ControlThreadServer;

public class CommandOperate extends BaseAxis {
	private static final Log log = LogFactory.getLog(CommandOperate.class);
	
	public String getRealLogs(String commandId){
		
		// 权限判断
		try{
			if (! this.canVisit(7) ){
				return BaseAxis.RightError;
			}
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		log.debug("getRealLogs");
		Transaction tx = null;
		try{
			CommandDAO commandDAO = new CommandDAO();
			ExecutelogDAO exeLogDAO = new ExecutelogDAO();
			Command command = (Command)commandDAO.findById(Integer.parseInt(commandId) );
			Iterator ite_Executelogs = exeLogDAO.getRealLog(command).iterator();
			Element root = new Element("Reallogs");
			Document doc =new Document(root);
			while(ite_Executelogs.hasNext()){
				Executelog log = (Executelog)ite_Executelogs.next();
					root.addContent( ExecutelogUtils.bean2xml(log) );
			}
			log.debug("getRealLog success");
			return XMLOut.outputToString(doc);
		}catch(Exception e)
		{
			log.error("getRealLog fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
	}
	
	
	public String reinitCommand(String commandId){
			
		log.debug("reinitCommand");
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(commandId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		Transaction tx = null;
		try{
			tx = getTransaction();
			CommandDAO commandDAO = new CommandDAO();
			commandDAO.reinitCommand( commandDAO.findById(Integer.parseInt(commandId)) );
			tx.commit();
			ControlThreadServer.getInstance().resume();
			log.debug("reinitCommand success");
			return BaseAxis.ActionSuccess;
		}catch(Exception e){
			log.error("reinitCommand fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
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
