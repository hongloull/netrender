package com.webrender.config;

import java.io.File;

import org.junit.Test;

import com.webrender.config.GenericConfig;

public class TestConfig {
	@Test
	public void testConfig()
	{
		File dir = new File(GenericConfig.getInstance().getFile("templates"));
		if( dir.isDirectory() ){
			System.out.println("isDir");
		}
		System.out.println(GenericConfig.getInstance().getMainServer());
		System.out.println(GenericConfig.getInstance().getRealLogPort());
		System.out.println(GenericConfig.getInstance().getSubServer());
	}
}
