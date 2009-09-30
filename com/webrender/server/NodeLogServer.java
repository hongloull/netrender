package com.webrender.server;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

public final class NodeLogServer {
	private static NodeLogServer instance = new NodeLogServer();
    private static final int PORT = 10061;
    private static final Log LOG = LogFactory.getLog(NodeLogServer.class);
    private IoAcceptor acceptor = new SocketAcceptor();
//    private IoAcceptorConfig config = new SocketAcceptorConfig();
//    private  DefaultIoFilterChainBuilder chain = config.getFilterChain();
    private IoHandler ioHandler = new NodeLogServerHandler();
    private NodeLogServer()
    {
    }
    public static NodeLogServer getInstance(){
    	return instance;
    }
    
    public void run() throws Exception{
//    	chain.addLast("codec", new ProtocolCodecFilter(
//    			new TextLineCodecFactory()));
    	ByteBuffer.setUseDirectBuffers(false);/*设置内存获取方式*/
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
    	acceptor.bind(new InetSocketAddress(PORT), ioHandler);
    	LOG.info("NodeLogServer Listening on port " + PORT);
    }
    public void close(){
    	acceptor.unbindAll();
    	LOG.info("NodeLogServer  port " + PORT + " is closed");
    }

}
