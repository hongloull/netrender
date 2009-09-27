package com.webrender.protocol.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.mina.common.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.processor.IClientProcessor;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class MessageHandlerImpl implements MessageHandler {
	private static MessageHandler instance;
    protected MessageHandlerImpl() {Runtime.getRuntime().traceMethodCalls(true);}
    public static MessageHandler getInstance() {
        if(instance == null) {
            MessageHandlerImpl.instance = new MessageHandlerImpl();
        }
        return instance;
    }
    protected final Log LOG = LogFactory.getLog(MessageHandlerImpl.class);
	
	public ByteBuffer parseClientPacket(CODE code, ByteBuffer packet,
			IClientProcessor processor) {
		
		int initialPositon = packet.position();
		try {
			
			ArrayList<String> datas = new ArrayList<String>();
			
			int headLength = (int)packet.get();
			if(headLength==0 ){ // 无参数
				
				processor.execute(code, new byte[0], datas);
				return null;
			}
		// 有参数时
		

			byte[] head = new byte[headLength];
			packet.get(head);
			ByteBuffer headBuffer = ByteBuffer.wrap(head);
			ByteBuffer fmts = ByteBuffer.allocate(headLength);
			while(headBuffer.hasRemaining()){
				char fmt = (char)headBuffer.get();
				switch(fmt){
				case 'i':
				case 'I':
					fmts.put((byte)'i');
					datas.add( (new Integer(packet.getInt())).toString() ) ;
					break;
				case 's':
				case 'S':
					fmts.put((byte)'s');
					int length = headBuffer.getInt(); 
//					LOG.info("length:"+ length);
					byte[] bytes = new byte[length];
					packet.get(bytes);
//					LOG.info(new String(bytes));
//					LOG.info(new String(bytes,"GBK"));
//					LOG.info(new String(bytes,"UTF-16"));
					String data = new String(bytes);
					datas.add(data);
					
					break;
				}	
			}
			fmts.flip();
			byte[] byteFmts = new byte[fmts.limit()];
			fmts.get(byteFmts);				
			processor.execute(code, byteFmts, datas);
			return null;
		} catch (java.nio.BufferUnderflowException e) {
			
			packet.position( (initialPositon-1) );
			int remaining = packet.remaining();
			byte[] bytes = new byte[remaining];
			packet.get(bytes);
			ByteBuffer remainBuffer = ByteBuffer.allocate(remaining);
			remainBuffer.put(bytes);
			return remainBuffer;
		}
//		switch(code){
//		case READY:
//			processor.ready();
//			break;
//		case FEEDBACK:
//			int commandId = packet.getInt();
//			byte[] message = new byte[packet.getInt()];
//			packet.get(message);
//			String mesString = new String(message);
//			processor.addFeedBack(commandId,mesString);
//			break;
//		case STATUSINFO:
//			byte[] status = new byte[packet.getInt()];
//			packet.get(status);
//			String statusString = new String(status);
//			processor.updateStatus(statusString);
//			break;
//		case CONFIGINFO:
//			byte[] configInfo = new byte[packet.getInt()];
//			packet.get(configInfo);
//			String configString = new String(configInfo);
//			processor.updateConfig(configString);
//		default :
//			break;
//		}
	}
	
	public int initialClient(ByteBuffer packet) {
		
		int headLength = (int)packet.get();
		byte[] head = new byte[headLength];
		packet.get(head);
		ByteBuffer headBuffer = ByteBuffer.wrap(head);
		if ( (char)headBuffer.get()== 'i' && (char)headBuffer.get()== 's' ){
			int nameLength = headBuffer.getInt();
			if ( headBuffer.get()!='s'){
				LOG.error("initialClientPkt fmt error ");
				return 0;
			}
			int ipLength = headBuffer.getInt();
			int nodeId = packet.getInt();
			byte[] name = new byte[ nameLength ];
			packet.get(name);
			byte[] ip = new byte[ipLength];
			packet.get(ip);
			String mapName=null;
			String strIp = new String(ip);
			try {
				mapName = new String(name,"UTF8");
			} catch (UnsupportedEncodingException e){
				mapName = new String(name);
				e.printStackTrace();
			}
			NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(nodeId);
			if( nodeMachine != null && nodeMachine.isConnect()==true){
//				已经有nodeId节点连接到服务器上了。需要重新分配
				nodeId=0;
			}
			NodeDAO nodeDAO = new NodeDAO();
			Transaction tx = null;
			try{
				tx = HibernateSessionFactory.getSession().beginTransaction();
				LOG.info(nodeId+"------- "+strIp+"---------"+mapName);
				Node node = nodeDAO.runNode(nodeId,strIp, mapName);
				tx.commit();
				int saveNodeId = node.getNodeId();
				return saveNodeId;
			}catch(Exception e){
				LOG.error("RunSaveNode fail nodeId:"+nodeId, e);
				return 0;
			}
			
		}
		else{
			LOG.error("initialClientPkt fmt error ");
			return 0;
		}
		
		
		
	}

}
