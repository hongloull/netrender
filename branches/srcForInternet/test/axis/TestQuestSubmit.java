package test.axis;

import org.junit.Test;

import com.webrender.axis.ModelOperate;
import com.webrender.axis.QuestOperate;

public class TestQuestSubmit {
	@Test
	public void testGetCommandModel()
	{
		ModelOperate cmAxis = new ModelOperate();
		System.out.println( cmAxis.getModel("1") ); 
		
	}

	@Test
	public void testGetDetail()
	{
		QuestOperate qo  = new QuestOperate();
		System.out.println( qo.getDetail("115"));
	}
	
}
