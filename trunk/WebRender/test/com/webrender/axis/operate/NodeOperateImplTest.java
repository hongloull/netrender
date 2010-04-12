/**
 * 
 */
package com.webrender.axis.operate;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.remote.NodeMachineManager;

/**
 * @author WAEN
 *
 */
public class NodeOperateImplTest {
	private NodeOperateImpl impl = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new NodeOperateImpl();
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#pauseNode(java.lang.String, int)}.
	 */
	@Test
	public final void testPauseNode() {
		
		assertTrue(impl.pauseNode(null, 0).equals("Failure null") );
		assertTrue(impl.pauseNode("0", 0).equals("Failure null") );
		assertTrue(impl.pauseNode("a", 0).startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#resumeNode(java.lang.String, int)}.
	 */
	@Test
	public final void testResumeNode() {
//		System.out.println(impl.resumeNode("aa", 0));
		assertTrue(impl.resumeNode(null, 0).equals("Failure null") );
		assertTrue(impl.resumeNode("0", 0).equals("Failure null") );
		assertTrue(impl.resumeNode("a", 0).startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#setRealLog(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testSetRealLog() {
		assertTrue(impl.setRealLog(null,"1", 0).equals("Failure null") );
		assertTrue(impl.setRealLog("0","1", 0).equals("Failure null") );
		assertTrue(impl.setRealLog("a","1", 0).startsWith("Failure For input string:") );
		assertTrue(impl.setRealLog("1","aa", 0).startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#getPriority(java.lang.String)}.
	 */
	@Test
	public final void testGetPriority() {
		assertTrue(impl.getPriority(null).equals("Failure null") );
		assertTrue(impl.getPriority("0").equals("Failure null") );
		assertTrue(impl.getPriority("a").startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#setPriority(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testSetPriority() {
		assertTrue(impl.setPriority(null,"10", 0).equals("Failure null") );
		assertTrue(impl.setPriority("0","10", 0).equals("Failure null") );
		assertTrue(impl.setPriority("a","10", 0).startsWith("Failure For input string:") );
		assertTrue(impl.setPriority("1","aa", 0).startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#killCommand(java.lang.String, int)}.
	 */
	@Test
	public final void testKillCommand() {
		assertTrue(impl.killCommand(null, 0).equals("Failure null") );
		assertTrue(impl.killCommand("0", 0).equals("Failure null") );
		assertTrue(impl.killCommand("a", 0).startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#shutdownNode(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testShutdownNode() {
		assertTrue(impl.shutdownNode(null,"1",0).equals("Failure null") );
		assertTrue(impl.shutdownNode("0","1",0).equals("Failure null") );
		assertTrue(impl.shutdownNode("a","1",0).startsWith("Failure For input string:") );
		assertTrue(impl.shutdownNode("1","A",0).startsWith("Failure For input string:") );
		assertTrue(impl.shutdownNode("1",null,0).startsWith("Failure null") );
		assertTrue(impl.shutdownNode("1","2",0).startsWith("Failure nodeId:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#softRestart(java.lang.String, int)}.
	 */
	@Test
	public final void testSoftRestart() {
		assertTrue(impl.softRestart(null, 0).equals("Failure null") );
		assertTrue(impl.softRestart("0", 0).equals("Failure null") );
		assertTrue(impl.softRestart("a", 0).startsWith("Failure For input string:") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#exeSystemCommand(java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testExeSystemCommand() {
		assertTrue(impl.exeSystemCommand(null,"S_SOFTRESTART","0",0).equals("Failure null") );
		assertTrue(impl.exeSystemCommand("0","S_SOFTRESTART","0",0).equals("Failure null") );
		assertTrue(impl.exeSystemCommand("a","S_SOFTRESTART","0",0).startsWith("Failure For input string:") );
		assertTrue( (impl.exeSystemCommand("1","F","0",0).startsWith("Failure :") ) );
		assertTrue( (impl.exeSystemCommand("1","S_SOFTRESTART",null,0).startsWith("Failure exe systemcommand error:") ) );
		assertTrue( (impl.exeSystemCommand("1","S_SOFTRESTART","ff",0).startsWith("Failure exe systemcommand error:") ) );
		assertTrue( (impl.exeSystemCommand("1","S_SOFTRESTART","1",0).startsWith("Failure exe systemcommand error:") ) );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.NodeOperateImpl#delNode(java.lang.String, int)}.
	 */
	@Test
	public final void testDelNode() {

		assertTrue(impl.delNode(null, 0).equals("Failure null") );
		assertTrue(impl.delNode("0", 0).equals("Failure NodeId:0 not exist") );
		assertTrue(impl.delNode("a", 0).startsWith("Failure For input string:") );
	}
	@Test
	public final void testgetExeRecords(){
		System.out.println( impl.getExeRecords("5", 1,3) );
	}
}
