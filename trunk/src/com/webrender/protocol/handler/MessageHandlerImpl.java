package com.webrender.protocol.handler;

import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	public void parseClientPacket(ByteBuffer packet, IClientProcessor processor) {
		EOPCODE code = this.getOpCode(packet);
		this.parseClientPacket(code,packet, processor);
	}
	private void parseClientPacket(EOPCODE code, ByteBuffer packet,
			IClientProcessor processor) {
		switch(code){
		case RUN:
			int nodeId = packet.getInt();
			byte[] name = new byte[ packet.getInt() ];
			packet.get(name);
			String mapName = new String(name);
			processor.run(nodeId,mapName);
			break;
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

}
