package com.webrender.protocol.messages;

import java.nio.ByteBuffer;

import com.webrender.protocol.enumn.EOPCODE;
import com.webrender.protocol.enumn.ESYSCODE;

public class ServerMessages {
	public static ByteBuffer createCommandPkt(String cmdString){
		byte[] bytes = new byte[1+4+cmdString.length()];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte) EOPCODE.COMMAND.ordinal());
		buffer.putInt(cmdString.length());
		buffer.put(cmdString.getBytes());
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createSystemPkt(ESYSCODE code){
		return null;
	}
	public static ByteBuffer createStatusPkt(){
		return null;
	}
	public static ByteBuffer createConnectFlag(int nodeId){
		return null;
	}
}
