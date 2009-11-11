package com.webrender.axis;

import org.junit.Test;

import com.webrender.axis.QuestOperate;
import com.webrender.axis.QuestsState;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;

public class TestQuestsState {
	@Test
	public void testGetQuestStatus()
	{
		QuestsState qs = new QuestsState();
		System.out.println( qs.getQuestStatus("143") );
	}
	
	@Test
	public void testGetQuestsStatus()
	{
		QuestsState qs = new QuestsState();
	//	QuestDAO questDAO = new QuestDAO();
		for(int i =0 ; i<10;i++){
			System.out.println( qs.getQuestsStatus() ); 
			
		}
	}
}
