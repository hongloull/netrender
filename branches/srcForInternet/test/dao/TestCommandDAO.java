package test.dao;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.Transaction;
import org.junit.Test;

import com.webrender.dao.CommandDAO;
import com.webrender.dao.Command;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;

public class TestCommandDAO {
	@Test
	public void testGetWaitingCommands(){
		CommandDAO commandDAO = new CommandDAO();
		Iterator ite = commandDAO.getWaitingCommands().iterator();
		System.out.println("WaitingCommanding :");
		while(ite.hasNext()){
			System.out.println( ( (Command)ite.next() ).getCommandId() );
		}		
	}
	@Test
	public void testAddDate(){
		CommandDAO commandDAO = new CommandDAO();
		Iterator<Command> ite = commandDAO.findAll().iterator();
		Transaction tx  = HibernateSessionFactory.getSession().beginTransaction();
		while(ite.hasNext()){
			Command c = ite.next();
			c.setSendTime(new Date());
			commandDAO.save(c);			
		}
		tx.commit();
		HibernateSessionFactory.closeSession();
	}
	@Test
	public void testInprogress()
	{
		CommandDAO commandDAO = new CommandDAO();
		QuestDAO questDAO = new QuestDAO();
		Iterator<Quest> ite = questDAO.findAll().iterator();
		while(ite.hasNext())
		{
			Quest quest = ite.next();
			try {
				System.out.println(commandDAO.isInProgress(quest)+quest.getQuestId().toString());
			} catch (Exception e) {
				
			}
		}
	}
	@Test 
	public void testgetNote(){
		CommandDAO commandDAO = new CommandDAO();
		Command command = (Command) commandDAO.findAll().get(0);
		System.out.println(commandDAO.getNoteWithID(command));
	}
	@Test
	public void testUpdateExeLog(){
		ExecutelogDAO exeLogDAO = new ExecutelogDAO();
		CommandDAO commandDAO = new CommandDAO();
		Iterator<Executelog> ite = exeLogDAO.findAll().iterator();
		Transaction tx = null;
		tx = HibernateSessionFactory.getSession().beginTransaction();
		while(ite.hasNext()){
			Executelog exeLog = ite.next();
			if ( exeLog.getStatus().getStatusId()==99){
				
				exeLog.setNote("SendError: "+commandDAO.getNoteWithID(exeLog.getCommand()));
			}
		}
		tx.commit();
		HibernateSessionFactory.closeSession();
	}
}
