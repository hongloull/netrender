package com.webrender.test;

import java.util.Date;

public class DateToDouble {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(date.toLocaleString());
		System.out.println(date.getTime());
	}

}
