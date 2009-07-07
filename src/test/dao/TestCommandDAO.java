package test.dao;

import java.util.Iterator;

import org.junit.Test;

import com.webrender.dao.CommandDAO;
import com.webrender.dao.Command;

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
}
