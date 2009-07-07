package com.webrender.axis;

import java.util.Set;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;

public abstract class BaseAxis {
	
	protected static final String NotLogin = "NotLogin";
	protected static final String ActionSuccess = "Success";
	protected static final String ActionFailure = "Failure";	
	protected static final String RightError = "NoRight";
	/**
	 * 查看登录用户ID
	 * @return ReguserId 0为未登录
	 */
	protected int getLoginUserId()
	{
		try{
			MessageContext mc = MessageContext.getCurrentContext();
			Session session = mc.getSession();
			int regUserId = (Integer)session.get("RegUserId");
			return regUserId;	
		}catch(Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 
	 * @param rightId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean canVisit(int rightId)
	{		
		try{
			MessageContext mc = MessageContext.getCurrentContext();
			Session session = mc.getSession();
			Set<Integer> rightValue = (Set<Integer>)session.get("RightValue");
			return rightValue.contains(rightId)?true:false;
			
		}catch(Exception e){
			return false;
		}
	}
	
	protected boolean isSelf(int id){
		return false;
	}
	protected Transaction getTransaction()
	{
		return HibernateSessionFactory.getSession().beginTransaction();
	}
	protected void closeSession()
	{
		HibernateSessionFactory.closeSession();
	}
	protected void evict(Class obj)
	{
		HibernateSessionFactory.getSessionFactory().evict(obj);
	}
}
