package com.webrender.axis.operate;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.ExecutelogUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.NetRenderLogFactory;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.Operatelog;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.server.ControlThreadServer;

public class CommandOperateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(CommandOperateImpl.class);
	private CommandDAO commandDAO = new CommandDAO();
	
	
//	public String getRealLogs(String commandId){
//		
//		LOG.debug("getRealLogs");
//		
//		try{
//			ExecutelogDAO exeLogDAO = new ExecutelogDAO();
//			Command command = commandDAO.findById(Integer.parseInt(commandId) );
//			Element root = new Element("Reallogs");
//			Document doc =new Document(root);
//			Executelog log  = exeLogDAO.getRealLog(command);
//			if(log!=null){
//				root.addContent( (new ExecutelogUtils()).bean2xml(log) );				
//			}
//			LOG.debug("getRealLog success");
//		//	XMLOut.outputToFile(doc,new File("d:/reallog.xml") );
//			return (new XMLOut()).outputToString(doc);
//		}catch(NullPointerException e){
//			LOG.error("getRealLog NullPointerException commandId:"+commandId);
//			return ACTIONFAILURE+e.getMessage();
//		}catch(NumberFormatException e){
//			LOG.error("getRealLog NumberFormatException commandId:"+commandId);
//			return ACTIONFAILURE+e.getMessage();
//		}
//		catch(Exception e)
//		{
//			LOG.error("getRealLog fail",e);
//			return ACTIONFAILURE+e.getMessage();
//		}
//		finally
//		{
//			this.closeSession();
//		}
//	}
	
	public String getRealLogFile(String commandId){	
		LOG.debug("getRealLogFile");
		try{
			File file = NetRenderLogFactory.getInstance().getFile(Integer.parseInt(commandId));
			if(file != null && file.exists()){
				StringBuffer buffer = new StringBuffer();
				FileInputStream in=new FileInputStream (file); 
				InputStreamReader inReader=new InputStreamReader (in); 
				BufferedReader bufReader=new BufferedReader(inReader);
				String line = null;
				while(true){
					line = bufReader.readLine();
					if(line!=null){
						if(line.length()==0) continue;
						buffer.append(line).append("\n");
					}
					else break;
				}
//				LOG.info(buffer.toString());				
//				return buffer.toString();
				ExecutelogUtils utils = new ExecutelogUtils();
				Date date = new Date();
				SimpleDateFormat dateFormat =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date.setTime(file.lastModified());
				Element element = utils.arg2xml(buffer.toString(),"",dateFormat.format(date), "");
				Element root = new Element("Reallogs");
				Document doc =new Document(root);
				root.addContent(element);
				String result = (new XMLOut()).outputToString(doc);
				return result;
			}else{
				LOG.error("Log file does not exist! for commandId "+commandId);
				return BaseOperate.ACTIONFAILURE+"Log file does not exist! for commandId "+commandId;
			}
		}catch(NullPointerException e){
			LOG.error("getRealLogFile NullPointerException commandId:"+commandId);
			return ACTIONFAILURE+e.getMessage();
		}catch(NumberFormatException e){
			LOG.error("getRealLogFile NumberFormatException commandId:"+commandId);
			return ACTIONFAILURE+e.getMessage();
		}catch(Exception e){
			LOG.error("getRealLogFile fail ",e);
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
		
	}
	
	public String reinitCommand(String commandId,int regUserId){

		LOG.debug("reinitCommand");
		
		Transaction tx = null;
//		boolean killFlag = true;
		try{
			tx = getTransaction();
			Command command = commandDAO.findById(Integer.parseInt(commandId));
			commandDAO.reinitCommand(command );
			logOperate(regUserId,Operatelog.MOD,"ReInit Command: "+commandDAO.getNoteWithID(command),Operatelog.COMMAND,command.getCommandId(),null);				
			tx.commit();
			
//			Dispatcher.getInstance().exeCommands();
			LOG.debug("reinitCommand success");
			
			ControlThreadServer.getInstance().notifyResume();
			return ACTIONSUCCESS;
		}catch(NullPointerException e){
			LOG.error("reinitCommand NullPointerException commandId:"+commandId);
			return ACTIONFAILURE+e.getMessage(); 
		}catch(NumberFormatException e){
			LOG.error("reinitCommand NumberFormatException commandId:"+commandId);
			return ACTIONFAILURE+e.getMessage();
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

		
		LOG.debug("setFinish command:"+commandId);
		
		Transaction tx = null;
		try{
			tx = getTransaction();
			Command command = commandDAO.findById(Integer.parseInt(commandId));
			commandDAO.setFinish(command );
			logOperate(regUserId,Operatelog.MOD,"setFinish Command: "+commandDAO.getNoteWithID(command),Operatelog.COMMAND,command.getCommandId(),null);
			tx.commit();
		
			LOG.debug("finishCommand success");
			return ACTIONSUCCESS;
		}catch(NullPointerException e){
			LOG.error("setFinish null commandId:"+commandId);
			return ACTIONFAILURE+e.getMessage(); 
		}catch(NumberFormatException e){
			LOG.error("setFinish NumberFormatException commandId:"+commandId);
			return ACTIONFAILURE+e.getMessage();
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
	
	public String getFrame(String commandId){
		LOG.debug("getFrame commandId:"+commandId);
		commandDAO.findById(Integer.parseInt(commandId) );
		try{
			String framesValue = null;
		}catch(Exception e){
			
		}
		return null;
	}
	
}
