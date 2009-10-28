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
import com.webrender.axis.operate.QuestsStateImpl;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;

public class QuestsState extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(QuestsState.class);
	
	public String getQuestStatus(String questId)
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new QuestsStateImpl()).getQuestStatus(questId);
		
	}
	
	public String getQuestsStatus()
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		return (new QuestsStateImpl()).getQuestsStatus();
	}
	
	public String getUserQuests(){
		int regUserId = this.getLoginUserId() ;
		if ( regUserId==0 )	return BaseAxis.NOTLOGIN;
		return (new QuestsStateImpl()).getUserQuests(regUserId);
	}
}
