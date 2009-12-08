/**
 * 
 */
package com.webrender.axis.beanxml;


import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Status;

/**
 * @author WAEN
 *
 */
public class ChunkDetailUtilsTest {

	
	private Command command = null;
	private ChunkDetailUtils  chunkDetailUtils = null;
	private XMLOut xmlOut = null;
	private CommandDAO commandDAO = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
//		command = new Command();
		chunkDetailUtils = new ChunkDetailUtils();
		xmlOut = new XMLOut();
		commandDAO = new CommandDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		command = null;
		chunkDetailUtils = null;
		xmlOut = null;
	}
	
	@Test
	public void testAllCommandsGetElement(){
		Iterator ite = commandDAO.findAll().iterator();
		while(ite.hasNext()){
			command = (Command) ite.next();
			Element root = new Element("Test");
			Document doc = new Document(root);
			
			root.addContent(chunkDetailUtils.getElement(command));
			assertTrue( xmlOut.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}		
	}
	
}
