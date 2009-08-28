package com.webrender.protocol.handler;

import org.apache.mina.common.ByteBuffer;

import com.webrender.protocol.enumn.EOPCODE;
import com.webrender.protocol.processor.IClientProcessor;


public interface MessageHandler {
	public void parseClientPacket(EOPCODE code,ByteBuffer packet,IClientProcessor processor);
	public int initialClient(ByteBuffer packet);
}
