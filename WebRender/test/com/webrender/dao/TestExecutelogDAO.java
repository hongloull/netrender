package com.webrender.dao;


import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestExecutelogDAO {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testfindByNodeIdWithPages(){
		ExecutelogDAO dao = new ExecutelogDAO();
		Iterator ite = dao.findByNodeIdWithPages(5,1,20).iterator();
		Executelog log = null;
		int i = 1;
		while(ite.hasNext()){
			log = (Executelog) ite.next();
			System.out.println(i+":  "+log.getLogTime()+" --- "+log.getNote());
			i++;
		}
	}
}
