package com.webrender.config;

import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;

public class NetRenderLogFactory {
	private static String logPath = null;;
	private static final Log LOG = LogFactory.getLog(NetRenderLogFactory.class);
	private static NetRenderLogFactory instance = null;
	public static synchronized NetRenderLogFactory getInstance() {
		if (instance==null){
			instance = new NetRenderLogFactory();
			logPath = GenericConfig.getInstance().getFile("");
			int index = logPath.lastIndexOf("config/");
			if (index!=-1){
				logPath = logPath.substring(0,index);
				logPath = logPath+"reports/";
			}
			else{
				logPath = logPath+"reports/";
			}
		}
		return instance;
	}
	public File getFile(Integer commandId) {
		try{
			CommandDAO commandDAO = new CommandDAO();
			Command command = commandDAO.findById(commandId);
			StringBuffer fileName = new StringBuffer(logPath);
			fileName.append("jobs/");
			fileName.append(command.getQuest().getQuestId()).append("/").append(command.getCommandId()).append(".log");
			LOG.info("GetRealLogFile :"+fileName.toString());
			return new File(fileName.toString());
		}catch(NullPointerException e){
			LOG.warn("GetRealLogFile NullPointerException commandId:"+commandId);
			return null;
		}
	}
//	public File getExeLog(Integer nodeId,Date date){
//		StringBuffer fileName = new StringBuffer(logPath);
//		fileName.append("nodes/");
//		fileName.append(nodeId).append("/").append(date.getTime()).append(".log");
//		LOG.info("GetExeLogFolder :"+fileName.toString());
//		return new File(fileName.toString());
//	}
//	public File getExeLogFolder(Integer nodeId){
//		StringBuffer fileName = new StringBuffer(logPath);
//		fileName.append("nodes/").append(nodeId).append("/");
//		return new File(fileName.toString());
//	}
}
