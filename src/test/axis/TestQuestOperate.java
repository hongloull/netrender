package test.axis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.webrender.axis.QuestOperate;

public class TestQuestOperate{
	@Test
	public void testCommitQuest()
	{
		String questXML = "<Quest  questName=\"testQuestargtocommand\" pri=\"3\" information=\"test\"><Model commandModelId=\"1\" ></Model><Node nodeId=\"1\"/><Node nodeId=\"2\"></Node><Questarg commandModelArgId=\"1\" value=\"/mnt/production/ASP_RnD/rmsProject/scenes/renderTest.mb\" /><Questarg commandModelArgId=\"6\" value=\"/mnt/production/ASP_RnD/rmsProject\" /><Questarg commandModelArgId=\"2\" value=\"100\" /><Questarg commandModelArgId=\"3\" value=\"200\" /><Questarg commandModelArgId=\"4\" value=\"1.0\" /><Questarg commandModelArgId=\"5\" value=\"file\" ></Questarg><Questarg commandModelArgId=\"7\" value=\"3\" /></Quest>";
		System.out.println(questXML);
		//QuestOperate qo = new QuestOperate();
		//assertEquals(qo.CommitQuest(questXML),"<ResultSet>Success</ResultSet>");
	}
	@Test
	public void testChunkDetail()
	{
		QuestOperate qo = new QuestOperate();
		System.out.println(qo.getChunkDetail("33"));
	}
}
