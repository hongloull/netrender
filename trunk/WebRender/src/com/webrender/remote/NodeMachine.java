package com.webrender.remote;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.webrender.axis.beanxml.CommandUtils;
import com.webrender.axis.operate.ConfigOperateImpl;
import com.webrender.config.NetRenderLogFactory;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Quest;
import com.webrender.dao.StatusDAO;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.protocol.processor.IClientProcessor;
import com.webrender.server.ControlThreadServer;
import com.webrender.server.RealLogServer;
import com.webrender.server.deal.DealQuest;
import com.webrender.tool.NameMap;
/* 
 *  NodeMachine 控制节点机操作的类；不与数据库交互信息
 */
public class NodeMachine implements TimeoutOperate,IClientProcessor {
//	String command ;
	private int nodeId ;
	private Short pri;
	private Set<Integer> currentCommands ;
	private static final Log LOG = LogFactory.getLog(NodeMachine.class);
	private String configInfo = null;
//	private StringBuffer realLog = null;
	private FileOutputStream realLogS = null;
	private ServerMessages serverMessages = new ServerMessages();
	private CommandUtils commandUtils = new CommandUtils();
	private CommandDAO commandDAO = new CommandDAO();
	private NodeDAO nodeDAO = new NodeDAO();
//	private Date shellCommandTime = null;
//	private String shellCommand = null;
//	IResultStore resultStore;
	private static final int ERRORMAX = 5;
	private int errorNum = 0;

	private IoSession session = null;

	private boolean isReady = false;
	private boolean isConnect = false;
	private boolean isBusy = false;
	private boolean isRealTime = false;
	private boolean isPause = true;
	private NodeStatus status = null;
//	private TimeoutThread timeOutThread = null;
	private String version;
	
	
	@SuppressWarnings("unused")
	private NodeMachine(){		
	}
	
	public NodeMachine(Integer nodeId,Short pri)
	{
		LOG.debug("A New NodeMachine  nodeId:"+nodeId + " pri : "+ pri);
		this.nodeId = nodeId;
		this.pri = pri;
	    currentCommands = Collections.synchronizedSet(new HashSet<Integer>());;
	    status = new NodeStatus();
	    ConnectTest cTest = new ConnectTest(this);
	    cTest.setDaemon(true);
		cTest.start();
	}
	
	//TODO 节点端需要保证能讲渲染的最终状态发送会服务器
	/**
	 * 
	 */
	
	
	/**
	 * @return the pri
	 */
	public Short getPri() {
		return pri;
	}


	/**
	 * @param pri the pri to set
	 */
	public void setPri(Short pri) {
		this.pri = pri;
		if( NodeMachineManager.getInstance().containIdles(this) ){
			// ReOrder
			NodeMachineManager.getInstance().removeIdleMachines(this);
			NodeMachineManager.getInstance().addNodeMachines(this);
		}
	}


