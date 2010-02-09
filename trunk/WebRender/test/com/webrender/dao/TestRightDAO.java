package com.webrender.dao;
import java.util.Iterator;

import org.junit.Test;


import com.webrender.dao.Right;
import com.webrender.dao.RightDAO;
public class TestRightDAO {
	@Test
	public void testFindAll(){
		RightDAO rightDAO = new RightDAO();
		Iterator ite =  rightDAO.findAll().iterator();
		while(ite.hasNext()){
			System.out.println( ((Right)ite.next()).getRightId());
		}
	}
}
