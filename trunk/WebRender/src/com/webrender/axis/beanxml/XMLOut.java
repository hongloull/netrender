package com.webrender.axis.beanxml;


import java.io.File;
import java.io.FileOutputStream;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

public final class XMLOut {
	
	
	public String outputToString(Document document)
	{
//	  ByteArrayOutputStream byteRep=new ByteArrayOutputStream();
	  XMLOutputter docWriter=new XMLOutputter(null,false,"UTF8");
	  docWriter.setEncoding("UTF8");
	  try
	  {
//		  docWriter.output(document,new   FileOutputStream("d:/quests.xml"));   
		 // docWriter.output(document,System.out);
		  return docWriter.outputString(document);	  	
	  }
	  catch(Exception e)
	  {
	  	e.printStackTrace();
	  	return null;
	  }
 	}
	
	public void outputToFile(Document doc ,File file){
		XMLOutputter docWriter=new XMLOutputter(null,false,"UTF8");
		  docWriter.setEncoding("UTF8");
		  try
		  {
			  FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
			  docWriter.output(doc,fileOut);
		  }
		  catch(Exception e)
		  {
		  	e.printStackTrace();
		  }
	}
}