	public boolean execute(Command command)
	{
		CODE cmdType = null;
		if( command.getType()==null || NameMap.RENDER.equalsIgnoreCase(command.getType())){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_RENDER");
		}
		else if( (NameMap.GETFRAME).equalsIgnoreCase(command.getType()) ){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_GETFRAME");
		}
		else if( (NameMap.PRELIGHT).equalsIgnoreCase(command.getType()) ){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_PRELIGHT");
		}
		else if( (NameMap.ONETOMANY).equalsIgnoreCase(command.getType()) || (NameMap.MANYTOMANY).equalsIgnoreCase(command.getType()) ){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_SHELL");
		}
		else{ // 其他默认为Render
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_RENDER");
		}
		String cmdString = commandUtils.commandToXMLForExe(command);
		
		ByteBuffer cmdBuffer;
		try {
			cmdBuffer = serverMessages.createCommandPkt(cmdType,command.getCommandId(),cmdString);
		} catch (Exception e) {
			LOG.error("createCommandPkt fail",e);
			LOG.warn("nodeId: "+nodeId + " execute commandId: "+command.getCommandId()+ " createCommandpkt failure.");
			return false;
		}
//		LOG.info(Thread.currentThread().getId()+" nodeId: "+nodeId + " execute commandId: "+command.getCommandId());
		if( this.execute(cmdBuffer))
		{
			command.setSendTime(new Date());
			addCommand(command);
//			if (timeOutThread == null){
//				LOG.debug("timeOutThread == null startNew");
//				timeOutThread = new TimeoutThread(0,command.getCommandId(),this);
//				timeOutThread.start();
//			}
			LOG.info("nodeId: "+nodeId + " execute commandId: "+command.getCommandId()+ " success.");
			return true ;
		}
		else
		{
			LOG.warn("nodeId: "+nodeId + " execute commandId: "+command.getCommandId()+ " exe command failure.");
			return false;
		}
//		resultStore.storeResult();
	}
	public boolean exeShellCommand(String command ){
		CODE cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_SHELL");
		String cmdString = commandUtils.simpleCommandToXML(command);
		ByteBuffer cmdBuffer;
		try {
			cmdBuffer = serverMessages.createCommandPkt(cmdType,-1,cmdString);
		} catch (Exception e) {
			LOG.error("createCommandPkt fail",e);
			LOG.warn("nodeId: "+nodeId + " exeShellCommand command: "+ command + " createCommandpkt failure.");
			return false;
		}
		if( this.execute(cmdBuffer)){
//			shellCommandTime = new Date();
//			shellCommand = command;
			LOG.info("nodeId: "+nodeId+ " exeShellCommand success ");
			return true;
		}else{
//			shellCommandTime = null;
//			shellCommand = null;
			LOG.error("nodeId: "+nodeId+ " exeShellCommand fail time out ");
			return false;
		}
	}
	public Set<Integer> getCurrentCommands()
	{
		return this.currentCommands;
	}
	public void addCommand(Command command)
	{
		if (command==null) return;
		status.setJobName(command.getQuest().getQuestName());
		status.setJobId(command.getCommandId().toString());
		SimpleDateFormat   df= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		status.setSendTime(df.format(command.getSendTime())+"");
		status.setFrames(commandDAO.getNote(command).toString());
		this.currentCommands.add(command.getCommandId());
		setBusy(true);
	}
	
	public void removeCommandId(Integer commandId)
	{
		this.currentCommands.remove(commandId);
		if(currentCommands.size()==0) setBusy(false);
	}
		
	
	public boolean execute(ByteBuffer command)
	{
		LOG.debug("execute bytebuffer");
		LOG.debug(nodeId +": "+ command);
		if (this.isConnect==false) return false;
		else if (session==null) {
			LOG.warn("NodeId:"+nodeId+" IoSession is null");
			return false;
		}
		session.setAttribute("StartFlag",null);
		session.write(command);
    	for (int i = 0 ; i<500 ; i++)
    	{
    		try {
    			if (session.getAttribute("StartFlag")!=null)
    			{
    				LOG.debug(nodeId +" exe Success");
    				session.setAttribute("StartFlag",null);
    				return true;	
    			}
				Thread.sleep(10);
    		}catch (InterruptedException e) {
    			e.printStackTrace();
    		}catch(NullPointerException e){
//    			LOG.info("Node: "+ nodeId +" session null..");
    			break;
    		}
    	}
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
			try {
				if(session.isConnected()==true){
//					LOG.info("NodeId:"+nodeId+" getstatus");
					session.write(serverMessages.createStatusPkt());					
				}else{
					LOG.info("session is not connect close it");
					session.close();
				}
			} catch (Exception e) {
				LOG.error("testStatus fail",e);
			}
		}
		LOG.debug("TestStatus success");
	}

	private NodeStatus setStatusFromXML(NodeStatus status,String  in) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		File file=new File("WebRoot/WEB-INF/classes/status.xml");
