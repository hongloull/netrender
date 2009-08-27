package com.webrender.protocol.messages;


import java.io.UnsupportedEncodingException;

import org.apache.mina.common.ByteBuffer;

import com.webrender.protocol.enumn.EOPCODE;


public class ClientMessages {
	public static ByteBuffer createRunPkt(int nodeId,String name) throws UnsupportedEncodingException{
		byte[] bytesName = name.getBytes("UTF8");
		byte[] bytes = new byte[1+4+4+bytesName.length];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte) EOPCODE.RUN.ordinal());
		buffer.putInt(nodeId);
		buffer.putInt(bytesName.length);
		buffer.put(bytesName);
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createReadyPkt(){
		byte[] bytes = new byte[1];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte)EOPCODE.READY.ordinal());
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createFeedbackPkt(int commandId ,String message){
		byte[] bytes = new byte[1+4+4+message.length()];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte) EOPCODE.FEEDBACK.ordinal());
		buffer.putInt(commandId);
		buffer.putInt(message.length());
		buffer.put(message.getBytes());
		buffer.flip();
		return buffer;
	}
	public static ByteBuffer createStatusInfoPkt(String status){
		byte[] bytes = new byte[1+4+status.length()];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.put((byte)EOPCODE.STATUSINFO.ordinal());
		buffer.putInt(status.length());
		buffer.put(status.getBytes());
		buffer.flip();
		return buffer;
	}
}
