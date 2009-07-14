package com.webrender.axis;


import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.axis.transport.http.HTTPConstants;

import com.webrender.dao.ReguserDAO;
import com.webrender.dao.Reguser;
import com.webrender.logic.LoginValidate;
import com.webrender.server.Conversion;

public class UserLogin extends BaseAxis {
	public String LoginValidate(String regName ,String passWord)
	{
		Reguser loginReguser = LoginValidate.Check(regName,passWord);
		if(loginReguser != null)
		{
			MessageContext mc = MessageContext.getCurrentContext();
			Session session = mc.getSession(); 
			session.set("RegUserId", loginReguser.getRegUserId());
			
			ReguserDAO regUserDAO = new ReguserDAO();
			 Set<Integer> rightValue = regUserDAO.getRightValue(loginReguser);
			session.set("RightValue",rightValue);
	  		HttpServletRequest request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);   
	        session.set( "RemoteIp",request.getRemoteAddr() );
			Iterator<Integer> ite_Rights = rightValue.iterator();
			StringBuilder result = new StringBuilder("[");
			while(ite_Rights.hasNext()){
				result.append( ite_Rights.next() );
				if (ite_Rights.hasNext()) result.append(",");
			}
			result.append("]");
			return result.toString();
		}
		else
		{
			return BaseAxis.ActionFailure;
		}
		
	}
	
	public int isRun()
	{
		return Conversion.getInstance().getStatus();
	}
}
 