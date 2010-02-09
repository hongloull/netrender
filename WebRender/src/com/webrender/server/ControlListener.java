package com.webrender.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ControlListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		Conversion.getInstance().stopServer();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		Conversion.getInstance().start();	
	}
}