//		Document doc=db.parse(file);
		LOG.debug("setStatusFromXML");
		StringBufferInputStream  is = new StringBufferInputStream (in); 
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
		private String platform="";
		private String priortiry;
		
		private String status;
		private String ramUsage="";
		private String cpuUsage="";
		private String jobId="";
		private String jobName="";
		private String frames="";
		private String sendTime="";
		
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
			return this.status+this.ramUsage+this.cpuUsage+this.jobName+this.frames+this.sendTime;
		}
		/**
		 * @return the startTime
		 */
		public String getSendTime() {
			return sendTime;
		}
		/**
		 * @param startTime the startTime to set
		 */
		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}
		/**
		 * @return the jobId
		 */
		public String getJobId() {
			return jobId;
		}
		/**
		 * @param jobId the jobId to set
		 */
		public void setJobId(String jobId) {
			this.jobId = jobId;
		}
	}
	


	public void setConnect(boolean isConnect) {
//		LOG.info(nodeId +":setConnect("+isConnect+")");
		this.isConnect = isConnect;
		if (isConnect==false)
		{
			setReady(false);
			//TODO waiting reconnect. go on. 
			cleanRunCommands("NodeId:"+nodeId +" disconnect",3);
		}else{
			selfCheck();			
		}
	}
	public boolean isConnect() {
		
		return isConnect;
	}
	

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		LOG.debug(nodeId +":setBusy("+isBusy+")");
		if (isBusy==false){
			status.setJobName("");
			status.setJobId("");
			status.setSendTime("");
			status.setFrames("");
		}
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
	/**
	 * set pauseStatus & reset errornum
	 * @param isPause
	 */
	public void setPause(boolean isPause) {
		errorNum = 0;
		if ( this.isPause == isPause) return;
		else this.isPause = isPause;
		selfCheck();
	}
	
	private void selfCheck()
	{
		if (isConnect() && isBusy()==false && isPause()==false && isReady()==true)
		{
			NodeMachineManager.getInstance().addNodeMachines(this);
		}
		else
		{
			NodeMachineManager.getInstance().removeIdleMachines(this);
		}
	}
	
	/**
	 * 
	 * @param message
	 * @param state 1:reinit 2:error 3:disconnect
	 */
	public void cleanRunCommands(String message,int state){
		Iterator<Integer> ite_CurrentCommands = this.currentCommands.iterator();
		while(ite_CurrentCommands.hasNext()){
			cleanRunCommand( ite_CurrentCommands.next(),message,state );
		}
	}
	/**
	 * 
	 * @param commandId
	 * @param message
	 * @param isReinit
	 * @return state 1:reinit 2:error 3:disconnect
	 */
	private boolean cleanRunCommand(int commandId,String message,int state)
	{
		LOG.debug("cleanRunCommand commandId:"+ commandId);
		if ( this.currentCommands.contains(commandId)){
			
			
			Transaction tx = null;
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				Command command = commandDAO.findById(commandId);
				if(command.getStatus().getStatusId()== 72 ){
					if(realLogS != null){
						realLogS.close();
						realLogS = null;
					}
					return true;
				}
				if(state==1){
					commandDAO.reinitCommand(command);					
				}else if(state==2){
					commandDAO.setError(command);
					
					errorNum++;
					if(errorNum>=ERRORMAX){ //  Continuous error setPause
						setPause(true);
					}
				}else if(state==3){
					commandDAO.setDisconnect(command);
					
				}
				saveRealLog(commandId,false,message);
				
				LOG.info("CommandID: "+command.getCommandId()+" clean from nodeId="+nodeId +" message:"+message);

				tx.commit();
				
				ControlThreadServer.getInstance().notifyResume();
//				Dispatcher.getInstance().exeCommands();
				
				return true;
			}catch(NullPointerException e){
				LOG.warn("NodeId: "+nodeId+": cleanCurrentCommand NullPointer commandId: "+commandId+" message:"+message);
				if(tx!=null){
					tx.rollback();
				}
				return false;
			}
			catch(Exception e){
				LOG.error("NodeId: "+nodeId+": cleanCurrentCommand Error commandId: "+commandId+" message:"+message,e);
				if(tx!=null){
					tx.rollback();
				}
				return false;
			}finally{
				this.removeCommandId(commandId);
			}
		}else{
			return true;
		}
	}
	
	
	/**
	 *
	 * @param commandId
	 * @return questId
	 */
	private Quest setFinish(int commandId){
		/*
		 * 接受到结束标记，根据IP地址获取节点机，查询该节点的CurrentCommands（表示该节点在执行Command的ID ，当前只有1个）
		 * 根据CommandId 查找对应数据库中的Command 将其设置成已完成状态 72
		 * 添加日志
		 * 从节点机的CurrentCommands移除CommandID
		 * 设置节点空闲。
		 */
		if ( this.currentCommands.contains(commandId)){
			
//			HibernateSessionFactory.closeSession();
			NodeDAO nodeDAO  = new NodeDAO();
			Transaction tx = null;
			try{
				Node node  = nodeDAO.findById(nodeId) ;
				
				tx = HibernateSessionFactory.getSession().beginTransaction();
				saveRealLog(commandId,true,"finish");
				Command command = commandDAO.findById(commandId);			
				StatusDAO statusDAO = new StatusDAO();				
				command.setNode(node);
				command.setStatus(statusDAO.findById(72)); //72->Finish
				commandDAO.attachDirty(command);
				tx.commit();
				LOG.info("NodeId: "+nodeId+" finish command: "+commandId);
				// reset error number.
				errorNum = 0;
				removeCommandId(commandId);
				
				return command.getQuest();
			}
			catch(NullPointerException e){
				LOG.error("finishCommand NullPointerException id:"+commandId);
				return null;
			}
			catch(Exception e)
			{
				LOG.error("finish command: "+commandId+" Error",e);
				if (tx != null) 
				{
					tx.rollback();
				}
				// kill command
				return null;
			}
		}
		else{
//			LOG.info("NodeId:"+nodeId+" can not finish command:"+commandId+"!  node's current commands doesn't contain this command.");
			return null;
		}
//		setBusy(false);
		
	}
	/**
	 * 
	 * @param commandId  
	 * @param isNormal  true:Normal false:Error
	 * @param message
	 */
	private synchronized void  saveRealLog(int commandId ,boolean isNormal,String message){
		LOG.debug("saveRealLog");
		
		try{
//			int realStatusId = isNormal?80:81;
			int exeStatusId  = isNormal?91:99;	
			Command command = commandDAO.findById(commandId);
			if(command == null){
				return;
			}
			StatusDAO statusDAO = new StatusDAO();
			NodeDAO nodeDAO = new NodeDAO();
			ExecutelogDAO exeDAO = new ExecutelogDAO();
			if (realLogS!=null) // 有则存之，无则无视
			{
				realLogS.close();
				realLogS = null;
			}
			
			Executelog exelog  = new Executelog(command,statusDAO.findById(exeStatusId),nodeDAO.findById(nodeId),commandDAO.getNoteWithID(command) + message,new Date());
			exeDAO.save(exelog);
			LOG.debug("saveRealLog success");
		}catch(Exception e)
		{
			LOG.error("saveRealLog fail",e);
			
		}
//		finally
//		{
////			LOG.info("realLog reset");
//			realLog = null;					
//			
//			
//		}
		
		
	}

	public void timeOutOperate(Object obj) {
		// 超时  认为改命令执行出错
		LOG.info("timethread timeOut! commandId: "+obj);
//		timeOutThread.cancel();
	//	saveRealLog( (Integer)obj,false);
		this.cleanRunCommand((Integer)obj,"Timeout",1);
		if (this.currentCommands.size()==0) setBusy(false);
//		timeOutThread = null;
	}


	public synchronized void addFeedBack(Integer commandId, String message) {
//		LOG.info("addFeedBack commandId:"+commandId + " message: "+message);
		if (  this.isRealTime() )
		{
			RealLogServer.getInstance().broadCast(nodeId+"***"+message);
		}
		
		if(commandId == null || commandId == 0){
			//TODO 应该报错 待测试
			LOG.error("node: "+nodeId+" commandId :"+commandId+" message: "+message);
//			if(currentCommands.iterator().hasNext()){
//				commandId = this.currentCommands.iterator().next();				
//			}
//			if (commandId == null || commandId == 0){
//				return;
//			}
		}
		else if( commandId == -1 ){
			return;
						
		}
		else if(! this.currentCommands.contains(commandId) ){
			LOG.error("NodeId:"+nodeId+" get new commandId:"+commandId+ " message:"+message);
			return;
//			this.addCommandId(commandId);
		}
		try{
//			if (timeOutThread == null){
//				timeOutThread = new TimeoutThread(0,commandId,this);
//				timeOutThread.start();
//			}			
			if("***GOODBYE***".endsWith(message)){
//				LOG.info("Node: "+nodeId+" GOODBYE");
//				timeOutThread.cancel();
				if(realLogS!=null){
					realLogS.close();
					realLogS = null;
				}
				if(commandId != -1){
					setFinish(commandId);					
				}
//				timeOutThread = null;
			}
			else{
				if(realLogS == null){
					File file = null;
//					if(commandId != -1){
					file = NetRenderLogFactory.getInstance().getFile(commandId);
//					}else{
//						file = NetRenderLogFactory.getInstance().getExeLog(nodeId,shellCommandTime);	
//					}
					
					if(file.getParentFile().exists() || file.getParentFile().mkdirs()){
						realLogS = new FileOutputStream(file);
//						LOG.info("new log file:"+file.getAbsolutePath());						
					}else{
						LOG.error("new log file error "+file.getAbsolutePath());
					}
				}
				realLogS.write(message.getBytes());				
				
//				realLog.append(message);
				// 重置超时时间
//				LOG.debug("timethread reset");
//				timeOutThread.reset();
			}			
		}catch(NullPointerException e){
			LOG.error("AddFeadBack NullPointerException commandId:"+commandId);
		}catch (IOException e) {
			LOG.error("parse log file fail IOException commandId:"+commandId);
		}
	}

	public void ready() {
		if(isReady()==false){
			setReady(true);
			this.selfCheck();
		}
		session.setAttribute("StartFlag","Success");		
	}

	public Integer getId() {
		
		return nodeId;
	}


	public void setSession(IoSession session2) {
		
		if(this.session!=null && session!=session2){
//			LOG.info("NodeMachine Id="+nodeId+" get a session2.");
			if(session2!=null){
				LOG.warn("set a different session ");
				LOG.warn("NodeId: "+nodeId + "      close a session info isClosed:"+session.isClosing()+" isConnected: "+session.isConnected()+" createTime:"+session.getCreationTime()+" remoteAddress"+session.getRemoteAddress());
				LOG.warn("NodeId: "+nodeId + "          new session info isClosed:"+session2.isClosing()+" isConnected: "+session2.isConnected()+" createTime:"+session.getCreationTime()+" remoteAddress"+session.getRemoteAddress());
			}
			session.close();
//			LOG.info("NodeId: "+nodeId + " drop the old session isClosed:"+session.isClosing()+" isConnected: "+session.isConnected()+" createTime:"+session.getCreationTime());
			session=null;
		}
		
		this.session = session2;
		if(session !=null && session.isClosing()==false){
			setConnect(true);
		}
		else{
//			LOG.info("NodeMachine Id="+nodeId+" disconnect session="+session);
			Transaction tx = null;
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				StatusDAO statusDAO = new StatusDAO();
				ExecutelogDAO exeDAO = new ExecutelogDAO();
				Node node = nodeDAO.findById(this.nodeId);
				Executelog log = new Executelog(null,statusDAO.findById(91),node,node.getNodeIp()+"---"+node.getNodeName()+": disconnect..",new Date());
				exeDAO.save(log);
				tx.commit();
			}catch(Exception e){
				LOG.error("record disconnect nodeId:"+nodeId,e);
			}
			setConnect(false);
		}
		
	}
	public IoSession getSession(){
		return session;
	}


	public void updateConfig(String configString) {
		setConfigInfo(configString);
	}


	public String getConfigInfo() {
		return configInfo;
	}
	public boolean sendPathCongfigToNode(){
		String pathConfig = (new ConfigOperateImpl()).getPathConfig();
		try {
			return execute(serverMessages.createPathConfigPkt(pathConfig));
			
		} catch (Exception e) {
			LOG.error("sendPathCongfigToNode fail",e);
			return false;
		}
		
	}

	public void setConfigInfo(String configInfo) {
		this.configInfo = configInfo;
	}

	public void parseDatas(CODE code, byte[] fmts, List<String> datas){
		if( fmts.length!= datas.size() ){
			if(isConnect()){
				LOG.error("nodeId:"+nodeId+" get the wrong format datas.");
				return ;
			}
		}
		
		try{
			if(code == null){
				LOG.error("code is null");
				return ;
			}else if(code.getId() == EOPCODES.getInstance().get("N_FEEDBACK").getId()){
				int commandId =Integer.parseInt(datas.get(0));
				String mesString = datas.get(1);
				addFeedBack(commandId,mesString);
			}else if(code.getId() == EOPCODES.getInstance().get("N_STATUSINFO").getId()){
				String statusString = datas.get(0);
				updateStatus(statusString);
			}else if ( code.getId() == EOPCODES.getInstance().get("N_SUCCESS").getId() ){
				ready();
			}else if(code.getId() == EOPCODES.getInstance().get("N_CONFIGINFO").getId()){
				String configString = datas.get(0);
				updateConfig(configString);
			}else if(code.getId() == EOPCODES.getInstance().get("N_FRAMEINFO").getId()){
				if (datas.size()==3){
					String commandId = datas.get(0);
					String frames = datas.get(1);
					String byFrame = datas.get(2);
					LOG.info("GETFRAME commandId: "+commandId+". frames: "+frames+". by: "+byFrame);
					int intCommandId = Integer.parseInt(commandId);
//					this.addFeedBack(intCommandId,"frames: "+frames+". by: "+byFrame );
//					Quest quest = setFinish(intCommandId);
					(new DealQuest()).makeQuestFrames(intCommandId, frames, byFrame);
				}
				else{
					LOG.error("N_FRAMEINFO need 4 arguments :"+ datas);
				}
			}else if(code.getId() == EOPCODES.getInstance().get("N_GETPATHCONFIG").getId()){
				LOG.debug("send path config");
				sendPathCongfigToNode();
			}else if( code.getId() == EOPCODES.getInstance().get("N_PRELIGHT").getId() ){
				String commandId = datas.get(0);
				String preLight = datas.get(1);
				LOG.info("GetPreLight  commandId:"+commandId+" preLight:"+preLight);
				int intCommandId = Integer.parseInt(commandId);
				(new DealQuest()).setPreLight(intCommandId, preLight);
				
//				LOG.info("GetPreLight from nodeId: "+ nodeId +" commandId: "+commandId + " preLight:"+preLight);
//				this.addFeedBack(intCommandId, preLight);
////				LOG.info("addPreLight to realLog from nodeId: "+ nodeId);
//				Quest quest = setFinish(intCommandId);
//				LOG.info("setFinish from nodeId: "+ nodeId);
			}else if(code.getId() == EOPCODES.getInstance().get("N_ERROR").getId() ){
				int commandId =Integer.parseInt(datas.get(0));
				String message = datas.get(1);
				this.addFeedBack(commandId, message);
				this.cleanRunCommand(commandId, message, 2);
			}else{
				// send not type 
			}
						
		}catch(Exception e){
			LOG.error("execute buffer fail",e);
		}
	}

	/**
	 * @return the isReady
	 */
	public boolean isReady() {
		return isReady;
	}

	/**
	 * @param isReady the isReady to set
	 */
	public void setReady(boolean isReady) {
//		LOG.info("Set Node Ready nodeId: "+ nodeId);
		this.isReady = isReady;
	}

	public void initial(String version, int state, int commandId) {
		this.version = version;
				
		if(state == EOPCODES.getInstance().get("N_RUN").getSubCode("N_PAUSE").getId()){
			return;
		}else if( state == EOPCODES.getInstance().get("N_RUN").getSubCode("N_IDLE").getId() ){
			setPause(false);
		}else  if(commandId != 0){
			Command command = commandDAO.findById(commandId);
			if (command.getStatus().getStatusId()!=74 || command.getNode().getNodeId()!=nodeId){
				LOG.warn("NodeId "+nodeId+ " try to set up commandId:"+commandId + " " + command.getStatus().getValue()+" executed by NodeId:"+nodeId);
				return ;
			}
			if( state == EOPCODES.getInstance().get("N_RUN").getSubCode("N_EXE").getId()){
				LOG.info("NodeId:"+nodeId+ " get execut symbol commandId "+commandId);
				this.addCommand(command);
			}else if(state == EOPCODES.getInstance().get("N_RUN").getSubCode("N_FINISH").getId()){
				LOG.info("NodeId:"+nodeId+ " get finish symbol commandId "+commandId);
				this.setFinish(commandId);			
			}else if(state == EOPCODES.getInstance().get("N_RUN").getSubCode("N_ERROR").getId()){
				LOG.info("NodeId:"+nodeId+ " get error symbol commandId "+commandId);
				this.cleanRunCommand(commandId,"node "+nodeId+" send error to commandId "+ commandId, 2);
			}
			setPause(false);
		} 
		
	}
}
