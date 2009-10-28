package com.webrender.axis.operate;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Operatelog;
import com.webrender.dao.OperatelogDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;


public class BaseOperate {
	private   static final Log LOG = LogFactory.getLog(BaseOperate.class);
	protected static final String ACTIONSUCCESS = "Success";
	protected static final String ACTIONFAILURE = "Failure ";
	
	protected void logOperate(int regUserId,Short type,String information){
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findById(regUserId);
		LOG.info("ReguserID: "+regUserId+" Information: " + information);
		if(regUser==null){
		}
		else{
			OperatelogDAO operateLogDAO = new OperatelogDAO();
			Operatelog transientInstance = new Operatelog(regUser,type,new Date());
			transientInstance.setOperateInformation(information);
			operateLogDAO.save(transientInstance);
		}
	}
	protected Transaction getTransaction()
	{
		return HibernateSessionFactory.getSession().beginTransaction();
	}
	protected org.hibernate.Session getSession()
	{
		return HibernateSessionFactory.getSession();
	}
	protected void closeSession()
	{
		HibernateSessionFactory.closeSession();
	}
	
}
