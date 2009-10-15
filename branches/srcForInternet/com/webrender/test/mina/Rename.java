package com.webrender.test.mina;

import java.io.File;

public class Rename {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("Y:\\taobao");
		if(file.isDirectory()){
			File[] files = file.listFiles();
			int length = files.length;
			System.out.println(length);
			
			for (int i=0;i<length;i++){
				String[] names = files[i].getName().split(".JPG");
				if(names.length>1){
					String newName = names[0]+".JPG";
					System.out.println(newName);
					File newFile = new File("Y:\\taobao2\\"+newName);
					files[i].renameTo(newFile);
				}
			}
		}

	}

}
