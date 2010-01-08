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
		assertTrue( impl.CommitQuest(null, 0, true).equals("Failure null") );
		assertTrue( impl.CommitQuest("", 0, true).equals("Failure Premature end of file.: Error on line -1: Premature end of file.") );
		
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#deleteQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testDeleteQuest() {
		System.out.println( impl.deleteQuest(null, 0).equals("Failure null"));
		System.out.println( impl.deleteQuest("0", 0).equals("Failure null") );
		System.out.println( impl.deleteQuest("A", 0).equals("Failure For input string: \"A\"") );
		
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#pauseQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testPauseQuest() {
		System.out.println( impl.pauseQuest(null, 0).equals("Failure null"));
		System.out.println( impl.pauseQuest("0", 0).equals("Failure null") );
		System.out.println( impl.pauseQuest("A", 0).equals("Failure For input string: \"A\"") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#resumeQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testResumeQuest() {
		System.out.println( impl.resumeQuest(null, 0).equals("Failure null"));
		System.out.println( impl.resumeQuest("0", 0).equals("Failure null") );
		System.out.println( impl.resumeQuest("A", 0).equals("Failure For input string: \"A\"") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#reinitQuest(java.lang.String, int)}.
	 */
	@Test
	public final void testReinitQuest() {
		System.out.println( impl.reinitQuest(null, 0).equals("Failure null"));
		System.out.println( impl.reinitQuest("0", 0).equals("Failure null") );
		System.out.println( impl.reinitQuest("A", 0).equals("Failure For input string: \"A\"") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#setFinish(java.lang.String, int)}.
	 */
	@Test
	public final void testSetFinish() {
		System.out.println( impl.setFinish(null, 0).equals("Failure null"));
		System.out.println( impl.setFinish("0", 0).equals("Failure null") );
		System.out.println( impl.setFinish("A", 0).equals("Failure For input string: \"A\"") );
	}

	/**
	 * Test method for {@link com.webrender.axis.operate.QuestOperateImpl#changeName(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public final void testChangeName() {
		System.out.println( impl.changeName(null, null, 0).equals("Failure null"));
		System.out.println( impl.changeName("0",null,0).equals("Failure null") );
		System.out.println( impl.changeName("A",null ,0).equals("Failure For input string: \"A\"") );
		Quest quest = (Quest)dao.findAll().get(0);
		System.out.println( impl.changeName(quest.getQuestId().toString(), null, 0).equals("Failure null"));
		System.out.println( impl.changeName(quest.getQuestId().toString(),"",0).equals("Failure null") );
		
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
