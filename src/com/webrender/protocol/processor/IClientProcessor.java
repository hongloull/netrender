package com.webrender.protocol.processor;

public interface IClientProcessor {

	void run(int nodeId,String name);

	void ready();

	void addFeedBack(int commandId,String message);

	void updateStatus(String statusString);
	
}
