package com.webrender.test.mina;

import java.io.File;

public class Rename {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "Y:\\software863\\TEMP\\scenes\\Episodes\\";
		File file = new File(path);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			int length = files.length;
			System.out.println("file Num: "+ length);
			
			for (int i=0;i<length;i++){
				String[] names = files[i].getName().split("kx_");
				if(names.length>1){
					String newName = "s"+names[1];
					System.out.println(newName);
					File newFile = new File(path+newName);
					files[i].renameTo(newFile);
				}
			}
		}

	}

}
