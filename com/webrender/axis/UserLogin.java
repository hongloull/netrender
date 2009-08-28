package com.webrender.axis;


import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.axis.transport.http.HTTPConstants;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Operatelog;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.Reguser;
import com.webrender.logic.LoginValidate;
import com.webrender.server.Conversion;

public class UserLogin extends BaseAxis {
	public String LoginValidate(String regName ,String passWord)
	{
		try{
			Reguser loginReguser = LoginValidate.check(regName,passWord);
			if(loginReguser != null)
			{
				MessageContext mc = MessageContext.getCurrentContext();
				Session session = mc.getSession(); 
				int regUserId = loginReguser.getRegUserId();
				session.set("RegUserId", regUserId);
				
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
				Transaction tx = null;
				try{
					tx = getTransaction();
					logOperate(regUserId,Operatelog.LOGIN,"login");
					tx.commit();
				}catch(Exception e){
					if(tx!=null){
						tx.rollback();
					}
					e.printStackTrace();
				}
				return result.toString();
			}
			else
			{
				return BaseAxis.ACTIONFAILURE;
			}
			
		}catch(Exception e)
		{
			return BaseAxis.ACTIONFAILURE;
		}finally{
			closeSession();
		}
		
	}
	public String getStatistics(){
		return "getSessionCloseCount(): "+HibernateSessionFactory.getSessionFactory().getStatistics().getSessionCloseCount()+"  getSessionOpenCount(): "+ HibernateSessionFactory.getSessionFactory().getStatistics().getSessionOpenCount();
	}
	public int isRun()
	{
		return Conversion.getInstance().getStatus().ordinal();
	}
}
 