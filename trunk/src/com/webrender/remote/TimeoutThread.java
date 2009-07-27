package com.webrender.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeoutThread extends Thread {
	private static final Log log = LogFactory.getLog(TimeoutThread.class);
	private long timeOut;
	private boolean isTimeOut;
	private boolean isStop;
	private TimeoutOperate timeOutOperate;
	private Object obj;
	
	public TimeoutThread(long timeOut ,Object obj,TimeoutOperate timeOutOperate){
		super();
		this.timeOut = timeOut;
		this.timeOutOperate = timeOutOperate;
		this.obj = obj;
		this.setDaemon(true);
	}
	
	public void reset()
	{
		isTimeOut=false;
	}
	
	public void cancel()
	{
		isStop=true;
	}
	
	public void run(){
		try{
			while(true){
				
				if(isStop) break;
				isTimeOut = true;
				Thread.sleep(timeOut);
				log.debug(obj+" isTimeOut:"+isTimeOut);
				log.debug(obj+" isStop:"+isStop);
				if(isStop) break;
				if(isTimeOut) timeOutOperate.timeOutOperate(obj);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
