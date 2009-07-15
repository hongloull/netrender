package test.axis;

import org.junit.Test;

import com.webrender.axis.UserOperate;

public class TestUserOperate {
//	@Test
	public void testAddUser()
	{
		String regName   = "ymxu";
		String passWord  = "111111";
		String groupName = "user";
		UserOperate uo = new UserOperate();
		System.out.println( uo.addUser(regName, passWord) );
	}
//	@Test
	public void testDelUser()
	{
		String regName   = "ymxu";
		String passWord  = "111111";
		String groupName = "user";
		UserOperate uo = new UserOperate();
		System.out.println( uo.delUser(regName) );
	}
	
	@Test
	public void testmodUser()
	{
		String regName   = "ymxu2";
		String modName   = "ymxu";
		String passWord  = "111111";
		String groupName = "user";
		UserOperate uo = new UserOperate();
		System.out.println( uo.modUser(regName, modName, passWord) );
	}
}
