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
	public static final String ACTIONSUCCESS = "Success";
	public static final String ACTIONFAILURE = "Failure ";
	/**
	 * recording user's operateLog to database
	 *  
	 * @param regUserId
	 * @param type Operatelog.LOGIN ADD MOD DEL ERROR
	 * @param information message
	 */
	protected void logOperate(int regUserId,Short type,String information){
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findById(regUserId);
		LOG.info("ReguserID: "+regUserId+" Information: " + information);
		if(regUser==null){
			LOG.error("ReguserID: "+regUserId+" not exist error! ");
		}
		else{
			OperatelogDAO operateLogDAO = new OperatelogDAO();
			Operatelog transientInstance = new Operatelog(regUser,type,new Date());
			transientInstance.setOperateInformation(information);
			operateLogDAO.save(transientInstance);
		}
	}

	/**
	 * get hibernate session transaction
	 * @return database transaction
	 */
	protected Transaction getTransaction()
	{
		return HibernateSessionFactory.getSession().beginTransaction();
	}
	/**
	 * get hibernate session
	 * @return database session
	 */
	protected org.hibernate.Session getSession()
	{
		return HibernateSessionFactory.getSession();
	}
	/**
	 * close hibernate session
	 */
	protected void closeSession()
	{
		HibernateSessionFactory.closeSession();
	}
	
}
