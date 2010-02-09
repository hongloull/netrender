/**
 * 
 */
package com.webrender.axis.operate;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author WAEN
 *
 */
public class PoolOperateImplTest {
	private PoolOperateImpl impl = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new PoolOperateImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.PoolOperateImpl#addPool(java.lang.String, int)}.
	 */
	@Test
	public final void testAddPool() {
		System.out.println( impl.addPool(null,0).equals("Failure name can not be null") );
		System.out.println( impl.addPool("All",0).equals("Failure PoolExistError"));
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.PoolOperateImpl#modPool(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testModPool() {
		assertTrue( impl.modPool("All", null, 0).equals("Failure PoolFileNotExistError"));
		assertTrue( impl.modPool(null, null, 0).equals("Failure name can not be null"));
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.PoolOperateImpl#getPoolConfig(java.lang.String)}.
	 */
	@Test
	public final void testGetPoolConfig() {
		assertTrue( impl.getPoolConfig("All").equals("Failure PoolNotExistError") );
		assertTrue( impl.getPoolConfig(null).equals("Failure name can not be null") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.PoolOperateImpl#delPool(java.lang.String, int)}.
	 */
	@Test
	public final void testDelPool() {
		assertTrue( impl.delPool(null, 0).equals("Failure PoolNotExistError") );
		assertTrue( impl.delPool("All", 0).equals("Failure Del All file error") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.PoolOperateImpl#getPools(int, boolean)}.
	 */
	@Test
	public final void testGetPools() {
		assertTrue(impl.getPools(0, true).startsWith("<?xml version"));
		assertTrue(impl.getPools(0, false).equals("Failure null"));
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.PoolOperateImpl#getNodes()}.
	 */
	@Test
	public final void testGetNodes() {
		assertTrue( impl.getNodes().equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Nodes />") );
	}

}
