package com.webrender.axis;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;

public class QuestsState extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(QuestsState.class);
	
	public String getQuestStatus(String questId)
	{
		if(questId==null || questId.equals("")){
			return BaseAxis.ACTIONFAILURE;
		}
		LOG.debug("getQuestStatus: id = "+questId);
//		try{
//			if (!this.canVisit(7) ){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
		
		try{
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			if(quest!=null){
				Element root = QuestUtils.bean2xmlWithState(questDAO.findById(Integer.parseInt(questId)));
				Document doc = new Document(root);
				LOG.debug("QuestID: "+questId + "  Progress: "+root.getAttributeValue("progress"));
				
				LOG.debug("getQuestStatus success: id = "+questId);
				return XMLOut.outputToString(doc);
			}else{
				return "QuestNotExistError";				
			}
		}
		catch(Exception e)
		{
			LOG.error("getQuestStatus failure: id = "+questId,e);
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
		
	}
	
	public String getQuestsStatus()
	{
		LOG.debug("getQuestsStatus");
		
//		try{
//			if (!this.canVisit(7) ){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
//		MessageContext mc = MessageContext.getCurrentContext();
//		String remoteAdd = ( (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
		try{
//			getSession().flush();
			
			Element root = new Element("Quests");
			Document doc = new Document(root);
			
			QuestDAO questDAO = new QuestDAO();
			int size = questDAO.findAll().size();
			
			Iterator ite_Quests = questDAO.findAll().iterator();
			while(ite_Quests.hasNext())
			{
				Quest quest = (Quest)ite_Quests.next();
				Element ele_Quest = QuestUtils.bean2xmlWithState(quest);
				root.addContent(ele_Quest);
			}
			String result = XMLOut.outputToString(doc);
//			LOG.info(result);
//			LOG.debug(remoteAdd+" ThreadID:"+Thread.currentThread().getId()+" QuestsNum: "+ size);
			LOG.debug("getQuestsStatus success");
			return result;			
		}catch(Exception e)
		{
			LOG.error("getQuestsStatus error",e);
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally
		{
//			LOG.info(" getQuestsStatus finally closeSession");
			this.closeSession();
		}
	}
	
	public String getUserQuests(){
		LOG.debug("getUserQuests");
		int regUserId = getLoginUserId();
		if(regUserId == 0 ){
			return BaseAxis.NOTLOGIN;
		}
		else{
			try{
				Element root = new Element("Quests");
				Document doc = new Document(root);
				
				ReguserDAO regUserDAO = new ReguserDAO();
				Reguser regUser = regUserDAO.findById(regUserId);
				Iterator ite_Quests = regUser.getQuests().iterator();
				while(ite_Quests.hasNext()){
					Quest quest = (Quest)ite_Quests.next();
					Element ele_Quest = QuestUtils.bean2xmlWithState(quest);
					root.addContent(ele_Quest);
				}
				String result = XMLOut.outputToString(doc);
				LOG.debug("getUserQuests success");
				return result;			
			}catch(Exception e){
				LOG.error("getUserQuests error",e);
				return BaseAxis.ACTIONFAILURE+e.getMessage();
			}finally{
//				LOG.info(" getQuestsStatus finally closeSession");
				this.closeSession();
			}
		}
	}
}
