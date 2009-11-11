package com.webrender.axis;

import org.junit.Test;

import com.webrender.axis.CommandOperate;

public class TestCommandOperate {
	@Test
	public void testGetRealLogs(){
		CommandOperate cOperate = new CommandOperate();
		String result = cOperate.getRealLogs("592");
		System.out.println( result );
	}
}
