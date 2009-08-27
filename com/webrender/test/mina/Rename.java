package com.webrender.test.mina;

import java.io.File;

public class Rename {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("Y:\\yiKeTe\\3d\\tmp");
		if(file.isDirectory()){
			File[] files = file.listFiles();
			int length = files.length;
			System.out.println(length);
			
			for (int i=0;i<length;i++){
				String[] names = files[i].getName().split("_Depth");
				if(names.length==2){
					String newName = names[0]+names[1];
					System.out.println(newName);
					File newFile = new File("Y:\\yiKeTe\\3d\\temp2\\"+newName);
					files[i].renameTo(newFile);
				}
			}
		}

	}

}
