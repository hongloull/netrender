package com.webrender.test.mina;

import java.util.Date;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

public class TimeServerHandler extends IoHandlerAdapter {
    public void exceptionCaught(IoSession session, Throwable t) throws Exception {
        t.printStackTrace();
        session.close();
    }

    public void messageReceived(IoSession session, Object msg) throws Exception {
        String str = msg.toString();
        //如果是quit就关闭session退出
        if( str.trim().equalsIgnoreCase("quit") ) {
            session.close();
            return;
        }
        //否则打印当前日期
        Date date = new Date();
        session.write( date.toString()+session.getRemoteAddress().toString() );
        System.out.println("Message written");
    }

    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("Session created");

        if( session.getTransportType() == TransportType.SOCKET )
            ((SocketSessionConfig) session.getConfig() ).setReceiveBufferSize( 2048 );

        session.setIdleTime( IdleStatus.BOTH_IDLE, 10 );
    }
}