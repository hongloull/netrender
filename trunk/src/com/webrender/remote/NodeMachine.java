package com.webrender.remote;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.webrender.axis.beanxml.XMLOut;
import com.webrender.config.GenericConfig;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.protocol.processor.IClientProcessor;
import com.webrender.server.ControlThreadServer;
import com.webrender.tool.StrOperate;

/* 
 *  NodeMachine 控制节点机操作的类；不与数据库交互信息
 */
public class NodeMachine implements TimeoutOperate,IClientProcessor {
//	String command ;
	private int nodeId ;
	private Set<Integer> currentCommands ;
	private static final Log LOG = LogFactory.getLog(NodeMachine.class);
	private StringBuffer realLog = new StringBuffer();
//	IResultStore resultStore;


	private IoSession session = null;

	private boolean isConnect = false;
	private boolean isBusy = false;
	private boolean isRealTime = false;
	private boolean isPause = false;
	private NodeStatus status = null;
	private TimeoutThread timeOutThread = null;
	
	
	
	public NodeMachine(Integer nodeId)
	{
		this.nodeId = nodeId;
	    currentCommands = Collections.synchronizedSet(new HashSet<Integer>());;
	    status = new NodeStatus();
	    ConnectTest cTest = new ConnectTest(this);
		cTest.start();
	}
	
	
	public boolean execute(Command command)
	{
		LOG.info("nodeId: "+nodeId + " execute commandId: "+command.getCommandId());
		String cmdString = this.getCommand(command);
		ByteBuffer cmdBuffer =ServerMessages.createCommandPkt(cmdString);
		if( this.execute(cmdBuffer))
		{
			addCommandId(command.getCommandId());
			status.setJobName(command.getQuest().getQuestName());
			if (timeOutThread == null){
				LOG.debug("timeOutThread == null startNew");
				timeOutThread = new TimeoutThread(0,command.getCommandId(),this);
				timeOutThread.start();
			}
			return true ;
		}
		else
		{
			
			return false;
		}
//		resultStore.storeResult();
	}

	public Set<Integer> getCurrentCommands()
	{
		return this.currentCommands;
	}
	public void addCommandId(Integer commandId)
	{
		this.currentCommands.add(commandId);
		setBusy(true);
	}
	
	public void removeCommandId(Integer commandId)
	{
		this.currentCommands.remove(commandId);
		if(currentCommands.size()==0) setBusy(false);
	}
		
