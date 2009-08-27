package com.webrender.test;

import java.util.HashSet;
import java.util.Set;

public class TestHashSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(2);
		set.add(3);
		set.remove(4);
		if(  set.contains(2) ) System.out.println("True");
		else System.out.println("False");
	}

}
