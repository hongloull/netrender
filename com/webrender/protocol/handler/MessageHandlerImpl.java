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

public class MessageHandlerImpl implements MessageHandler {
	private static MessageHandler instance;
    protected MessageHandlerImpl() {}
    public static MessageHandler getInstance() {
        if(instance == null) {
            MessageHandlerImpl.instance = new MessageHandlerImpl();
        }
        return instance;
    }
    protected final Log LOG = LogFactory.getLog(MessageHandlerImpl.class);
	
	public void parseClientPacket(CODE code, ByteBuffer packet,
			IClientProcessor processor) {
		ArrayList<String> datas = new ArrayList<String>();
		if(packet.hasRemaining()==false){ // 无参数
			
			processor.execute(code, new byte[0], datas);
			return ;
		}
		// 有参数时
		int headLength = (int)packet.get();
		
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
				byte[] bytes = new byte[length];
				packet.get(bytes);
				try {
						String data = new String(bytes,"UTF-8");
						datas.add(data);
					} catch (UnsupportedEncodingException e) {
						LOG.error("UTF-8 decode fail",e);
					}
				break;
			}	
		}
		fmts.flip();
		byte[] byteFmts = new byte[fmts.limit()];
		fmts.get(byteFmts);				
		processor.execute(code, byteFmts, datas);
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
