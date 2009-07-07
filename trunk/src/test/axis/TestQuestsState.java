package test.axis;

import org.junit.Test;

import com.webrender.axis.QuestsState;

public class TestQuestsState {
/*	@Test
	public void testGetQuestStatus()
	{
		QuestsState qs = new QuestsState();
		System.out.println( qs.getQuestStatus("70") );
	}
*/	
	@Test
	public void testGetQuestsStatus()
	{
		for(int i =0 ; i<10;i++){
			QuestsState qs = new QuestsState();
			System.out.println(qs.getQuestsStatus());			
		}
	}
}
