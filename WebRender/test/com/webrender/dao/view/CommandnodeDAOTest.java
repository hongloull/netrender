package com.webrender.dao.view;


import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandnodeDAOTest {
	private CommandnodeDAO dao = null;
	@Before
	public void setUp() throws Exception {
		dao = new CommandnodeDAO(); 
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFindByNode(){
		Iterator ite = dao.findByNodeId(72).iterator();
		Commandnode commandNode = null;
		while(ite.hasNext()){
			commandNode = (Commandnode) ite.next();
			System.out.println( commandNode.getId() );
		}
	}
}
