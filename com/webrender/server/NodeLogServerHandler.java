package com.webrender.server;



import org.apache.mina.common.ByteBuffer;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.webrender.dao.HibernateSessionFactory;

import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.handler.MessageHandler;
import com.webrender.protocol.handler.MessageHandlerImpl;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class NodeLogServerHandler extends IoHandlerAdapter {
	private static final Log LOG = LogFactory.getLog(NodeLogServerHandler.class);
	public void exceptionCaught(IoSession session, Throwable cause) {
		  // Close connection when unexpected exception is caught.
		cause.printStackTrace();
//		session.write(cause.getMessage());
//		session.close();
	  }
	
	public void sessionOpened(IoSession session) throws Exception{
		//  System.out.println("New Session" );
				
	  }
	
	public void messageReceived(IoSession session, Object message) {
		if (!(message instanceof ByteBuffer)){
			LOG.info(message + "'type isn't ByteBuffer!");
            return;
        }
//		LOG.info( "getnewmessage:: " + message );
		
		byte opCode = -1 ;
		try{
			MessageHandler handler = MessageHandlerImpl.getInstance();
			Integer nodeId = (Integer)session.getAttribute("nodeId");
			ByteBuffer buffer = (ByteBuffer) message ;
			if (buffer.limit() >= buffer.capacity())
			{
//				LOG.info(buffer);
				ByteBuffer lastBuffer = (ByteBuffer) session.getAttribute("HalfPacket");
				
				if ( lastBuffer == null ) {
					LOG.debug("first add HalfPkt to session attribute");
					lastBuffer = ByteBuffer.allocate(buffer.limit());
					lastBuffer.put(buffer);
					session.setAttribute("HalfPacket",lastBuffer);
					buffer = null;
					
				}else {
					LOG.debug("append HalfPkt to session attribute");
					lastBuffer.expand(buffer.limit());
					lastBuffer.put(buffer);
					session.setAttribute("HalfPacket",lastBuffer);
				}
			}
			else  // 没有不完整的包
			{
				ByteBuffer lastBuffer = (ByteBuffer) session.getAttribute("HalfPacket");
				if ( lastBuffer != null ) {
//					LOG.info(buffer);
					LOG.debug("get LastBuffer parse");
					lastBuffer.expand(buffer.limit());
					lastBuffer.put(buffer);
					session.setAttribute("HalfPacket",null);
					buffer = null;
					lastBuffer.flip();
					LOG.debug(lastBuffer);
				}
				else{
					lastBuffer = buffer;
					buffer = null;
				}
				while(lastBuffer.hasRemaining()){
					
					opCode = lastBuffer.get();
//					LOG.info("CODEID:"+ opCode + "NodeId :" +nodeId);
//					LOG.info("parse LastBuffer position:"+lastBuffer.position() +" limit:" + lastBuffer.limit()+" capacity:"+ lastBuffer.capacity());
					if( EOPCODES.getInstance().get("N_RUN").getId() == opCode){
						if (nodeId != null){ 
							// run more than one time
							return ;
						}
						nodeId = handler.initialClient(lastBuffer);
						if(nodeId == 0){
							LOG.warn("N_RUN initialClient fail nodeId=0");
						}else{
							session.setAttribute("nodeId",nodeId);
							LOG.info("nodeId:"+nodeId + " run success.");
							NodeMachine processor = NodeMachineManager.getNodeMachine(nodeId);
							processor.setSession(session);
							session.write(ServerMessages.createConnectFlagPkt(nodeId));					
						}
					}
					else if (EOPCODES.getInstance().get("N_GETSERVERSTATUS").getId()== opCode){
						lastBuffer.get(); // 后面跟了00 需要读取
						session.write(ServerMessages.createServerStatusPkt());
					}
					else if(nodeId !=null && nodeId !=0){
						ByteBuffer remainBuffer = handler.parseClientPacket(EOPCODES.getInstance().get(opCode),lastBuffer,NodeMachineManager.getNodeMachine(nodeId));
						if(remainBuffer !=null){ // 还有未结束的数据包
							session.setAttribute("HalfPacket",remainBuffer);
						}
					}
					else{
						
						LOG.error("messageReceived nodeId error:"+nodeId);
					}	
					
				}
			} // end if( buffer.limit() >= buffer.capacity() ) else
			
		}catch(Exception e){
			LOG.info("Error CODEID:"+ opCode);
			ByteBuffer result = (ByteBuffer) message;
			int remaining = result.remaining();
			byte[] bytes = new byte[remaining];
			result.get(bytes);
			String data = new String(bytes);
			LOG.info("remaining: " +data);			
			LOG.error("messageReceived:"+message,e);
//			LOG.error(message.toString());
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
//		LOG.info("Total " + session.getReadBytes() + " byte(s)");
		Integer  nodeId = (Integer)session.getAttribute("nodeId");
		if(nodeId!=null){
			LOG.info("NodeId:"+nodeId+" connect close.");
			NodeMachineManager.getNodeMachine(nodeId).setSession(null);
		}
		HibernateSessionFactory.closeSession();
	}
}
