package com.webrender.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveLogToFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("d:\\test.log");
		String message = "中文试试看";
		try {
			
				FileOutputStream fops = new FileOutputStream(file);
				fops.write(message.getBytes());
				fops.write(message.getBytes());
				fops.close();

			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
