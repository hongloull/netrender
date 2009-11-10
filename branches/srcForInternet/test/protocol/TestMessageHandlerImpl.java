package test.protocol;

import org.apache.mina.common.ByteBuffer;
import org.junit.Test;

import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.handler.MessageHandlerImpl;
import com.webrender.protocol.messages.ServerMessages;
import com.webrender.remote.NodeMachine;

public class TestMessageHandlerImpl {
	@Test
	public void testParseMessage() throws Exception{
		CODE code = EOPCODES.getInstance().get("S_SYSTEM");
		ByteBuffer buffer = (new ServerMessages()).createStatusPkt();
		buffer.get();
		Short pri = 0;
		NodeMachine nodeMachine = new NodeMachine(50,pri);
		MessageHandlerImpl.getInstance().parseClientPacket(code, buffer, nodeMachine);
	}
}
