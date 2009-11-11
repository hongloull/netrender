package com.webrender.axis;

import org.junit.Test;

import com.webrender.axis.PoolOperate;

public class TestPoolOperate {
	@Test
	public void testGetPools()
	{
		PoolOperate po = new PoolOperate();
		System.out.println( po.getPools());
	}
	@Test
	public void testAddPool()
	{
		String poolName   = "NGroup3";
		PoolOperate po = new PoolOperate();
		System.out.println( po.addPool(poolName) );
	}
	
	@Test
	public void testmodPool()
	{
		String name   = "NGroup3";
		String questXML = "<NodeGroup timeGroup=\"All\"><Node nodeIp=\"192.168.20.127\" /><Node nodeIp=\"192.168.20.227\" /></NodeGroup>";
		PoolOperate po = new PoolOperate();
		System.out.println(po.modPool(name, questXML));
	}
	@Test
	public void testGetPoolConfig(){
		String name   = "NGroup3";
		PoolOperate po = new PoolOperate();
		System.out.println( po.getPoolConfig(name));
	}
	@Test
	public void testDelPool(){
		String name = "NGroup3";
		PoolOperate po = new PoolOperate();
		System.out.println(po.delPool(name));
	}
	
}
