package com.webrender.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.HibernateSessionFactory;

public class XMLConfigManager {
	private static String[] xmls = {"templates","nodes","users"};
//	private static boolean[] pathExist = {false,false};
//	private static String basePath = XMLConfigManager.class.getResource("/").getPath();
	private static final Log LOG = LogFactory.getLog(XMLConfigManager.class);
	public static void loadConfig()
	{
		int length = xmls.length;
		for (int i = 0 ; i<length;i++)
		{
			try{
				String basePath = GenericConfig.getInstance().getFile(xmls[i]);
				File dir = new File(basePath);
				if( dir.isDirectory() ){
					File[] files = dir.listFiles();
					int filesNum = files.length;
					LOG.info("Read ConfigDir : "+ dir.getAbsolutePath() +" filesNum:"+filesNum);
					XMLConfig load = null;
					for(int j=0;j<filesNum;j++){
						if (files[j].isFile()){
							
								load = XMLConfigFactory.getXMLConfig(files[j]);
								load.loadFromXML(files[j]);	
						}
					}
					// 删除文件夹内不包含的数据 etc: NodeGroup
					load.deleteExtraData();
				}
			}
			catch(NullPointerException e){
				LOG.error(xmls[i] +" not exist! ",e);
			}
			catch(Exception e){
				LOG.error("loadConfig fail",e);
			}finally{
				HibernateSessionFactory.closeSession();
			}
		}
//		if (pathExist){
//			CommandModelXMLConfig.deleteExtraModel();
//			
//		}
	}
}
