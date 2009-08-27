package test.axis;

import org.junit.Test;

import com.webrender.axis.UserLogin;

public class TestUserLogin {
	@Test
	public void testLoginValidate()
	{
		UserLogin ul = new UserLogin();
		System.out.println( ul.LoginValidate("admin", "111111") ); 
	}
}
