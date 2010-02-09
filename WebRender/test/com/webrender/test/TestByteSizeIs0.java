package com.webrender.test;

import java.util.ArrayList;

import org.apache.mina.common.ByteBuffer;
import org.junit.Test;

public class TestByteSizeIs0 {
	@Test
	public void testByteSizeIs0(){
		byte[] test = new byte[0];
		assert(test.length==0);
		ByteBuffer fmts = ByteBuffer.allocate(0);
		assert( fmts.array().length==0 );
		ArrayList<String> datas = new ArrayList<String>();
		assert( datas.toArray().length==0);
	}
}
