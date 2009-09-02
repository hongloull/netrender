package com.webrender.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.webrender.config.GenericConfig;
import com.webrender.config.XMLConfigManager;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;

public final class Conversion extends Thread {
	private  String mainServer = GenericConfig.getInstance().getMainServer();
	private  String subServer  = GenericConfig.getInstance().getSubServer();
	private static final Log LOG = LogFactory.getLog(Conversion.class);
	private static Conversion instance = new Conversion(); 
	// 标记该服务器类型 flag = 0 无用，1为主机 2为副机
	private  int flag = 0; 
	// 标记该服务器状态 0 未启动过 ， 1 运行中 2 暂停
	private CODE status = EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_OFF");
	
	public static Conversion  getInstance()
	{
		return instance;
	}
	
	private Conversion()
	{
		InetAddress myIp;
		try {
			setDaemon(true);
			myIp = InetAddress.getLocalHost();
			LOG.info("local IP : "+myIp.getHostAddress());
			if ("localhost".equals(mainServer) ||myIp.getHostAddress().equals(mainServer)) flag = 1;
			else if ("localhost".equals(subServer) ||myIp.getHostAddress().equals(subServer))  flag = 2;
		} catch (UnknownHostException e) {
		}
		
	}
	public int getFlag(){
		return flag;
	}
	
	public void run()
	{
		switch (flag)
		{
		case 1:{// MainServer
			// 通知SubServer，暂停工作，运行转交MainServer
			// MainServer运行 start()
			LOG.info("MainServer Run");
			XMLConfigManager.loadConfig();
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
					if(this.status==EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_ON")) {
						i=0;
						try {
							sleep(60000);
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
					}
					i++;
					if (i>=5)
					{
						LOG.info("SubServer Run");
						this.runServer();
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
			LOG.error("Neither mainServer nor subServer ,Server don't run!!!!!!!!!!!!!");
			break;
		}
		}
	}
	

	private void runServer()
	{
		if (status.equals(EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_ON")))
		{
			return;
		}
		
		if (status.equals(EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_OFF"))){
			LOG.info("SocketServer Run");
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
		else if (status.equals(EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_PAUSE")))
		{
			LOG.info("ControlThreadServer restart");
			ControlThreadServer.getInstance().resume();
//			NodeThreadServer.getInstance().resume();
		}
		status = EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_ON");
	}
	
	private void pauseServer()
	{
		if ( status.equals(EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_ON")))
		{
			ControlThreadServer.getInstance().threadSuspend("pauseServer");
//			NodeThreadServer.getInstance().threadStop("NodeThreadServer stop");
			status = EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_PAUSE");
		}
	}
	public CODE getStatus()
	{
		return status;
	}
		
	private boolean isRun(String mainServer2) {						
		try{
			LOG.debug("isRun ? "+mainServer2);
			String endpoint="http://"+mainServer2+":8080/WebRender/services/UserLogin?wsdl";
			org.apache.axis.client.Service service=new org.apache.axis.client.Service();
			org.apache.axis.client.Call call=(org.apache.axis.client.Call)service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new javax.xml.namespace.QName("http://www.animationsp.com","isRun"));
			call.setTimeout(2000);
			Object result=call.invoke(new Object[]{});
			if ( result.equals(EOPCODES.getInstance().get("S_SERVERSTATUS").getSubCode("S_ON").getId()) ){
				LOG.debug(mainServer2+" already run");
				return true;
			}
			else 
			{
				LOG.debug(mainServer2+" not run");
				return false;
			}
			
		}catch(Exception e){
			LOG.debug(mainServer2+" not run");
			return false;
		}
	}
	
}
