/**
 * 
 */
package com.webrender.axis.beanxml;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.webrender.axis.beanxml.ExecutelogUtils;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
/**
 * @author asp
 *
 */
public class ExecutelogUtilsTest {
	private ExecutelogUtils utils = null;
	private Executelog executelog=null;
	private ExecutelogDAO dao = null;
	private XMLOut xmlOut = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		utils = new ExecutelogUtils();
		dao = new ExecutelogDAO();
		xmlOut = new XMLOut();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.ExecutelogUtils#bean2xml(com.webrender.dao.Executelog)}.
	 */
	@Test
	public void testBean2xml() {
		Iterator ite = dao.findAll().iterator();
		while(ite.hasNext()){
			executelog = (Executelog)ite.next();
			Element root = new Element("Test");
			Document doc = new Document(root);
			root.addContent(utils.bean2xml(executelog));
//			System.out.println( xmlOut.outputToString(doc) );
			assertTrue(xmlOut.outputToString(doc).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.ExecutelogUtils#bean2XMLString(com.webrender.dao.Executelog)}.
	 */
	@Test
	public void testBean2XMLString() {
		Iterator ite=dao.findAll().iterator();
		while(ite.hasNext()){
			executelog = (Executelog)ite.next();
			utils.bean2XMLString(executelog);
//			System.out.println(utils.bean2XMLString(executelog));
			assertTrue( utils.bean2XMLString(executelog).startsWith( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ) );
		}
	}

}
