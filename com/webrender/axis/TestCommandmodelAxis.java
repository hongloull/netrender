package com.webrender.axis;

import com.webrender.axis.ModelOperate;
import org.junit.Test;
public class TestCommandmodelAxis {
	@Test
	public void testGetCommandModels()
	{
		ModelOperate axis = new ModelOperate();
		System.out.println( axis.getModels() );
		
	}
	@Test
	public void testGetCommandModel()
	{
		ModelOperate axis = new ModelOperate();
		System.out.println( axis.getModel("1") );
	}
}
