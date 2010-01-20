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

import com.webrender.config.GenericConfig;

public final class RealLogServer {
    private static RealLogServer instance = new RealLogServer();
    private int PORT = GenericConfig.getInstance().getRealLogPort();
    private static final Log LOG = LogFactory.getLog(RealLogServer.class);
    private IoAcceptor acceptor = new SocketAcceptor();
    private IoAcceptorConfig config = new SocketAcceptorConfig();
    private  DefaultIoFilterChainBuilder chain = config.getFilterChain();
    private IoHandler ioHandler = new RealLogServerHandler();
    private RealLogServer()
    {
    }
    public static synchronized RealLogServer getInstance(){
    	if(instance==null){
    		instance = new RealLogServer();
    	}
    	return instance;
    }
    
    public void run() throws Exception{
    	chain.addLast("codec", new ProtocolCodecFilter(
    			new TextLineCodecFactory()));
    	acceptor.bind(new InetSocketAddress(PORT), ioHandler,
    			config);
    	LOG.info("RealLogServer Listening on port " + PORT);
    }
    public void stop(){
    	acceptor.unbindAll();
    	instance = null;
    	LOG.info("RealLogServer  port " + PORT + " is closed");
    }
    public void broadCast(String message)
    {
    	( (RealLogServerHandler)ioHandler ).broadcast(message);
    }
}
