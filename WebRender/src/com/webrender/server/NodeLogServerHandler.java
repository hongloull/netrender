package com.webrender.server;



import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.common.ByteBuffer;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IdleStatus;
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
	private ServerMessages serverMessages = new ServerMessages();
	private Set<IoSession> proxySessions = Collections.synchronizedSet(new HashSet<IoSession>());
	public void exceptionCaught(IoSession session, Throwable cause) {
		  // Close connection when unexpected exception is caught.
		LOG.error("NodeLogServerHandler exception",cause);
//		session.write(cause.getMessage());
//		session.close();
	  }
	
	public void sessionOpened(IoSession session) throws Exception{
		
		session.setIdleTime(IdleStatus.READER_IDLE, 60);
				
	  }
	public void sessionIdle(IoSession session, IdleStatus status) {
        LOG.info("nodeId:"+session.getAttribute("nodeId")+"*** IDLE *** "+session.getRemoteAddress());
        session.close();
    }
	public void messageReceived(IoSession session, Object message) {
		if (!(message instanceof ByteBuffer)){
			LOG.info(message + "'type isn't ByteBuffer!");
            return;
        }
//		LOG.info( "getnewmessage:: " + message );
		ByteBuffer lastBuffer = null;
		byte opCode = -1 ;
		try{
			MessageHandler handler = MessageHandlerImpl.getInstance();
			Integer nodeId = (Integer)session.getAttribute("nodeId");
			ByteBuffer buffer = (ByteBuffer) message ;
			if (buffer.limit() >= buffer.capacity())
			{
//				LOG.info(buffer);
				lastBuffer = (ByteBuffer) session.getAttribute("HalfPacket");
				
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
				lastBuffer = (ByteBuffer) session.getAttribute("HalfPacket");
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
							LOG.warn("N_RUN fail nodeInfo:"+session.getRemoteAddress());
							session.close();
						}else{
							session.setAttribute("nodeId",nodeId);
							LOG.debug("nodeId:"+nodeId + " run success.");
							NodeMachine processor = NodeMachineManager.getInstance().getNodeMachine(nodeId);
							processor.setSession(session);
							session.write(serverMessages.createConnectFlagPkt(nodeId));					
						}
					}
					else if (EOPCODES.getInstance().get("N_GETSERVERSTATUS").getId()== opCode){
						lastBuffer.get(); // 后面跟了00 需要读取 丢掉。
						session.write(serverMessages.createServerStatusPkt());
					}
					else if(EOPCODES.getInstance().get("N_PROXY").getId() == opCode){
						// Proxy node
						if(!proxySessions.contains(session)){
							LOG.info("new proxy connect.");
							proxySessions.add(session);
							session.setIdleTime(IdleStatus.READER_IDLE, 0);
//							session.write(serverMessages.createServerStatusPkt());
						}
						
					}
					else if(nodeId !=null && nodeId !=0){
						ByteBuffer remainBuffer = handler.parseClientPacket(EOPCODES.getInstance().get(opCode),lastBuffer,NodeMachineManager.getInstance().getNodeMachine(nodeId));
						if(remainBuffer !=null){ // 还有未结束的数据包
							session.setAttribute("HalfPacket",remainBuffer);
						}
					}
					else{
						//TODO 错误处理，是否清空HalfPacket；是否跳出while(lastBuffer.hasRemaining())循环
//						LOG.error("messageReceived error:nodeId="+nodeId+" CODE="+opCode);
					}	
					
				}
			} // end if( buffer.limit() >= buffer.capacity() ) else
			
		}catch(Exception e){
			LOG.info("Error CODEID:"+ opCode);
			ByteBuffer result = (ByteBuffer) message;
			String data = new String(result.array());
			if(lastBuffer!=null){
				String lastData = new String(lastBuffer.array()); 
				LOG.error("messageReceived parse failed: "+lastData);
			}
			LOG.error("messageReceived parse failed: " +data,e);			
			
			session.setAttribute("HalfPacket",null);
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
		if(nodeId!=null && nodeId!=0){
			//LOG.info("NodeId:"+nodeId+" session closed ."+" createTime: "+session.getCreationTime());
//			LOG.info("NodeId: "+nodeId + "  closed  old session isClosed:"+session.isClosing()+" isConnected: "+session.isConnected()+" createTime:"+session.getCreationTime());
			IoSession nodeSession = NodeMachineManager.getInstance().getNodeMachine(nodeId).getSession();
			if(nodeSession == session){
				LOG.info("NodeId: "+nodeId + " set session null");
				NodeMachineManager.getInstance().getNodeMachine(nodeId).setSession(null);
				NodeMachineManager.getInstance().deleteNodeMachine(nodeId);		
			}
		}		
		proxySessions.remove(session);
		HibernateSessionFactory.closeSession();
	}
	
	 public void wakeUp(String mac) throws Exception {
		  synchronized (proxySessions) {
			  Iterator iter = proxySessions.iterator();
			  while (iter.hasNext()) {
				  IoSession s = (IoSession) iter.next();
				  if (s.isConnected()) {
					  s.write(serverMessages.createProxyPkt(mac));
				  }
			  }
		  }
	  }
}
