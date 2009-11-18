package com.webrender.axis.operate;


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
import com.webrender.server.ControlThreadServer;
import com.webrender.server.Dispatcher;

public class CommandOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(CommandOperateImpl.class);
	
	public String getRealLogs(String commandId){
		
		LOG.debug("getRealLogs");
		
		try{
			CommandDAO commandDAO = new CommandDAO();
			ExecutelogDAO exeLogDAO = new ExecutelogDAO();
			Command command = (Command)commandDAO.findById(Integer.parseInt(commandId) );
			Element root = new Element("Reallogs");
			Document doc =new Document(root);
			Executelog log  = exeLogDAO.getRealLog(command);
			if(log!=null){
				root.addContent( (new ExecutelogUtils()).bean2xml(log) );				
			}
			LOG.debug("getRealLog success");
		//	XMLOut.outputToFile(doc,new File("d:/reallog.xml") );
			return (new XMLOut()).outputToString(doc);
		}catch(Exception e)
		{
			LOG.error("getRealLog fail",e);
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	public String getRealLogFile(String commandId){		
		return ACTIONFAILURE;
	}
	
	public String reinitCommand(String commandId,int regUserId){

		LOG.debug("reinitCommand");
		
		Transaction tx = null;
		try{
			tx = getTransaction();
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(Integer.parseInt(commandId));
			commandDAO.reinitCommand(command );
			logOperate(regUserId,Operatelog.MOD,"ReInit Command: "+commandDAO.getNoteWithID(command));
			tx.commit();
//			Dispatcher.getInstance().exeCommands();
			LOG.debug("reinitCommand success");
			
			ControlThreadServer.getInstance().notifyResume();
			return ACTIONSUCCESS;
		}catch(Exception e){
			LOG.error("reinitCommand fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String setFinish(String commandId , int regUserId){

		
		LOG.debug("finishCommand");
		Transaction tx = null;
		try{
			tx = getTransaction();
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(Integer.parseInt(commandId));
			commandDAO.setFinish(command );
			logOperate(regUserId,Operatelog.MOD,"setFinish Command: "+commandDAO.getNoteWithID(command));
			tx.commit();
		
			LOG.debug("finishCommand success");
			return ACTIONSUCCESS;
		}catch(Exception e){
			LOG.error("finishCommand fail",e);
			if (tx != null)
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	
	
}
