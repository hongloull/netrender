package com.webrender.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Questarg;

public class TestQuestDAO {
	@Test
	public void testQuestargOrder()
	{
		QuestDAO dao = new QuestDAO();
		List list = dao.findAll();
		if( list.isEmpty()!=true ){
			Quest quest = (Quest) list.get(list.size()-1);
			Set<Questarg> set = quest.getQuestargs();
			for(Questarg arg : set){
				System.out.println("QuestArgId: "+arg.getQuestArgId()+" CommandModelArgId: "+arg.getCommandmodelarg().getCommandModelArgId()+" Value: "+arg.getValue() );
			}
		}
	}
}
