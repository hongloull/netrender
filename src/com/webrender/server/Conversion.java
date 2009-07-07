package com.webrender.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.webrender.config.GenericConfig;
import com.webrender.config.XMLConfigManager;

public class Conversion extends Thread {
	private  String mainServer = GenericConfig.getInstance().getMainServer();
	private  String subServer  = GenericConfig.getInstance().getSubServer();
	private static final Log log = LogFactory.getLog(Conversion.class);
	private static Conversion instance = new Conversion(); 
	// 标记该服务器类型 flag = 0 无用，1为主机 2为副机
	private  int flag = 0; 
	// 标记该服务器状态 0 未启动过 ， 1 运行中 2 暂停
	private int status = 0;
	
	public static Conversion  getInstance()
	{
		return instance;
	}
	
	private Conversion()
	{
		InetAddress myIp;
		try {
			myIp = InetAddress.getLocalHost();
			log.info("local IP : "+myIp.getHostAddress());
			if (myIp.getHostAddress().equals(mainServer)) flag = 1;
			else if (myIp.getHostAddress().equals(subServer))  flag = 2;
		} catch (UnknownHostException e) {
		}
		
	}
	
	public void run()
	{
		switch (flag)
		{
		case 1:{// MainServer
			// 通知SubServer，暂停工作，运行转交MainServer
			// MainServer运行 start()
			log.info("MainServer Run");
			this.runServer();
			break;
		}
		case 2:{ //SubServer
			// 判断MainServer是否正常运行，若MainServer启动了，做心动检测，等到检测出错（MainServer运行有问题时） 启动SubServer
			int i = 0 ;
			while (true)
			{
				if ( isRun(mainServer)==false)
				{
					i++;
					if (i>=5)
					{
						log.info("SubServer Run");
						this.runServer();
						try {
							sleep(60000);
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						i = 0;
					}
					
				}
				else
				{
					this.pauseServer();
					try {
						sleep(60000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					i = 0;
				}
			}
		}
		case 0:{
			log.error("Neither mainServer nor subServer ,Server don't run!!!!!!!!!!!!!");
			break;
		}
		}
	}
	

	private void runServer()
	{
		if (status == 1)
		{
			return;
		}
		XMLConfigManager.loadConfig();
		if (status == 0){
			log.info("SocketServer Run");
			try {
				NodeLogServer.getInstance().run();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			NodeThreadServer.getInstance().start();
			try {
				RealLogServer.getInstance().run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ExecuteLogServer.getInstance().run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ControlThreadServer.getInstance().start();			
		}
		else if (status ==2)
		{
			log.info("ControlThreadServer NodeThreadServer restart");
			ControlThreadServer.getInstance().resume();
//			NodeThreadServer.getInstance().resume();
		}
		status = 1;
	}
	
	private void pauseServer()
	{
		if ( status == 1)
		{
			ControlThreadServer.getInstance().threadSuspend(" ControlThreadServer stop");
//			NodeThreadServer.getInstance().threadStop("NodeThreadServer stop");
			status = 2;
		}
	}
	public int getStatus()
	{
		return status;
	}
		
	private boolean isRun(String mainServer2) {						
		try{
			log.debug("isRun ? "+mainServer2);
			String endpoint="http://"+mainServer2+":8080/WebRender/services/UserLogin?wsdl";
			org.apache.axis.client.Service service=new org.apache.axis.client.Service();
			org.apache.axis.client.Call call=(org.apache.axis.client.Call)service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new javax.xml.namespace.QName("http://www.animationsp.com","isRun"));
			call.setTimeout(2000);
			Object result=call.invoke(new Object[]{});
			if ( result.equals(1) ){
				log.debug(mainServer2+" already run");
				return true;
			}
			else 
			{
				log.debug(mainServer2+" not run");
				return false;
			}
			
		}catch(Exception e){
			log.debug(mainServer2+" not run");
			return false;
		}
	}
	
}
