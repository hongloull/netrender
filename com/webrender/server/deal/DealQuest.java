package com.webrender.server.deal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.logic.CalcFrames;

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
				questDAO.save(quest);
				tx.commit();
			}catch(Exception e){
				LOG.error("makeQuestFrames save quest's totalFrames fail",e);
				if(tx!=null){
					tx.rollback();
				}
			}
		}
		
	}
	
	public void setPreLight(Quest quest , String preLight){
		Transaction tx = null;
		try{
			tx = HibernateSessionFactory.getSession().beginTransaction();
			QuestDAO questDAO = new QuestDAO();
			quest.setPreLight(preLight);
			questDAO.save(quest);
			tx.commit();
			return;
		}catch(Exception e){
			if(tx !=null){
				tx.rollback();
			}
			LOG.error("setPreLight fail",e);
		}
	}
}
