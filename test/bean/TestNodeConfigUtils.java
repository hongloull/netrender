package test.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jdom.JDOMException;
import org.junit.Test;

import com.webrender.bean.nodeconfig.NodeConfig;
import com.webrender.bean.nodeconfig.NodeConfigUtils;

public class TestNodeConfigUtils {
	
	
	@Test
	public void testString2bean(){
		String XML;
		try {
			XML = readFileAsString("E:\\workspace\\WebRender\\src\\test\\bean\\NodeConfig.xml");
		} catch (IOException e1) {
			XML = null;
			e1.printStackTrace();
		}
		 try {
			NodeConfig config = NodeConfigUtils.xmlString2bean(XML);
			assert config.getGeneralConfig().getPriority().equals("0");
			
		} catch (JDOMException e) {
			
			e.printStackTrace();
		}
	}
	
	private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
