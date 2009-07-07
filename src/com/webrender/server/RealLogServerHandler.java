package com.webrender.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

public class RealLogServerHandler extends IoHandlerAdapter {
	 private Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
	 
	  public void exceptionCaught(IoSession session, Throwable cause) {
		  // Close connection when unexpected exception is caught.
		  session.close();
	  }
	  
	  public void sessionOpened(IoSession session) throws Exception{
		  sessions.add(session);
	//	  System.out.println("New Session. totalNum:"+sessions.size() );
	  }
	  
	  public void messageReceived(IoSession session, Object message) {
	        System.out.println("RealLogServer:"+ message);
	  }
	  public void broadcast(String message) {
		  synchronized (sessions) {
			  Iterator iter = sessions.iterator();
			  while (iter.hasNext()) {
				  IoSession s = (IoSession) iter.next();
				  if (s.isConnected()) {
					  s.write(message);
				  }
			  }
		  }
	  }
	  public void sessionClosed(IoSession session) throws Exception {
		  sessions.remove(session);
		 // broadcast("SessionClosed totalNum:"+sessions.size());
	    }
}
