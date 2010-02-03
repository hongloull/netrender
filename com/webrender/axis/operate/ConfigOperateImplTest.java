/**
 * 
 */
package com.webrender.axis.operate;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.remote.NodeMachineManager;

/**
 * @author WAEN
 *
 */
public class ConfigOperateImplTest {
	private ConfigOperateImpl impl = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new ConfigOperateImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.ConfigOperateImpl#getPathConfig()}.
	 */
	@Test
	public final void testGetPathConfig() {
		String result = impl.getPathConfig();
//		System.out.println( impl.getPathConfig() );
		assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.ConfigOperateImpl#setPathConfig(java.lang.String, int)}.
	 */
	@Test
	public final void testSetPathConfig() {
//		System.out.println(impl.setPathConfig("", 0));
		assertTrue(impl.setPathConfig(null, 0).equals("Failure null"));
		assertTrue(impl.setPathConfig("", 0).startsWith("Failure "));
		String questXML = impl.getPathConfig();
		assertTrue( BaseOperate.ACTIONSUCCESS.equals( impl.setPathConfig(questXML, 0) ) );
		
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.ConfigOperateImpl#getNodeConfig(java.lang.String)}.
	 */
	@Test
	public final void testGetNodeConfig() {

//		Set<Integer> set = NodeMachineManager.getInstance().getNodeMachines();
//		for(Integer nodeId : set){
//			String result = impl.getNodeConfig(nodeId.toString());
//			assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") );
//		}
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.ConfigOperateImpl#setNodeConfig(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testSetNodeConfig() {
		String nodeId = null;
		String config = null;
		assertTrue( impl.setNodeConfig(nodeId, config, 0).equals("Failure null") );
		
		nodeId = "12";
		config = "";
		assertTrue( impl.setNodeConfig(nodeId, config, 0).startsWith("Failure ") );
		
	}

}
