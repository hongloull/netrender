package com.webrender.protocol.handler;

import org.apache.mina.common.ByteBuffer;

import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.processor.IClientProcessor;


public interface MessageHandler {
	public ByteBuffer parseClientPacket(CODE code,ByteBuffer packet,IClientProcessor processor);
	public int initialClient(ByteBuffer packet);
}
