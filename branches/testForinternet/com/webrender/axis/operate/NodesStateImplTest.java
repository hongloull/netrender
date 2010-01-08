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
public class NodesStateImplTest {
	private NodesStateImpl impl = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new NodesStateImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodesStateImpl#getNodeStatus(java.lang.String)}.
	 */
	@Test
	public final void testGetNodeStatus() {
		assertTrue( impl.getNodeStatus("0").endsWith("Failure null") );
		assertTrue( impl.getNodeStatus(null).endsWith("Failure null") );
		assertTrue( impl.getNodeStatus("F").endsWith("Failure For input string: \"F\"") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodesStateImpl#getNodesStatus(java.lang.String)}.
	 */
	@Test
	public final void testGetNodesStatus() {
		assertTrue(impl.getNodesStatus("All").startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodesStateImpl#getAllNodes()}.
	 */
	@Test
	public final void testGetAllNodes() {
		assertTrue(impl.getAllNodes().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
	}

}
