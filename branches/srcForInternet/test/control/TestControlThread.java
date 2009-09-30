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
//			RealLogServer.getInstance().run();
			NodeLogServer.getInstance().run();
			Thread.sleep(10000);
			NodeLogServer.getInstance().close();
			Thread.sleep(10000);
			NodeLogServer.getInstance().run();
			Thread.sleep(10000);
			NodeLogServer.getInstance().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
