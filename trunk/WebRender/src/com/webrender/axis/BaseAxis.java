package com.webrender.axis;

import java.util.Date;
import java.util.Set;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Operatelog;
import com.webrender.dao.OperatelogDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
/**
 * Axis Base Class
 * 
 * @author WAEN
 *
 */
public abstract class BaseAxis {
	private   static final Log LOG = LogFactory.getLog(BaseAxis.class);
	protected static final String NOTLOGIN = "Failure:Please log in first.";
	protected static final String ACTIONSUCCESS = "Success";
	protected static final String ACTIONFAILURE = "Failure ";	
	protected static final String RIGHTERROR = "Failure: NoRight";
	/**
	 * get login userId from http session 
	 * @return 0 : not log in
	 */
	protected int getLoginUserId()
	{
		try{
			MessageContext mc = MessageContext.getCurrentContext();
			Session session = mc.getSession();
			int regUserId = (Integer)session.get("RegUserId");
			LOG.debug("getLoginUserId : " + regUserId);
			return regUserId;	
		}catch(Exception e)
		{
			LOG.debug("getLoginUserId error return 0 ");
			return 0;
		}
	}
	
	/**
	 * Judge login user's rights
		
		<Right Id="0" detail="is a administrator" />
		<Right Id="10" detail="can submit jobs to the dispatcher engine" />
		<Right Id="11" detail="can manage jobs belongs to him" />
		<Right Id="12" detail="can manage jobs belongs to others" />
		<Right Id="13" detail="can arrange jobs changing their priority" />
		<Right Id="20" detail="can manager hosts in the hosts list" />
		<Right Id="21" detail="can remote control hosts in the hosts list" />
		<Right Id="30" detail="can modify the status of the engine" />
		<Right Id="40" detail="can login by web page" />
		
	 * @param rightId id's value
	 */
	protected boolean canVisit(int rightId)
	{
		try{
			MessageContext mc = MessageContext.getCurrentContext();
			Session session = mc.getSession();
			Set<Integer> rightValue = (Set<Integer>)session.get("RightValue");
			// is admin
			if(rightValue.contains(0)) return true; 
			else{
				return rightValue.contains(rightId)?true:false;				
			}
			
		}catch(Exception e){
			return false;
		}
	}
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
		}
		else{
			OperatelogDAO operateLogDAO = new OperatelogDAO();
			Operatelog transientInstance = new Operatelog(regUser,type,new Date());
			transientInstance.setOperateInformation(information);
			operateLogDAO.save(transientInstance);
		}
	}
	
	/**
	 * This implementation always throws an UnsupportedOperationException
	 */
	protected boolean isSelf(int objectId){
		throw new UnsupportedOperationException();
	}
	

	
}
