package com.webrender.axis.beanxml;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;

public class CommmandUtilsTest {
	private CommandUtils utils = null;
	private Command command = null;
	private CommandDAO dao = null;
	
	@Before
	public void setUp() throws Exception {
		dao = new CommandDAO();
		utils = new CommandUtils();
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
		utils = null;
	}

	@Test
	public void testCommandToXMLForExe() {
		List<Command> list = dao.findAll();
		for(Command command : list){
			String result =  utils.commandToXMLForExe(command);
//			System.out.println(result);
			assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		}
		
	}

}
