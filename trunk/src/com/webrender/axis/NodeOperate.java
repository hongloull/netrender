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
	
	private static final Log log = LogFactory.getLog(NodeOperate.class);
	
	public String pauseNode(String nodeIp)
	{
		log.debug("pauseNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeIp);
			nodeMachine.setPause(true);
			log.debug("pauseNode success");
			return BaseAxis.ActionSuccess;			
		}catch(Exception e){
			log.error("pauseNode", e);
			return BaseAxis.ActionFailure;
		}
	}
	public String resumeNode(String nodeIp)
	{
		log.debug("resumeNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		try{
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeIp);
			nodeMachine.setPause(false);
			log.debug("resumeNode success");
			return BaseAxis.ActionSuccess;			
		}catch(Exception e){
			log.error("resumeNode fail");
			return BaseAxis.ActionFailure;
		}
	}
	public String setRealLog(String nodeIp,int isOpen){
		log.debug("setRealLog");
		try{
			if ( ! this.canVisit(7)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		try{
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeIp);
		nodeMachine.setRealTime(isOpen==1?true:false);
		log.debug("setRealLog success");
		return BaseAxis.ActionSuccess;
		}catch(Exception e)
		{
			log.error("setRealLog fail" ,e);
			return BaseAxis.ActionFailure;
		}
	}
	public String killCommand(String nodeIp)
	{
		log.debug("killCommand");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			NodeDAO nodeDAO = new NodeDAO();
			Node node = nodeDAO.findByNodeIp(nodeIp) ;
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(node.getNodeIp());
			boolean flag = nodeMachine.execute("***KILL***");
			CommandDAO commandDAO = new CommandDAO();
			StatusDAO statusDAO = new StatusDAO();
			if (flag)
			{
				nodeMachine.cleanRunCommands();
			}
			else
			{
				Executelog executelog = new  Executelog(null,statusDAO.findById(99),node,"killError",new Date());
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				exeDAO.save(executelog);
				nodeMachine.setPause(true);
				return BaseAxis.ActionFailure;
			}
			tx.commit();
			nodeMachine.setBusy(false);
			ControlThreadServer.getInstance().resume();
			log.debug("killCommand success");
			return BaseAxis.ActionSuccess;
		}
		
		catch(Exception e)
		{
			log.error("killCommand error",e);
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
	public String  shutdownNode(String nodeIp ,int Flag)
	{
		// FLAG  0 shutdown  1 reboot 2 soft restart
		log.debug("shutdownNode");
		try{
			if ( ! this.canVisit(5)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		if (Flag==0)  //shutdown
		{
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(nodeIp);
			return nodeMachine.execute("***SYSTEM***shutdown")? BaseAxis.ActionSuccess : BaseAxis.ActionFailure;
		}
		else if (Flag==1)
		{
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(nodeIp);
			return nodeMachine.execute("***SYSTEM***reboot")? BaseAxis.ActionSuccess : BaseAxis.ActionFailure;
		}
		else if (Flag == 2 )
		{
			NodeMachine nodeMachine  = NodeMachineManager.getNodeMachine(nodeIp);
			return nodeMachine.execute("***SYSTEM***softrestart")? BaseAxis.ActionSuccess : BaseAxis.ActionFailure;
		}
		return BaseAxis.ActionFailure;
	}
	
	
	
	
}
