package com.webrender.test.mina.client;

public class Message {
	private String msgBody ="";
	public Message(int i, int j, String string) {
		this.msgBody = string;
	}

	public String getMsgBody() {
		
		return this.msgBody;
	}

}
