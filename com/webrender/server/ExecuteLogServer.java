package com.webrender.server;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoHandler;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

public final class ExecuteLogServer {
    private static ExecuteLogServer instance = new ExecuteLogServer();
    private static final int PORT = 10063;
    private static final Log LOG = LogFactory.getLog(ExecuteLogServer.class);
    private IoAcceptor acceptor = new SocketAcceptor();
    private IoAcceptorConfig config = new SocketAcceptorConfig();
    private  DefaultIoFilterChainBuilder chain = config.getFilterChain();
    private IoHandler ioHandler = new ExecuteLogServerHandler();
    private ExecuteLogServer()
    {
    }
    public static ExecuteLogServer getInstance(){
    	return instance;
    }
    
    public void run() throws Exception{
    	chain.addLast("codec", new ProtocolCodecFilter(
    			new TextLineCodecFactory()));
    	acceptor.bind(new InetSocketAddress(PORT), ioHandler,
    			config);
    	LOG.info("EventServer Listening on port " + PORT);
    }
    public void broadCast(String message)
    {
    	( (ExecuteLogServerHandler)ioHandler ).broadcast(message);
    }
}
