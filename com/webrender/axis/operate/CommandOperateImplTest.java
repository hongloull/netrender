/**
 * 
 */
package com.webrender.axis.operate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;

/**
 * @author asp
 *
 */
public class CommandOperateImplTest {
	private CommandOperateImpl impl = null;
	private CommandDAO dao = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new CommandOperateImpl();
		dao = new CommandDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.CommandOperateImpl#getRealLogs(java.lang.String)}.
	 */
	@Test
	public final void testGetRealLogs() {
		List<Command> list = dao.findAll();
		for(Command command : list){
			String commandId = command.getCommandId().toString();
//			System.out.println( impl.getRealLogs(commandId));
			assertTrue( impl.getRealLogs(commandId).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		}
		
		assertTrue( impl.getRealLogs("").equals("Failure For input string: \"\"") );
		assertTrue( impl.getRealLogs("0").equals("Failure null"));
		assertTrue( impl.getRealLogs("a").startsWith("Failure For input string:"));
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.CommandOperateImpl#getRealLogFile(java.lang.String)}.
	 */
	@Test
	public final void testGetRealLogFile() {
		List<Command> list = dao.findAll();
		for(Command command : list){
			String commandId = command.getCommandId().toString();
//			System.out.println( impl.getRealLogFile(commandId));
			assertTrue( impl.getRealLogFile(commandId).equalsIgnoreCase( BaseOperate.ACTIONFAILURE));
		}
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.CommandOperateImpl#reinitCommand(java.lang.String, int)}.
	 */
	@Test
	public final void testReinitCommand() {
//		System.out.println(impl.reinitCommand("a", 1));
		assertTrue(impl.reinitCommand("0", 0).equals("Failure null"));
		assertTrue(impl.reinitCommand("f", 0).startsWith("Failure For input string:"));
		List<Command> list = dao.findAll();
		for(Command command : list){
			String commandId = command.getCommandId().toString();
			assertTrue( BaseOperate.ACTIONSUCCESS.equals( impl.reinitCommand(commandId, 0) ) );
		}
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.CommandOperateImpl#setFinish(java.lang.String, int)}.
	 */
	@Test
	public final void testSetFinish() {
		assertTrue(impl.setFinish("0", 0).equals("Failure null"));
		assertTrue(impl.setFinish("", 0).startsWith("Failure For input string:"));
		List<Command> list = dao.findAll();
		for(Command command : list){
			String commandId = command.getCommandId().toString();
			assertTrue( BaseOperate.ACTIONSUCCESS.equals( impl.setFinish(commandId, 0) ) );
//			assertTrue( impl.getRealLogFile(commandId).equalsIgnoreCase( BaseOperate.ACTIONFAILURE));
		}
	}

}
