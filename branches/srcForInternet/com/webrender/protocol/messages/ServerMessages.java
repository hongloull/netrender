package com.webrender.protocol.messages;


import java.io.UnsupportedEncodingException;

import org.apache.mina.common.ByteBuffer;

import com.webrender.protocol.enumn.EOPCODE;
import com.webrender.protocol.enumn.ESYSCODE;

public class ServerMessages {
	public static ByteBuffer createCommandPkt(int commandId, String cmdString) throws UnsupportedEncodingException{
		byte[] bytesName = cmdString.getBytes("utf-8");
		byte[] bytes = new byte[1+4+4+bytesName.length];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte) EOPCODE.COMMAND.ordinal());
		buffer.putInt(commandId);
		buffer.putInt(bytesName.length);
		buffer.put(bytesName);
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createSystemPkt(ESYSCODE code){
		byte[] bytes = new byte[2];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte)EOPCODE.SYSTEM.ordinal());
		buffer.put((byte)code.ordinal());
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createStatusPkt(){
		byte[] bytes = new byte[1];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte)EOPCODE.STATUS.ordinal());
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createConnectFlag(int nodeId){
		byte[] bytes = new byte[1+4];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte)EOPCODE.CONNECTFLAG.ordinal());
		buffer.putInt(nodeId);
		buffer.flip();
		return buffer;
	}
}
