package com.webrender.axis;

import org.junit.Test;

import com.webrender.axis.ConfigOperate;

public class TestConfigOperate {
	@Test
	public void testGetConfig()
	{
		ConfigOperate co = new ConfigOperate();
		System.out.println( co.getPathConfig());
	}
	
	@Test
	public void testSetConfig()
	{
		ConfigOperate co = new ConfigOperate();
		String questXML = "";
		System.out.println( co.setPathConfig(questXML) );
	}
		
}
