package test.protocol;

import org.apache.mina.common.ByteBuffer;
import org.junit.Test;
import com.webrender.protocol.enumn.EOPCODES;
import com.webrender.protocol.enumn.EOPCODES.CODE;
import com.webrender.protocol.messages.ServerMessages;

public class TestServerMessageCreate {
	@Test
	public void testCreatePkt() throws Exception
	{
		char[] fmt = {'c'};
		CODE code = EOPCODES.getInstance().get("S_SYSTEM").getSubCode("S_SHUTDOWN");
		char charCode = (char)code.getId();
		String[] data = {charCode+""};
		ByteBuffer pkt = ServerMessages.createPkt(EOPCODES.getInstance().get("S_SYSTEM"), fmt, data);
		System.out.println(pkt);
	}
	
	@Test
	public void testCreateCommandPkt() throws Exception{
		System.out.println( ServerMessages.createCommandPkt(5,"commandInformation") ); 
	}
}
