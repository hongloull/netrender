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
		try{
			QuestDAO questDAO = new QuestDAO();
			quest = questDAO.getQuestWithFrameInfo(quest, framesValue, byFrame);
			CalcFrames calcFrames = new CalcFrames();
			tx = HibernateSessionFactory.getSession().beginTransaction();
			
			int result = calcFrames.calc(quest);
			if(result == CalcFrames.SUCCESS){
				tx.commit();
				return;
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