package com.webrender.config;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.webrender.dao.HibernateSessionFactory;

public class XMLConfigManager {
	private static String[] xmls = {"templates","nodes","right"};
//	private static boolean[] pathExist = {false,false};
//	private static String basePath = XMLConfigManager.class.getResource("/").getPath();
	private static final Log log = LogFactory.getLog(XMLConfigManager.class);
	public static void loadConfig()
	{
		int length = xmls.length;
		for (int i = 0 ; i<length;i++)
		{
			try{
				String basePath = GenericConfig.getInstance().getFile(xmls[i]);
				File dir = new File(basePath);
				if( dir.isDirectory() ){
					log.info("Read ConfigDir : "+ dir.getAbsolutePath() );
					File[] files = dir.listFiles();
					int filesNum = files.length;
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
				log.error(xmls[i] +" not exist! ");
			}
			catch(Exception e){
				log.error("",e);
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
