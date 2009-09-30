package com.webrender.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ControlListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
	
//		StatusReceiver statusReceiver = StatusReceiver.getInstance();
//		if( statusReceiver.isAlive() == false) statusReceiver.start(); 
		
		Conversion.getInstance().start();
		
	}
}
