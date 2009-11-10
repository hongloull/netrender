package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.QuestargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.logic.CalcFrame;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;
import com.webrender.server.ControlThreadServer;

public class NodeOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(NodeOperate.class);
	
	public String pauseNode(String nodeIp)
	{
		LOG.debug("pauseNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeIp);
			String result = this.killCommand(nodeIp);
			nodeMachine.setPause(true);
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"Pause "+nodeIp);
			tx.commit();
			if(BaseAxis.ACTIONSUCCESS.equals(result)){
				LOG.debug("pauseNode success");
				return BaseAxis.ACTIONSUCCESS;							
			}
			else{
				LOG.error("pauseNode fail killCommandError");
				nodeMachine.setPause(true);
				return "KillCommandError";
			}
		}catch(Exception e){
			LOG.error("pauseNode fail", e);
			if(tx!=null){
				tx.rollback();
			}
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}
	public String resumeNode(String nodeIp)
	{
		LOG.debug("resumeNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeIp);
			nodeMachine.setPause(false);
			LOG.debug("resumeNode success");
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"Resume "+nodeIp);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;			
		}catch(Exception e){
			LOG.error("resumeNode fail");
			if(tx!=null){
				tx.rollback();
			}
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}
	public String setRealLog(String nodeIp,int isOpen){
		LOG.debug("setRealLog");
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		Transaction tx = null;
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeIp);
			nodeMachine.setRealTime(isOpen==1?true:false);
			LOG.debug("setRealLog success");
			tx = getTransaction();
			logOperate(getLoginUserId(),Operatelog.MOD,"SetRealTime "+(isOpen==1?"open":"close") );
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		}catch(Exception e)
		{
			LOG.error("setRealLog fail" ,e);
			if(tx!=null){
				tx.rollback();
			}
			return BaseAxis.ACTIONFAILURE;
		}finally{
			this.closeSession();
		}
	}
	public String killCommand(String nodeIp)
	{
		LOG.debug("killCommand");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findByNodeIp(nodeIp) ;
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeIp());
			boolean flag = nodeMachine.execute("***KILL***");
			//CommandDAO commandDAO = new CommandDAO();
			logOperate(getLoginUserId(),Operatelog.MOD,"kill Commands in "+nodeIp );
			StatusDAO statusDAO = new StatusDAO();
			if (flag)
			{
				nodeMachine.cleanRunCommands("Service kill "+nodeIp+"'s Commands");
			}
			else
			{
				Executelog executelog = new  Executelog(null,statusDAO.findById(99),node,"killError",new Date());
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				exeDAO.save(executelog);
				nodeMachine.setPause(true);
				return BaseAxis.ACTIONFAILURE;
			}
			tx.commit();
			nodeMachine.setBusy(false);
//			ControlThreadServer.getInstance().resume();
			LOG.debug("killCommand success");
			return BaseAxis.ACTIONSUCCESS;
		}
		
		catch(Exception e)
		{
			LOG.error("killCommand error",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  shutdownNode(String nodeIp ,int Flag)
	{
		// FLAG  0 shutdown  1 reboot 2 soft restart
		LOG.debug("shutdownNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RIGHTERROR;
			}			
		}catch(Exception e){
			LOG.error("RightVisit error",e);
			return BaseAxis.RIGHTERROR;
		}
		Transaction tx =null;
		boolean exeFlag = false;
		String message = "";
		try{
			tx = getTransaction();
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(nodeIp);
			if (Flag==0)  //shutdown
			{
				if( nodeMachine.execute("***SYSTEM***shutdown"))
				{
					exeFlag =true;
					message = "shutdown "+nodeIp;
				}
				else{
					message = "shutdown "+nodeIp+" fail!";
				}
			}
			else if (Flag==1) // reboot
			{
				if( nodeMachine.execute("***SYSTEM***reboot")){
					exeFlag = true;
					message = "reboot "+nodeIp;
				}
				else{
					message = "reboot "+nodeIp+" fail!";
				}
			}
			else if (Flag == 2 )// soft restart
			{
				if( nodeMachine.execute("***SYSTEM***softrestart")){
					exeFlag = true;
					message = "soft restart "+nodeIp;
				}else{
					message = "soft restart "+nodeIp+" fail!";
				}
			}
			logOperate(getLoginUserId(),exeFlag?Operatelog.MOD:Operatelog.DEL,message);
			tx.commit();
			return exeFlag?ACTIONSUCCESS:ACTIONFAILURE;
		}catch(Exception e){
			
			return ACTIONFAILURE;
		}finally{
			closeSession();
		}
	}
	
	
	
	
}