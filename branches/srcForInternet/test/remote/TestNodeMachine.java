package test.remote;

import org.junit.Test;

import com.webrender.dao.CommandDAO;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class TestNodeMachine {
	
	public void testGetCommand()
	{
		CommandDAO commandDAO = new CommandDAO();
		
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(2);
		nodeMachine.setConnect(false);
		System.out.println(nodeMachine.getCommand((commandDAO.findById(1))));
	}
	
	@Test
	public void testAddRealLog(){
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(3);
		int commandId = 143;
		nodeMachine.addCommandId(commandId);
		for(int i = 0 ; i<5;i++){
			try {
				Thread.sleep(i*7000);
				nodeMachine.addFeedBack(commandId,i+":realLog" );
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
//		nodeMachine.addRealLog(commandId, "***GOODBYE***");
	}
	
	@Test
	public void testGetStatus(){
		NodeMachine nodeMachine = NodeMachineManager.getNodeMachine(11);
//		nodeMachine.testConnect();
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
