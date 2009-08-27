package test.remote;

import org.junit.Test;

import com.webrender.dao.CommandDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class TestNodeMachine {
	
	public void testGetCommand()
	{
		CommandDAO commandDAO = new CommandDAO();
		
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine("172.16.20.127");
		nodeMachine.setConnect(false);
		System.out.println(nodeMachine.getCommand((commandDAO.findById(1))));
	}
	
	@Test
	public void testAddRealLog(){
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine("192.168.10.129");
		int commandId = 143;
		nodeMachine.addCommandId(commandId);
		for(int i = 0 ; i<5;i++){
			try {
				Thread.sleep(i*7000);
				nodeMachine.addRealLog(commandId,i+":realLog" );
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
//		nodeMachine.addRealLog(commandId, "***GOODBYE***");
	}
	
	@Test
	public void testGetStatus(){
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine("192.168.20.127");
		nodeMachine.testConnect();
		for(int i = 0 ; i<20;i++){
			System.out.println( nodeMachine.getStatus().toString() );	
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}
}
