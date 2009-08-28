package com.webrender.protocol.handler;

import java.io.UnsupportedEncodingException;

import org.apache.mina.common.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.protocol.enumn.EOPCODE;
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
	
	public void parseClientPacket(EOPCODE code, ByteBuffer packet,
			IClientProcessor processor) {
		switch(code){
		case READY:
			processor.ready();
			break;
		case FEEDBACK:
			int commandId = packet.getInt();
			byte[] message = new byte[packet.getInt()];
			packet.get(message);
			String mesString = new String(message);
			processor.addFeedBack(commandId,mesString);
			break;
		case STATUSINFO:
			byte[] status = new byte[packet.getInt()];
			packet.get(status);
			String statusString = new String(status);
			processor.updateStatus(statusString);
			break;
		case CONFIGINFO:
			byte[] configInfo = new byte[packet.getInt()];
			packet.get(configInfo);
			String configString = new String(configInfo);
			processor.updateConfig(configString);
		default :
			break;
		}
		
	}
	private EOPCODE getOpCode(ByteBuffer packet) {
		byte opbyte = packet.get();
		if ((opbyte < 0) || (opbyte > EOPCODE.values().length - 1)) {
            this.LOG.error("Unknown op value: " + opbyte);
            return null;
        }
        EOPCODE code = EOPCODE.values()[opbyte];
        
        return code;
	}
	public int initialClient(ByteBuffer packet) {
		int nodeId = packet.getInt();
		byte[] name = new byte[ packet.getInt() ];
		packet.get(name);
		String mapName=null;
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
			Node node = nodeDAO.runNode(nodeId,"", mapName);
			tx.commit();
			int saveNodeId = node.getNodeId();
			return saveNodeId;
		}catch(Exception e){
			LOG.error("RunSaveNode fail nodeId:"+nodeId, e);
			return 0;
		}
	}

}
