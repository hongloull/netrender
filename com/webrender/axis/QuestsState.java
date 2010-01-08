package com.webrender.axis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.axis.operate.QuestsStateImpl;

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
