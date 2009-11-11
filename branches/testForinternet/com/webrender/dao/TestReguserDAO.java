package com.webrender.dao;

import java.util.Iterator;
import org.junit.Test;
import com.webrender.dao.ReguserDAO;


public class TestReguserDAO{
	@Test
	public void testGetRightValue()
	{
		ReguserDAO regUserDAO = new ReguserDAO();
		Iterator rights = regUserDAO.getRightValue( regUserDAO.findById(1) ).iterator();
		while(rights.hasNext()){
			System.out.println( rights.next() );
		}
	}
}
