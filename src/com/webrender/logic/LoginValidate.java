package com.webrender.logic;

import java.util.List;

import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;

public class LoginValidate {
	public static Reguser check(String regName,String passWord)
	{
		ReguserDAO regDAO = new ReguserDAO();
		Reguser reguser = regDAO.findByRegName(regName);
		
		if (reguser!= null && reguser.getPassWord().equals(passWord))
		{
			return reguser;
		}
		
		return null;
	}
}