	public String getCommand(Command command)
	{
		org.jdom.Element root = new org.jdom.Element("Cmd");
		root.addAttribute("cmdModelName",command.getQuest().getCommandmodel().getCommandModelName());
		HashSet<Integer> set_ids = new HashSet<Integer>();
		
		Iterator ite_CommandArgs = command.getCommandargs().iterator();
		while(ite_CommandArgs.hasNext())
		{
			
			Commandarg commandArg = (Commandarg)ite_CommandArgs.next();
			Commandmodelarg cMDArg = commandArg.getCommandmodelarg();
			set_ids.add(cMDArg.getCommandModelArgId());
			org.jdom.Element element = null;
			if (commandArg.getCommandmodelarg().getStatus().getStatusId()==64){
				element = new org.jdom.Element("Endarg");
			}
			else element = new org.jdom.Element("Cmdarg");
//			element.addAttribute("cmdModelArgId", cMDArg.getCommandModelArgId().toString());
//			element.addAttribute("argInstruction", cMDArg.getArgInstruction());
			element.addAttribute("argName", cMDArg.getArgName());
//			element.addAttribute("type",cMDArg.getType().toString());
			element.addAttribute("value", commandArg.getValue());
//	        element.addAttribute("statusId",commandArg.getCommandmodelarg().getStatus().getStatusId().toString());
			root.addContent(element);
		}
		Iterator ite_Questargs = command.getQuest().getQuestargs().iterator();
		while(ite_Questargs.hasNext())
		{
			Questarg questArg = (Questarg)ite_Questargs.next();
			Commandmodelarg cMDArg = questArg.getCommandmodelarg();
			if ( set_ids.contains( cMDArg.getCommandModelArgId() ) )
			{
				continue;
			}
			else {
				org.jdom.Element element = null;
				if (questArg.getCommandmodelarg().getStatus().getStatusId()==64){
					element = new org.jdom.Element("Endarg");
				}
				else element = new org.jdom.Element("Cmdarg");
//				element.addAttribute("commandModelArgId", cMDArg.getCommandModelArgId().toString());	
//				element.addAttribute("argInstruction", cMDArg.getArgInstruction());
				element.addAttribute("argName", cMDArg.getArgName());
//				element.addAttribute("type",cMDArg.getType().toString());
				element.addAttribute("value", questArg.getValue());	
				root.addContent(element);
			}
		}
		
		
		
//		StringBuilder result = new StringBuilder();
//		result.append(" ").append( command.getCommand() ).append(" ");
//		QuestargDAO questArgDAO = new QuestargDAO();
//		Iterator<Questarg> constantArgs = questArgDAO.getConstantArgs(command.getQuest()).iterator();
//		while(constantArgs.hasNext())
//		{
//			Questarg arg = constantArgs.next();
//			String argName = arg.getCommandmodelarg().getArgName();
//			String argValue = arg.getValue();
//			result.append(argName).append(" ").append(argValue).append(" ");
//		}
//		root.addAttribute("content",result.toString() );
		org.jdom.Document doc = new org.jdom.Document(root);
		return XMLOut.outputToString(doc);
	}
	public boolean execute(ByteBuffer command)
	{
		LOG.debug("execute");
		LOG.debug(nodeId +": "+ command);
		if (this.isConnect==false) return false;
		session.write(command);
    	for (int i = 0 ; i<300 ; i++)
    	{
    		if (session.getAttribute("StartFlag")!=null)
    		{
    			LOG.info(nodeId +" command is START");
    			session.setAttribute("StartFlag",null);
    			return true;	
    		}
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
		LOG.info("execute error");
		return false;
	}
	public void updateStatus(String message)
	{
		LOG.debug("updateStatus");
		try {
			setStatusFromXML(status,message);
			LOG.debug("updateStatus success nodeId: "+this.nodeId);
		} catch (Exception e){
			LOG.error(nodeId +": XMLException:"+message,e);
		}
	}
	public NodeStatus getStatus()
	{
		LOG.debug("getStatus nodeId: "+this.nodeId);
		
//		LOG.info("status.JobName:"+status.getJobName());
		return status;
	}
	public void testStatus(){
		LOG.debug("TestStatus");
		if( this.isConnect()==false ){
			status.setStatus("DISCONNECT");
		}
		else{
			status.setStatus("CONNECT");
			session.write(ServerMessages.createStatusPkt());
		}
		LOG.debug("TestStatus success");
	}

	private NodeStatus setStatusFromXML(NodeStatus status,String  in) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		File file=new File("WebRoot/WEB-INF/classes/status.xml");
//		Document doc=db.parse(file);
		LOG.debug("setStatusFromXML");
		StringBufferInputStream is = new StringBufferInputStream(in); 
		Document doc=db.parse(is);
		Element root=doc.getDocumentElement();
		status.setHostName(root.getAttribute("hostName"));
		status.setCpuUsage(root.getAttribute("cpuUsage"));
		status.setRamUsage(root.getAttribute("ramUsage"));
		status.setPlatform(root.getAttribute("platform"));
//		status.setStatus (root.getAttribute("cpuUsage"));
		LOG.debug("setStatusFromXML success");
		return status;
	}
	
	public class NodeStatus{
		private String hostName;
		private String platform;
		private String priortiry;
		
		private String status;
		private String ramUsage;
		private String cpuUsage;
		private String jobName;
		private String frames;
		private String log;
		
		public String getCpuUsage() {
			return cpuUsage;
		}
		public void setCpuUsage(String cpuUsage) {
			this.cpuUsage = cpuUsage;
		}
		public String getFrames() {
			return frames;
		}
		public void setFrames(String frames) {
			this.frames = frames;
		}
		public String getHostName() {
			return hostName;
		}
		public void setHostName(String hostName) {
			this.hostName = hostName;
		}
		public String getJobName() {
			return jobName;
		}
		public void setJobName(String jobName) {
			this.jobName = jobName;
		}
		public String getLog() {
			return log;
		}
		public void setLog(String log) {
			this.log = log;
		}
		public String getPlatform() {
			return platform;
		}
		public void setPlatform(String platform) {
			this.platform = platform;
		}
		public String getPriortiry() {
			return priortiry;
		}
		public void setPriortiry(String priortiry) {
			this.priortiry = priortiry;
		}
		public String getRamUsage() {
			return ramUsage;
		}
		public void setRamUsage(String ramUsage) {
			this.ramUsage = ramUsage;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String toString(){
			return this.status+this.ramUsage+this.cpuUsage+this.jobName+this.frames+this.log;
		}
	}
	


