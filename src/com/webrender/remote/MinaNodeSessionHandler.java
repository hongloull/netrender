package com.webrender.remote;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;




public class MinaNodeSessionHandler extends IoHandlerAdapter {
	
	private NodeMachine nodeMachine = null;
	private static final Log log = LogFactory.getLog(MinaNodeSessionHandler.class);
	public MinaNodeSessionHandler(NodeMachine nodeMachine){
		this.nodeMachine = nodeMachine;
	}
	
	public void sessionOpened(IoSession session) {
		
		log.info("Connect to "+session.getRemoteAddress());
		this.nodeMachine.setConnect(true);
	}
	
	public void sessionClosed(IoSession session) {	
		
		log.info("Total " + session.getReadBytes() + " byte(s)");
		this.nodeMachine.setConnect(false);
	}

	public void messageReceived(IoSession session, Object message) {
		
		if ( "***START***".equals(message ) ){
			session.setAttribute("StartFlag","Success");
		}
		else{
			log.debug("StatusMessage:"+message);
		//	session.setAttachment(message);
			this.nodeMachine.updateStatus((String)message);
		}
	}
	
	public void exceptionCaught(IoSession session, Throwable cause) {
	//	log.error("ServerConnectNode error",cause);
		session.close();
	}
}
