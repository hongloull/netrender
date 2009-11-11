package com.webrender.test;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Transaction;

import com.webrender.axis.BaseAxis;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.StatusDAO;

public class RefreshQuestion {

	
	public static void main(String[] args) {
		CommandDAO commandDAO = new CommandDAO();
		List lis_Commands = commandDAO.getWaitingCommands();
		int size = lis_Commands.size();
		System.out.println(size);
		if (size>0) {
			Transaction tx = null;
			try{
				tx = getTransaction();
				Command command = (Command)lis_Commands.get(0);
				StatusDAO statusDAO = new StatusDAO(); 
				command.setStatus(statusDAO.findById(71));
				commandDAO.attachDirty(command);
				tx.commit();
				
			}
			catch(Exception e)
			{
				if (tx != null) 
				{
					tx.rollback();
				}
				
			}
		}

		List lis_Commands2 = commandDAO.getWaitingCommands();
		int size2 = lis_Commands2.size();
		System.out.println(size2);
	}

	private static Transaction getTransaction() {
		return HibernateSessionFactory.getSession().beginTransaction();
	}
	
}

