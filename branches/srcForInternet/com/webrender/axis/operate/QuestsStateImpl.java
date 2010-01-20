package com.webrender.axis.operate;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.StatusDAO;

public class QuestsStateImpl extends BaseOperate {
	private static final Log LOG = LogFactory.getLog(QuestsStateImpl.class);
	private QuestUtils questUtils = new QuestUtils();
	private XMLOut xmlOut = new XMLOut();
	public String getQuestStatus(String questId)
	{
		
		LOG.debug("getQuestStatus: id = "+questId);
		if(questId==null || questId.equals("")){
			return  ACTIONFAILURE+"questid = null";
		}
//		try{
//			if (!this.canVisit(7) ){
//				return  RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return  RIGHTERROR;
//		}
		
		try{
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			if(quest!=null){
				Element root = questUtils.bean2xmlWithState(questDAO.findById(Integer.parseInt(questId)));
				Document doc = new Document(root);
				LOG.debug("QuestID: "+questId + "  Progress: "+root.getAttributeValue("progress"));
				
				LOG.debug("getQuestStatus success: id = "+questId);
				return xmlOut.outputToString(doc);
			}else{
				return ACTIONFAILURE+"QuestNotExistError";				
			}
		}
		catch(Exception e)
		{
			LOG.error("getQuestStatus failure: id = "+questId,e);
			return  ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
		
	}
	
	public String getQuestsStatus()
	{
		LOG.debug("getQuestsStatus");
		
// 很郁闷，为了解决删除或添加后，数据显示不更新的问题，修改下数据库刷新。
//		StatusDAO statusDAO = new StatusDAO();
//		Transaction tx = null;
//		try {
//			tx = getSession().beginTransaction();
//			statusDAO.updateSystemVersion();
//			tx.commit();					
//		}catch (Exception e){					
//			LOG.error("UpdateSystemVersion Error", e);
//			if(tx!=null){
//				tx.rollback();
//			}
//		}finally{		
//		}
		
		try{
			
			Element root = new Element("Quests");
			Document doc = new Document(root);
			
			QuestDAO questDAO = new QuestDAO();
			int size = questDAO.findAll().size();
			
			Iterator ite_Quests = questDAO.findAll().iterator();
			while(ite_Quests.hasNext())
			{
				Quest quest = (Quest)ite_Quests.next();
				Element ele_Quest = questUtils.bean2xmlWithState(quest);
				root.addContent(ele_Quest);
			}
			String result = xmlOut.outputToString(doc);
//			LOG.info(result);
//			LOG.debug(remoteAdd+" ThreadID:"+Thread.currentThread().getId()+" QuestsNum: "+ size);
			LOG.debug("getQuestsStatus success");
			return result;			
		}catch(Exception e)
		{
			LOG.error("getQuestsStatus error",e);
			return  ACTIONFAILURE+e.getMessage();
		}finally
		{
//			LOG.info(" getQuestsStatus finally closeSession");
			this.closeSession();
		}
	}
	
	public String getUserQuests(int regUserId){		
		LOG.debug("getUserQuests");
		try{
			Element root = new Element("Quests");
			Document doc = new Document(root);
			
			ReguserDAO regUserDAO = new ReguserDAO();
			Reguser regUser = regUserDAO.findById(regUserId);
			Iterator ite_Quests = regUser.getQuests().iterator();
			while(ite_Quests.hasNext()){
				Quest quest = (Quest)ite_Quests.next();
				Element ele_Quest = questUtils.bean2xmlWithState(quest);
				root.addContent(ele_Quest);
			}
			String result = xmlOut.outputToString(doc);
			LOG.debug("getUserQuests success");
			return result;			
		}catch(Exception e){
			LOG.error("getUserQuests error",e);
			return  ACTIONFAILURE+e.getMessage();
		}finally{
//				LOG.info(" getQuestsStatus finally closeSession");
			this.closeSession();
		}
		
		

	}
}
