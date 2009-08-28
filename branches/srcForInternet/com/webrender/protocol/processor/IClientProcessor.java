package com.webrender.protocol.processor;

public interface IClientProcessor {

	void ready();

	void addFeedBack(int commandId,String message);

	void updateStatus(String statusString);

	void updateConfig(String configString);
	
}
