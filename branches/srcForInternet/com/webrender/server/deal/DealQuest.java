package com.webrender.server.deal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.logic.CalcFrames;
import com.webrender.server.ControlThreadServer;

public class DealQuest {
	private static final Log LOG = LogFactory.getLog(DealQuest.class);
	/**
	 * 
	 */
	public void makeQuestFrames(Quest quest,String framesValue,String byFrame){
		Transaction tx = null;
		int totalSize = 0;
		QuestDAO questDAO = new QuestDAO();
		try{
			questDAO.attachClean(quest);
			
			quest = questDAO.getQuestWithFrameInfo(quest, framesValue, byFrame);
			CalcFrames calcFrames = new CalcFrames();
			tx = HibernateSessionFactory.getSession().beginTransaction();
			
			int result = calcFrames.calc(quest);
			totalSize = calcFrames.getTotalSize();
			if(result == CalcFrames.SUCCESS){
				tx.commit();
			}
			else{
				LOG.error("quest:"+quest.getQuestName()+" deal frames fail.");
				if(tx!=null) tx.rollback();
			}
		}catch(Exception e){
			if(tx !=null){
				tx.rollback();
			}
			LOG.error("makeQuestFrames fail",e);
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
	
	public void setPreLight(Quest quest , String preLight){
		Transaction tx = null;
		try{
//			LOG.info("setPreLight before save questId:"+quest.getQuestId());
			tx = HibernateSessionFactory.getSession().beginTransaction();
			QuestDAO questDAO = new QuestDAO();
			quest.setPreLight(preLight);
			questDAO.attachDirty(quest);
			tx.commit();
//			LOG.info("setPreLight after save questId:"+quest.getQuestId());
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