	public void setConnect(boolean isConnect) {
		LOG.info(nodeId +":setConnect("+isConnect+")");
		this.isConnect = isConnect;
		if (isConnect==false)
		{
			cleanRunCommands("NodeId:"+nodeId +" disconnect");
		}
		selfCheck();			
	}
	public boolean isConnect() {
		
		return isConnect;
	}
	

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		
		LOG.debug(nodeId +":setBusy("+isBusy+")");
		if (isBusy==false) status.setJobName("");
		this.isBusy = isBusy;
		selfCheck();
	}

	public boolean isRealTime() {
		return isRealTime;
	}

	public void setRealTime(boolean isRealTime) {
		this.isRealTime = isRealTime;
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
		selfCheck();
	}
	
	private void selfCheck()
	{
		if (isConnect() && isBusy()==false && isPause()==false )
		{
			if(NodeMachineManager.idleMachines.contains(this)==false){
				LOG.debug(nodeId +" Add to IdleMachines ");
				NodeMachineManager.idleMachines.add(this);
			}
			else{
				LOG.debug(nodeId+" is in IdleMachines ");
			}
		}
		else
		{
			LOG.debug(nodeId+" remove from IdleMachines ");
			NodeMachineManager.idleMachines.remove(this);
		}
	}
	
	
	public void cleanRunCommands(String message){
		Iterator<Integer> ite_CurrentCommands = this.currentCommands.iterator();
		while(ite_CurrentCommands.hasNext()){
			cleanRunCommand( ite_CurrentCommands.next(),message);
		}
	}
	
