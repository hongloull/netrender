package test.axis;

import org.junit.Test;

import com.webrender.axis.UserOperate;

public class TestUserOperate {
	@Test
	public void testGetUsers()
	{
		UserOperate uo = new UserOperate();
		System.out.println( uo.getUsersList());
	}
	@Test
	public void testAddUser()
	{
		String regName   = "ymxu";
		String passWord  = "111111";
		String groupName = "user";
		UserOperate uo = new UserOperate();
		System.out.println( uo.addUser(regName,passWord) );
	}
	
	@Test
	public void testmodUser()
	{
		String regName   = "ymxu2";
		String modName   = "ymxu";
		String passWord  = "111111";
		String questXML = "<User maxInstance=\"2\"><Right id=\"1\"/><Right id=\"2\"/><Right id=\"3\"/><Right id=\"7\"/><Model name=\"Maya\"/><Group name=\"Group1\"/></User>";
		UserOperate uo = new UserOperate();
		System.out.println( uo.modUserConfig(modName, questXML));
	}
}
