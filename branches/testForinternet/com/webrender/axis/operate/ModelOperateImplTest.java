/**
 * 
 */
package com.webrender.axis.operate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;

/**
 * @author WAEN
 *
 */
public class ModelOperateImplTest {
	private ModelOperateImpl impl = null;
	private CommandmodelDAO dao = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new ModelOperateImpl();
		dao = new CommandmodelDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.ModelOperateImpl#getModel(java.lang.String)}.
	 */
	@Test
	public final void testGetModel() {
		String commandModelId = null;
		assertTrue(impl.getModel(commandModelId).equals("Failure null"));
		
		commandModelId = "adf";
		assertTrue(impl.getModel(commandModelId).startsWith("Failure For input string: "));
		
		List<Commandmodel> list = dao.findAll();
		for(Commandmodel model : list){
			System.out.println( impl.getModel(model.getCommandModelId().toString()) );
			assertTrue(impl.getModel(model.getCommandModelId().toString()).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		}

	}

	/**
	 * Test method for {@link com.webrender.axis.operate.ModelOperateImpl#getModels(int)}.
	 */
	@Test
	public final void testGetModels() {
		int regUserId = 0;
		assertTrue(impl.getModels(regUserId).startsWith("Failure : Reguser = null"));
		regUserId = -1;
		assertTrue(impl.getModels(regUserId).startsWith("Failure : Reguser = null"));
		ReguserDAO regUserDAO = new ReguserDAO();
		List<Reguser> list = regUserDAO.findAll();
		for(Reguser user : list){
//			System.out.println(user.getRegName());
//			System.out.println( impl.getModels(user.getRegUserId()) );
			assertTrue(impl.getModels(user.getRegUserId()).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		}
	}

}
