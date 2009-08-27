package com.webrender.server;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.hibernate.Transaction;

import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.remote.ConnectTest;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class NodeLogServerHandler extends IoHandlerAdapter {
	private static final Log LOG = LogFactory.getLog(NodeLogServerHandler.class);
	public void exceptionCaught(IoSession session, Throwable cause) {
		  // Close connection when unexpected exception is caught.
		  session.close();
	  }
	
	public void sessionOpened(IoSession session) throws Exception{
		//  System.out.println("New Session" );
	  }
	
	public void messageReceived(IoSession session, Object message) {
		String ip = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
		
		String theMessage = (String) message;
		LOG.debug(ip+": "+theMessage);
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(ip);
		
		if(theMessage.startsWith("***RUN***")){
			LOG.debug(ip+theMessage);
			NodeDAO nodeDAO  = new NodeDAO();
			Transaction tx = null;
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				Node node = nodeDAO.findByNodeIp(ip);
				if( node ==null ){
					node = new Node();
					node.setNodeIp(ip);
				}
				node.setNodeName(theMessage.substring(9));//***RUN***之后的字符
				nodeDAO.save(node);
				tx.commit();
				LOG.debug(ip+" RUN OK");
				//	NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(ip);
				
				session.write("***OK***");
				nodeMachine.testConnect();
			}
			catch(Exception e)
			{
				LOG.error(ip+" run fail ",e );
				if (tx != null) 
				{
					tx.rollback();
				}
			}finally{
				HibernateSessionFactory.closeSession();
			}
			return;
		}
		try{
			Iterator iteCommands = nodeMachine.getCurrentCommands().iterator();
			if(iteCommands.hasNext()){
				nodeMachine.addRealLog(nodeMachine.getCurrentCommands().iterator().next(),theMessage);
				if (  nodeMachine.isRealTime() )
				{
					RealLogServer.getInstance().broadCast(ip+"***"+theMessage);
				}
				
			}
			else{
				LOG.debug(theMessage);
			}
			
		
		}
		catch(Exception e){
			LOG.error("messageReceived fail",e);
		}finally{
			HibernateSessionFactory.closeSession();
		}
		
		
	}
	
	public void sessionClosed(IoSession session) throws Exception {
		HibernateSessionFactory.closeSession();
	}
}
