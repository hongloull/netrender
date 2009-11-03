package test.axis;

import org.junit.Test;

import com.webrender.axis.NodesState;
import com.webrender.remote.NodeMachine;
import com.webrender.remote.NodeMachineManager;

public class TestNodeState {
	
	@Test
	public void testGetNodeStatus()
	{
		NodeMachine node = NodeMachineManager.getInstance().getNodeMachine(15);
//		node.testConnect();
		NodesState ns = new NodesState();
		String result = ns.getNodeStatus("15");
		System.out.println(result);
//		Math.random();
	}
	
	@Test
	public void testGetNodesStatus()
	{
		NodesState ns = new NodesState();
		String result = ns.getNodesStatus("All");
		System.out.println(result);
	}
}
