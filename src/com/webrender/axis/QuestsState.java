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

public class QuestsState extends BaseAxis {
	private static final Log log = LogFactory.getLog(QuestsState.class);
	
	public String getQuestStatus(String questId)
	{
		log.debug("getQuestStatus: id = "+questId);
		try{
			if (!this.canVisit(7) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		try{
			QuestDAO questDAO = new QuestDAO();
			Element root = QuestUtils.bean2xml_State(questDAO.findById(Integer.parseInt(questId)));
			Document doc = new Document(root);
			log.info("QuestID: "+questId + "  Progress: "+root.getAttributeValue("progress"));
		
			log.debug("getQuestStatus success: id = "+questId);
			return XMLOut.outputToString(doc);
		}
		catch(NullPointerException e){
			log.error("getQuestStatus failure: id = "+questId,e);
			return "QuestNotExistError";
		}
		catch(Exception e)
		{
			log.error("getQuestStatus failure: id = "+questId,e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
		
	}
	
	public String getQuestsStatus()
	{
		log.debug("getQuestsStatus");
		
		try{
			if (!this.canVisit(7) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		MessageContext mc = MessageContext.getCurrentContext();
		String remoteAdd = ( (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST)).getRemoteAddr();
		try{
			getSession().flush();
			
			Element root = new Element("Quests");
			Document doc = new Document(root);
			
			QuestDAO questDAO = new QuestDAO();
			int size = questDAO.findAll().size();
			
			Iterator ite_Quests = questDAO.findAll().iterator();
			while(ite_Quests.hasNext())
			{
				Quest quest = (Quest)ite_Quests.next();
				Element ele_Quest = QuestUtils.bean2xml_State(quest);
				root.addContent(ele_Quest);
			}
			String result = XMLOut.outputToString(doc);
//			log.info(result);
			log.debug(remoteAdd+" ThreadID:"+Thread.currentThread().getId()+" QuestsNum: "+ size);
			log.debug("getQuestsStatus success");
			return result;			
		}catch(Exception e)
		{
			log.error("getQuestsStatus error",e);
			return BaseAxis.ActionFailure;
		}finally
		{
//			log.info(" getQuestsStatus finally closeSession");
			this.closeSession();
		}
	}
}
