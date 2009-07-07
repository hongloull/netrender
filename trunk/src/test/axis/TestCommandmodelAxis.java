package test.axis;

import com.webrender.axis.CommandmodelAxis;
import org.junit.Test;
public class TestCommandmodelAxis {
	@Test
	public void testGetCommandModels()
	{
		CommandmodelAxis axis = new CommandmodelAxis();
		System.out.println( axis.getCommandModels() );
		
	}
	@Test
	public void testGetCommandModel()
	{
		CommandmodelAxis axis = new CommandmodelAxis();
		System.out.println( axis.getCommandModel("1") );
	}
}
