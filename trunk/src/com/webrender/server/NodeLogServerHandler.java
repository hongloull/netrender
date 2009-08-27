package com.webrender.server;


import org.apache.mina.common.ByteBuffer;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.webrender.dao.HibernateSessionFactory;

import com.webrender.protocol.enumn.EOPCODE;
import com.webrender.protocol.handler.MessageHandler;
import com.webrender.protocol.handler.MessageHandlerImpl;
import com.webrender.protocol.messages.ClientMessages;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class NodeLogServerHandler extends IoHandlerAdapter {
	private static final Log LOG = LogFactory.getLog(NodeLogServerHandler.class);
	public void exceptionCaught(IoSession session, Throwable cause) {
		  // Close connection when unexpected exception is caught.
		cause.printStackTrace();
		session.close();
	  }
	
	public void sessionOpened(IoSession session) throws Exception{
		//  System.out.println("New Session" );
				
	  }
	
	public void messageReceived(IoSession session, Object message) {
		System.out.println(message.toString());
		if (!(message instanceof ByteBuffer)){
			LOG.info(message + "'type isn't ByteBuffer!");
            return;
        }
		try{
			MessageHandler handler = MessageHandlerImpl.getInstance();
			Integer nodeId = (Integer)session.getAttribute("nodeId");
//			LOG.info("message CODE: "+((ByteBuffer)message).get());
//			LOG.info("ClientMessage"+ClientMessages.createRunPkt(0, "ASP127") );
			ByteBuffer buffer = (ByteBuffer) message ;//ClientMessages.createRunPkt(16, "中文");
			
			if (EOPCODE.RUN == EOPCODE.values()[buffer.get()]){
				nodeId = handler.initialClient(buffer);
				session.setAttribute("nodeId",nodeId);
				NodeMachine processor = NodeMachineManager.getNodeMachine(nodeId);
				processor.setSession(session);
				session.write(ServerMessages.createConnectFlag(nodeId));
			}
			else if(nodeId !=null && nodeId !=0){
				handler.parseClientPacket((ByteBuffer)message,NodeMachineManager.getNodeMachine(nodeId));
			}
			else{
				LOG.error("nodeId is null or 0!  messageReceived:"+message);
			}			
		}catch(Exception e){
			LOG.error("messageReceived fail",e);
		}
//		String ip = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
//		
//		String theMessage = (String) message;
//		LOG.debug(ip+": "+theMessage);
//		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(ip);
//		
//		if(theMessage.startsWith("***RUN***")){
//			LOG.debug(ip+theMessage);
//			NodeDAO nodeDAO  = new NodeDAO();
//			Transaction tx = null;
//			try{
//				tx = HibernateSessionFactory.getSession().beginTransaction();
//				Node node = nodeDAO.findByNodeIp(ip);
//				if( node ==null ){
//					node = new Node();
//					node.setNodeIp(ip);
//				}
//				node.setNodeName(theMessage.substring(9));//***RUN***之后的字符
//				nodeDAO.save(node);
//				tx.commit();
//				LOG.debug(ip+" RUN OK");
//				//	NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(ip);
//				
//				session.write("***OK***");
//				nodeMachine.testConnect();
//			}
//			catch(Exception e)
//			{
//				LOG.error(ip+" run fail ",e );
//				if (tx != null) 
//				{
//					tx.rollback();
//				}
//			}finally{
//				HibernateSessionFactory.closeSession();
//			}
//			return;
//		}
//		try{
//			Iterator iteCommands = nodeMachine.getCurrentCommands().iterator();
//			if(iteCommands.hasNext()){
//				nodeMachine.addRealLog(nodeMachine.getCurrentCommands().iterator().next(),theMessage);
//				if (  nodeMachine.isRealTime() )
//				{
//					RealLogServer.getInstance().broadCast(ip+"***"+theMessage);
//				}
//				
//			}
//			else{
//				LOG.debug(theMessage);
//			}
//		}
//		catch(Exception e){
//			LOG.error("messageReceived fail",e);
//		}finally{
//			HibernateSessionFactory.closeSession();
//		}
	}
	
	public void sessionClosed(IoSession session) throws Exception {
		LOG.info("Total " + session.getReadBytes() + " byte(s)");
		Integer  nodeId = (Integer)session.getAttribute("nodeId");
		if(nodeId!=null && nodeId!=null){
			NodeMachineManager.getNodeMachine(nodeId).setSession(null);
		}
		HibernateSessionFactory.closeSession();
	}
}
