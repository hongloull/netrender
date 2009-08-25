package com.webrender.protocol.handler;

import java.nio.ByteBuffer;

import com.webrender.protocol.processor.IClientProcessor;


public interface MessageHandler {
	public void parseClientPacket(ByteBuffer packet,IClientProcessor processor);
}
