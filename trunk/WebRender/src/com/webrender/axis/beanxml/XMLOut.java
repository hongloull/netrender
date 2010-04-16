package com.webrender.axis.beanxml;


import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

public final class XMLOut {
	
	private static final Log LOG = LogFactory.getLog(XMLOut.class);
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
		LOG.error("xml to string fail",e);
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
			  LOG.error("xml to file fail",e);
		  }
	}
}
