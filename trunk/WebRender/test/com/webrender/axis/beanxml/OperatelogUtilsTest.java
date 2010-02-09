/**
 * 
 */
package com.webrender.axis.beanxml;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Operatelog;
import com.webrender.dao.OperatelogDAO;

/**
 * @author asp
 *
 */
public class OperatelogUtilsTest {
	private OperatelogUtils utils = null;
	private OperatelogDAO dao = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		utils = new OperatelogUtils();
		dao = new OperatelogDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.beanxml.OperatelogUtils#bean2XMLString(com.webrender.dao.Operatelog)}.
	 */
	@Test
	public void testBean2XMLString() {
		List<Operatelog> list = dao.findAll();
		for(Operatelog log : list){
//			System.out.println( utils.bean2XMLString(log) );
			assertTrue( utils.bean2XMLString(log).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
		}
	}

}
