package com.webrender.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class TestTest {
	@Test
	public void testEmpty(){
		Collection c1 = new ArrayList();
		assert c1.isEmpty();
	}
}
