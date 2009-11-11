package com.webrender.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestFramesValue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String regEx = "\\d+(\\.\\d+)?";
		String str = "1.3,1.3,13.5,144-15*12.0";
		Pattern p=Pattern.compile(regEx);
		Matcher ma = p.matcher(str);
		while(ma.find()){
			System.out.println( ma.group() );
		}
	}

}
