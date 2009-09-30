package com.webrender.axis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerOperate extends BaseAxis {
	private static final Log LOG = LogFactory.getLog(ServerOperate.class);
	public String restartServer(){
		try {
			LOG.info("restart Server");
			
			String path = ServerOperate.class.getResource("/").getPath();
			int index = path.indexOf("webapps");
			String tomcatHome =  path.substring(0,index) ;
			Process process = Runtime.getRuntime().exec(tomcatHome+"bin/restart.bat");
			BufferedReader in = new BufferedReader(  
					new InputStreamReader(process.getInputStream()));  
			String line = null;  
			while ((line = in.readLine()) != null) {  
				LOG.info(line);
			}  
			LOG.info("restart Server finish");
			return BaseAxis.ACTIONSUCCESS;
		} catch (Exception e) {
			LOG.error("restartServer failed",e);
			return BaseAxis.ACTIONFAILURE;
		}   
	}
}
