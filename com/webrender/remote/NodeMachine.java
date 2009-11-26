package com.webrender.remote;



import java.io.IOException;
import java.io.StringBufferInputStream;
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

import com.webrender.axis.beanxml.CommmandUtils;
import com.webrender.axis.operate.ConfigOperateImpl;
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
	private StringBuffer realLog = null;
	private ServerMessages serverMessages = new ServerMessages();
	private CommmandUtils commmandUtils = new CommmandUtils();
//	IResultStore resultStore;


	private IoSession session = null;

	private boolean isConnect = false;
	private boolean isBusy = false;
	private boolean isRealTime = false;
	private boolean isPause = false;
	private NodeStatus status = null;
//	private TimeoutThread timeOutThread = null;
	
	
	private NodeMachine(){		
	}
	public NodeMachine(Integer nodeId,Short pri)
	{
		this.nodeId = nodeId;
		this.pri = pri;
	    currentCommands = Collections.synchronizedSet(new HashSet<Integer>());;
	    status = new NodeStatus();
	    ConnectTest cTest = new ConnectTest(this);
	    cTest.setDaemon(true);
		cTest.start();
	}
	
	
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
	}


	public boolean execute(Command command)
	{
		LOG.info("nodeId: "+nodeId + " execute commandId: "+command.getCommandId());
		CODE cmdType = null;
		if( command.getType()==null || NameMap.RENDER.equalsIgnoreCase(command.getType())){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_RENDER");
		}
		else if( (NameMap.GETFRAME).equalsIgnoreCase(command.getType()) ){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_GETFRAME");
		}
		else if( (NameMap.ONETOMANY).equalsIgnoreCase(command.getType()) || (NameMap.MANYTOMANY).equalsIgnoreCase(command.getType()) ){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_SHELL");
		}
		else if( (NameMap.PRELIGHT).equalsIgnoreCase(command.getType()) ){
			cmdType = EOPCODES.getInstance().get("S_COMMAND").getSubCode("S_PRELIGHT");
		}
		String cmdString = commmandUtils.commandToXMLForExe(command);
		
		ByteBuffer cmdBuffer;
		try {
			cmdBuffer = serverMessages.createCommandPkt(cmdType,command.getCommandId(),cmdString);
		} catch (Exception e) {
			LOG.error("createCommandPkt fail",e);
			return false;
		}
		if( this.execute(cmdBuffer))
		{
			addCommandId(command.getCommandId());
			status.setJobName(command.getQuest().getQuestName());
//			if (timeOutThread == null){
//				LOG.debug("timeOutThread == null startNew");
//				timeOutThread = new TimeoutThread(0,command.getCommandId(),this);
//				timeOutThread.start();
//			}
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
		
	
	public synchronized boolean execute(ByteBuffer command)
	{
		LOG.debug("execute bytebuffer");
		LOG.debug(nodeId +": "+ command);
		if (this.isConnect==false) return false;
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
    			LOG.info("Node: "+ nodeId +" disconnect..");
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
				session.write(serverMessages.createStatusPkt());
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
//			NodeMachineManager.getInstance()
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
			NodeMachineManager.getInstance().addNodeMachines(this);
		}
		else
		{
			NodeMachineManager.getInstance().removeIdleMachines(this);
		}
	}
	
	
	public void cleanRunCommands(String message){
		Iterator<Integer> ite_CurrentCommands = this.currentCommands.iterator();
		while(ite_CurrentCommands.hasNext()){
			cleanRunCommand( ite_CurrentCommands.next(),message,true);
		}
	}
	
	private boolean cleanRunCommand(int commandId,String message,boolean isReinit)
	{
		LOG.debug("cleanRunCommand commandId:"+ commandId);
		if ( this.currentCommands.contains(commandId)){
			
			
			Transaction tx = null;
			CommandDAO commandDAO = new CommandDAO();
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				Command command = commandDAO.findById(commandId);
				if(command.getStatus().getStatusId()== 72 ){
					if(realLog != null && realLog.length()>0){
						realLog = null;
					}
					return true;
				}
				if(isReinit){
					commandDAO.reinitCommand(command);					
				}else{
					commandDAO.setError(command);
				}
				saveRealLog(commandId,false,message);
				
				LOG.info("CommandID: "+command.getCommandId()+" clean from nodeId="+nodeId);

				tx.commit();
				
				
				ControlThreadServer.getInstance().notifyResume();
//				Dispatcher.getInstance().exeCommands();
				
				return true;
			}catch(Exception e){
				LOG.error("NodeId: "+nodeId+": cleanCurrentCommand Error commandId: "+commandId,e);
				tx.rollback();
				return false;
			}finally{
				this.removeCommandId(commandId);
			}
		}
		return true;
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
		
		LOG.debug("setFinish("+commandId+")");
		if ( this.currentCommands.contains(commandId)){
			
			HibernateSessionFactory.closeSession();
			NodeDAO nodeDAO  = new NodeDAO();
			Transaction tx = null;
			try{
				Node node  = nodeDAO.findById(nodeId) ;
				
				tx = HibernateSessionFactory.getSession().beginTransaction();
				saveRealLog(commandId,true,"finish");
				CommandDAO commandDAO = new CommandDAO();
				Command command = commandDAO.findById(commandId);			
				StatusDAO statusDAO = new StatusDAO();
				if (command !=null)
				{
					command.setNode(node);
					command.setStatus(statusDAO.findById(72)); //72->Finish
					command.setSendTime(new Date());
					commandDAO.attachDirty(command);							
					LOG.info(nodeId+" finish command "+command.getCommandId());
				}
				tx.commit();
				LOG.debug("setFinish success");
				removeCommandId(commandId);
				return command.getQuest();
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
			LOG.info("NodeId:"+nodeId+" can not finish command:"+commandId+"!  node's current commands doesn't contain this command.");
			return null;
		}
//		setBusy(false);
		
	}
	
	private synchronized void  saveRealLog(int commandId ,boolean isNormal,String message){
		LOG.debug("saveRealLog");
		
		try{
			int realStatusId = isNormal?80:81;
			int exeStatusId  = isNormal?91:99;				
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(commandId);
			if(command == null){
				return;
			}
			StatusDAO statusDAO = new StatusDAO();
			NodeDAO nodeDAO = new NodeDAO();
			ExecutelogDAO exeDAO = new ExecutelogDAO();
			if (realLog!=null && realLog.length()>0) // 有则存之，无则无视
			{
				Executelog reallog = new Executelog(command,statusDAO.findById(realStatusId),nodeDAO.findById(nodeId),realLog.toString(),new Date());
				exeDAO.save(reallog);		
			}
			
			Executelog exelog  = new Executelog(command,statusDAO.findById(exeStatusId),nodeDAO.findById(nodeId),commandDAO.getNoteWithID(command) + message,new Date());
			exeDAO.save(exelog);
			LOG.debug("saveRealLog success");
		}catch(Exception e)
		{
			LOG.error("saveRealLog fail",e);
			
		}finally
		{
//			LOG.info("realLog reset");
			realLog = null;					
			
			
		}
		
		
	}

	public void timeOutOperate(Object obj) {
		// 超时  认为改命令执行出错
		LOG.info("timethread timeOut! commandId: "+obj);
//		timeOutThread.cancel();
	//	saveRealLog( (Integer)obj,false);
		this.cleanRunCommand((Integer)obj,"Timeout",true);
		if (this.currentCommands.size()==0) setBusy(false);
//		timeOutThread = null;
	}


	public synchronized void addFeedBack(Integer commandId, String message) {
//		LOG.info("addFeedBack"+message);
		if (  this.isRealTime() )
		{
			RealLogServer.getInstance().broadCast(nodeId+"***"+message);
		}
		if(commandId == null || commandId == 0){
			
			// 目前 commandId 传过来都是null
			if(currentCommands.iterator().hasNext()){
				commandId = this.currentCommands.iterator().next();				
			}
			if (commandId == null || commandId == 0){
				LOG.error("addRealLog error commandId :"+commandId);
				return;
			}
		}
		else if(! this.currentCommands.contains(commandId) ){
			LOG.info("NodeId:"+nodeId+" get new commandId:"+commandId);
			this.addCommandId(commandId);
		}
		try{
//			if (timeOutThread == null){
//				timeOutThread = new TimeoutThread(0,commandId,this);
//				timeOutThread.start();
//			}
			if(realLog == null) realLog = new StringBuffer();
			
			
			if(message.startsWith("***GOODBYE***")){
				LOG.info("addReadlog GOODBYE");
//				timeOutThread.cancel();
				
				setFinish(commandId);
//				timeOutThread = null;
			}
			else{
				realLog.append(message);
				// 重置超时时间
				LOG.debug("timethread reset");
//				timeOutThread.reset();
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
					LOG.info("commandId: "+commandId+". frames: "+frames+". by: "+byFrame);
					int intCommandId = Integer.parseInt(commandId);
					this.addFeedBack(intCommandId,"frames: "+frames+". by: "+byFrame );
					Quest quest = setFinish(intCommandId);
					(new DealQuest()).makeQuestFrames(quest, frames, byFrame);
				}
				else{
					LOG.error("N_FRAMEINFO need 4 arguments :"+ datas);
				}
			}else if(code.getId() == EOPCODES.getInstance().get("N_GETPATHCONFIG").getId()){
				LOG.info("send path config");
				sendPathCongfigToNode();
			}else if( code.getId() == EOPCODES.getInstance().get("N_PRELIGHT").getId() ){
				String commandId = datas.get(0);
				String preLight = datas.get(1);
				int intCommandId = Integer.parseInt(commandId);
				this.addFeedBack(intCommandId, preLight);
				Quest quest = setFinish(intCommandId);
				(new DealQuest()).setPreLight(quest, preLight);
			}else if(code.getId() == EOPCODES.getInstance().get("N_ERROR").getId() ){
				int commandId =Integer.parseInt(datas.get(0));
				String message = datas.get(1);
				this.addFeedBack(commandId, message);
				this.cleanRunCommand(commandId, message, false);
				
			}else{
				// send not type 
			}
						
		}catch(Exception e){
			LOG.error("execute buffer fail",e);
			
		}
	}
}
