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

public class RealLogServer {
    private static RealLogServer instance = new RealLogServer();
    private static final int PORT = 10062;
    private static final Log log = LogFactory.getLog(RealLogServer.class);
    private IoAcceptor acceptor = new SocketAcceptor();
    private IoAcceptorConfig config = new SocketAcceptorConfig();
    private  DefaultIoFilterChainBuilder chain = config.getFilterChain();
    private IoHandler ioHandler = new RealLogServerHandler();
    private RealLogServer()
    {
    }
    public static RealLogServer getInstance(){
    	return instance;
    }
    
    public void run() throws Exception{
    	chain.addLast("codec", new ProtocolCodecFilter(
    			new TextLineCodecFactory()));
    	acceptor.bind(new InetSocketAddress(PORT), ioHandler,
    			config);
    	log.debug("RealLogServer Listening on port " + PORT);
    }
    public void broadCast(String message)
    {
    	( (RealLogServerHandler)ioHandler ).broadcast(message);
    }
}