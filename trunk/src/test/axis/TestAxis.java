package test.axis;

import org.junit.Test;

import com.webrender.axis.CommandmodelAxis;
import com.webrender.axis.QuestOperate;

public class TestAxis {
	@Test
	public void testGetCommandModel()
	{
		CommandmodelAxis cmAxis = new CommandmodelAxis();
		System.out.println( cmAxis.getCommandModel("1") ); 
		
	}

	@Test
	public void testGetDetail()
	{
		QuestOperate qo  = new QuestOperate();
		System.out.println( qo.getDetail("84"));
	}
	
}
