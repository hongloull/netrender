package com.webrender.server.deal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandarg;
import com.webrender.dao.CommandargDAO;
import com.webrender.dao.CommandmodelargDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.logic.CalcFrames;
import com.webrender.server.ControlThreadServer;
import com.webrender.tool.NameMap;

public class DealQuest {
	private static final Log LOG = LogFactory.getLog(DealQuest.class);
	/**
	 * @throws Exception 
	 * 
	 */
	private void makeQuestFrames(Quest quest,String framesValue,String byFrame) throws Exception{
		Transaction tx = null;
		int totalSize = 0;
		QuestDAO questDAO = new QuestDAO();
		
		questDAO.attachClean(quest);		
		quest = questDAO.getQuestWithFrameInfo(quest, framesValue, byFrame,false);
		CalcFrames calcFrames = new CalcFrames();
		tx = HibernateSessionFactory.getSession().beginTransaction();
		
		int result = calcFrames.calc(quest);
		totalSize = calcFrames.getTotalSize();
		if(result == CalcFrames.SUCCESS){
			tx.commit();
			tx = null;
		}
		else{
			LOG.error("quest:"+quest.getQuestName()+" deal frames fail."+" framesValue:"+framesValue+" byFrame:"+byFrame);
			if(tx!=null) tx.rollback();
			throw (new Exception("quest:"+quest.getQuestName()+" deal frames fail."+" framesValue:"+framesValue+" byFrame:"+byFrame) );
		}
		
		if(totalSize != 0){
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				quest.setTotalFrames(totalSize);
				questDAO.attachDirty(quest);
				tx.commit();
			}catch(Exception e){
				LOG.error("makeQuestFrames save quest's totalFrames fail",e);
				if(tx!=null){
					tx.rollback();
				}
			}
		}
		ControlThreadServer.getInstance().notifyResume();
		
	}
	public void patchFrames(Quest quest, String frames, String byFrame) throws Exception{
		if(NameMap.RENDER.equals(quest.getCommandmodel().getType())){
			Transaction tx = null;
			
			QuestDAO questDAO = new QuestDAO();
			quest = questDAO.getQuestWithFrameInfo(quest, frames, byFrame,true);			
			CalcFrames calc = new CalcFrames();
			tx = HibernateSessionFactory.getSession().beginTransaction();
			if( CalcFrames.SUCCESS == calc.patch(quest) ){
				tx.commit();
			}else{
				LOG.error("quest:"+quest.getQuestName()+" patch frames fail."+" framesValue:"+frames+" byFrame:"+byFrame);
				if(tx!=null) tx.rollback();
				throw (new Exception("quest:"+quest.getQuestName()+" patch frames fail."+" framesValue:"+frames+" byFrame:"+byFrame) );
			}
		}else{
			throw new Exception("Template type:"+ quest.getCommandmodel().getType()+" , cannot patchFrames!");
		}
		ControlThreadServer.getInstance().notifyResume();
	}
	
	public void makeQuestFrames(int commandId,String framesValue,String byFrame) throws Exception{
		CommandDAO commandDAO = new CommandDAO();
		Command command = commandDAO.findById(commandId);
		if( command.getType().endsWith(NameMap.GETFRAME)){
			Transaction tx = null;
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				CommandargDAO commandArgDAO = new CommandargDAO();
//				CommandDAO commandDAO = new CommandDAO();
				commandDAO.deleteCommandRel(command);
				CommandmodelargDAO commandmodelargDAO = new CommandmodelargDAO();
				Commandarg commandFrameArg = new Commandarg(command,commandmodelargDAO.findFrameArg(command.getQuest().getCommandmodel()),framesValue);
				Commandarg commandByArg = new Commandarg(command,commandmodelargDAO.findByArg(command.getQuest().getCommandmodel()),byFrame);
				commandArgDAO.save(commandFrameArg);
				commandArgDAO.save(commandByArg);
				tx.commit();
			}catch(Exception e){
				if(tx !=null){
					tx.rollback();
				}
				LOG.error("makeQuestFrames save getFrame args fail",e );
			}
			this.makeQuestFrames(command.getQuest(), framesValue, byFrame);
		}
		else{
			LOG.error("MakeQuestFrames command not GETFRAME commandId:"+commandId + " type:"+command.getType());
			throw( new Exception("Command not getframe error"));
		}
	}
	public void setPreLight(Quest quest , String preLight){
		Transaction tx = null;
		try{
			tx = HibernateSessionFactory.getSession().beginTransaction();
			QuestDAO questDAO = new QuestDAO();
			quest.setPreLight(preLight);
			questDAO.attachDirty(quest);
			tx.commit();
			return;
		}catch(Exception e){
			if(tx !=null){
				tx.rollback();
			}
			LOG.error("setPreLight fail",e);
		}
	}
	public void setPreLight(int commandId , String preLight){
		CommandDAO commandDAO = new CommandDAO();
		Command command = commandDAO.findById(commandId);
		this.setPreLight(command.getQuest(), preLight);
	}
	
}
