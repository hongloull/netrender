package test.control;

import org.junit.Test;

import com.webrender.server.NodeLogServer;
import com.webrender.server.RealLogServer;
public class TestControlThread {

	@Test
	public  void run() {
	//	ControlThread.getInstance().run();
	//	NodeThread.getInstance().run();
	//	StatusReceiver.getInstance().run();
		try {
			RealLogServer.getInstance().run();
			NodeLogServer.getInstance().run();
			int i = 0; 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