	private boolean cleanRunCommand(int commandId,String message)
	{
		LOG.debug("cleanRunCommand commandId:"+ commandId);
		if ( this.currentCommands.contains(commandId)){
			
			this.saveRealLog(commandId,false,message);
			
			Transaction tx = null;
			CommandDAO commandDAO = new CommandDAO();
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();

				Command command = commandDAO.findById(commandId);
				commandDAO.reinitCommand(command);
//				ControlThreadServer.getInstance().resume();
				LOG.info("CommandID: "+command.getCommandId()+" reinit");

				tx.commit();
				this.removeCommandId(commandId);
				return true;
			}catch(Exception e){
				LOG.error(nodeId+": cleanCurrentCommand Error",e);
				tx.rollback();
				return false;
			}
		}
		return true;
	}
	
	
	
	private void setFinish(int commandId){
		/*
		 * 接受到结束标记，根据IP地址获取节点机，查询该节点的CurrentCommands（表示该节点在执行Command的ID ，当前只有1个）
		 * 根据CommandId 查找对应数据库中的Command 将其设置成已完成状态 72
		 * 添加日志
		 * 从节点机的CurrentCommands移除CommandID
		 * 设置节点空闲。
		 */
		LOG.debug("setFinish("+commandId+")");

		HibernateSessionFactory.closeSession();
		NodeDAO nodeDAO  = new NodeDAO();
		Transaction tx = null;
		try{
			Node node  = nodeDAO.findById(nodeId) ;
			
			tx = HibernateSessionFactory.getSession().beginTransaction();
			
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(commandId);
			//Command command = null;
//			List list = commandDAO.getCurrentCommand(node);
//			System.out.println(ip+" currentCommandNum "+list.size());
//			if ( list.size()==1 )
//			{
//				command = (Command)list.get(0);
//			}
			
			StatusDAO statusDAO = new StatusDAO();
			if (command !=null)
			{
//				node.setStatus(statusDAO.findById(41)); //41 -> idle
//				nodeDAO.attachDirty(node);
				command.setNode(node);
				command.setStatus(statusDAO.findById(72)); //72->Finish
				command.setSendTime(new Date());
				commandDAO.attachDirty(command);							
				LOG.info(nodeId+" finish command");
			}
//			else
//			{
//				/*改错功能：遇到查找不到该节点在执行哪条Command情况时。但收到GOODBYE
//				 *         将节点设为闲置（41） 节点上运行的未完成的任务状态设置成Input（70）
//				 *         日志记录重置节点机
//				 */
//
//				Iterator ite_ListCommands = list.iterator();
//				ExecutelogDAO exeDAO = new ExecutelogDAO();
//				Executelog executelog = null;
//				while(ite_ListCommands.hasNext())
//				{
//					command = (Command) ite_ListCommands.next();
//				//	command.setNode(node);
//					command.setStatus(statusDAO.findById(70)); //72->input
//					commandDAO.attachDirty(command);			
//					executelog = new  Executelog(command,statusDAO.findById(99),node,"Redo",new Date()); 
//					exeDAO.save(executelog);
//				}
//				
//			}
			tx.commit();
		}
		catch(Exception e)
		{
			LOG.error("FinishCommandError",e);
			if (tx != null) 
			{
				tx.rollback();
			}
			// kill command
		}
//		setBusy(false);
		LOG.debug("setFinish success");
	}
	
	private synchronized void  saveRealLog(int commandId ,boolean isNormal,String message){
		LOG.debug("saveRealLog");
		if(this.currentCommands.contains(commandId))
		{
			Transaction tx = null;
			try{
				int realStatusId = isNormal?80:81;
				int exeStatusId  = isNormal?91:99;
				tx = HibernateSessionFactory.getSession().beginTransaction();				
				CommandDAO commandDAO = new CommandDAO();
				Command command = commandDAO.findById(commandId);
				StatusDAO statusDAO = new StatusDAO();
				NodeDAO nodeDAO = new NodeDAO();
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				
				Executelog reallog = new Executelog(command,statusDAO.findById(realStatusId),nodeDAO.findById(nodeId),realLog.toString(),new Date());
				Executelog exelog  = new Executelog(command,statusDAO.findById(exeStatusId),nodeDAO.findById(nodeId),commandDAO.getNote(command) + message,new Date());
				exeDAO.save(exelog);
				exeDAO.save(reallog);		
				tx.commit();
				LOG.debug("saveRealLog success");
			}catch(Exception e)
			{
				LOG.error("saveRealLog fail",e);
				if (tx != null)
				{
					tx.rollback();
				}
			}finally
			{
				HibernateSessionFactory.closeSession();
				LOG.info("realLog renew");
				realLog = new StringBuffer();
				removeCommandId(commandId);
			}
		}
	}

	public void timeOutOperate(Object obj) {
		// 超时  认为改命令执行出错
		LOG.info("timethread timeOut! commandId: "+obj);
		timeOutThread.cancel();
	//	saveRealLog( (Integer)obj,false);
		this.cleanRunCommand((Integer)obj,"Timeout");
		if (this.currentCommands.size()==0) setBusy(false);
		timeOutThread = null;
	}


	public synchronized void addFeedBack(int commandId, String message) {
		LOG.debug("addRealLog");
		if(message==null || ! this.currentCommands.contains(commandId) ){
			LOG.error("addRealLog commandId error");
			return;
		}
		try{
			if (timeOutThread == null){
				LOG.info("timeOutThread == null startNew");
				timeOutThread = new TimeoutThread(0,commandId,this);
				timeOutThread.start();
			}
			realLog.append(message).append("\r\n");
			
			if(message.startsWith("***GOODBYE***")){
				LOG.info("addReadlog goodBye");
				timeOutThread.cancel();
				saveRealLog(commandId,true,"finish");
				setFinish(commandId);
				timeOutThread = null;
			}
			else{
				// 重置超时时间
				LOG.debug("timethread reset");
				timeOutThread.reset();
			}			
		}catch(Exception e)
		{
			
		}
	}

	public void ready() {
		session.setAttribute("StartFlag","Success");		
	}

	public Integer getId() {
		
		return nodeId;
	}


	public void setSession(IoSession session2) {
		this.session = session2;
		if(session !=null && session.isClosing()==false){
			setConnect(true);
		}
		else{
			setConnect(false);
		}
		
	}
}
