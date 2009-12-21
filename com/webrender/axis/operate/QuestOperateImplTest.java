/**
 * 
 */
package com.webrender.axis.operate;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;

/**
 * @author WAEN
 *
 */
public class QuestOperateImplTest {
	private QuestOperateImpl impl = null;
	private QuestDAO dao = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		impl = new QuestOperateImpl();
		dao = new QuestDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#CommitQuest(java.lang.String, int, boolean)}.
	 */
	@Test
	public final void testCommitQuest() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#deleteQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testDeleteQuest() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#pauseQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testPauseQuest() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#resumeQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testResumeQuest() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#reinitQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testReinitQuest() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#setFinish(java.lang.String, int)}.
	 */
	@Test
	public final void testSetFinish() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#changeName(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testChangeName() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#changePriority(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testChangePriority() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#changeMaxNodes(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testChangeMaxNodes() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#changePool(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testChangePool() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#getDetail(java.lang.String)}.
	 */
	@Test
	public final void testGetDetail() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#getChunkDetail(java.lang.String)}.
	 */
	@Test
	public final void testGetChunkDetail() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#getPreLight(java.lang.String)}.
	 */
	@Test
	public final void testGetPreLight() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetFrames(){
		List<Quest> quests = dao.findAll();
		for(Quest quest : quests ){
			System.out.println( impl.getFrame(quest.getQuestId().toString()) ) ;
		}
	}

}
