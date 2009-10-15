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
import com.webrender.dao.Operatelog;
import com.webrender.server.Dispatcher;

public class CommandOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(CommandOperate.class);
	
	public String getRealLogs(String commandId){
		
//      权限判断
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
		
		
		LOG.debug("getRealLogs");
		
		try{
			CommandDAO commandDAO = new CommandDAO();
			ExecutelogDAO exeLogDAO = new ExecutelogDAO();
			Command command = (Command)commandDAO.findById(Integer.parseInt(commandId) );
			Element root = new Element("Reallogs");
			Document doc =new Document(root);
		
			Executelog log  = exeLogDAO.getRealLog(command);
			if(log!=null){
				root.addContent( ExecutelogUtils.bean2xml(log) );				
			}
			LOG.debug("getRealLog success");
		//	XMLOut.outputToFile(doc,new File("d:/reallog.xml") );
			return XMLOut.outputToString(doc);
		}catch(Exception e)
		{
			LOG.error("getRealLog fail",e);
						
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	
	public String reinitCommand(String commandId){
			
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) )
			return BaseAxis.RIGHTERROR;
			
		

		LOG.debug("reinitCommand");
		
		Transaction tx = null;
		try{
			tx = getTransaction();
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(Integer.parseInt(commandId));
			commandDAO.reinitCommand(command );
			logOperate(this.getLoginUserId(),Operatelog.MOD,"ReInit Command: "+commandDAO.getNote(command));
			tx.commit();
//			ControlThreadServer.getInstance().resume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("reinitCommand success");
			return BaseAxis.ACTIONSUCCESS;
		}catch(Exception e){
			LOG.error("reinitCommand fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String setFinish(String commandId){
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) )
			return BaseAxis.RIGHTERROR;
		
		
		LOG.debug("finishCommand");
		Transaction tx = null;
		try{
			tx = getTransaction();
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(Integer.parseInt(commandId));
			commandDAO.setFinish(command );
			logOperate(this.getLoginUserId(),Operatelog.MOD,"setFinish Command: "+commandDAO.getNote(command));
			tx.commit();
		
			LOG.debug("finishCommand success");
			return BaseAxis.ACTIONSUCCESS;
		}catch(Exception e){
			LOG.error("finishCommand fail",e);
			if (tx != null)
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE+e.getMessage();
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
