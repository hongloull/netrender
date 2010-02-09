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

public abstract class BaseAxis {
	private   static final Log LOG = LogFactory.getLog(BaseAxis.class);
	protected static final String NOTLOGIN = "Failure:Please log in first.";
	protected static final String ACTIONSUCCESS = "Success";
	protected static final String ACTIONFAILURE = "Failure ";	
	protected static final String RIGHTERROR = "Failure: NoRight";
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
			LOG.debug("getLoginUserId : " + regUserId);
			return regUserId;	
		}catch(Exception e)
		{
			LOG.debug("getLoginUserId error return 0 ");
			return 0;
		}
	}
	
	/**
	 * 未登录
	 * if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;
	 * 需要管理员权限
	 * if (  !this.canVisit(0) ) return BaseAxis.RIGHTERROR;
     * 修改job权限
	 * if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(commandId)) ) )
	 *		 return BaseAxis.RIGHTERROR;
	 * 需要20权限
     * if (  !this.canVisit(0) &&  !this.canVisit(20) ) return BaseAxis.RIGHTERROR;
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
			if(rightValue.contains(8)) return true;
			else{
				return rightValue.contains(rightId)?true:false;				
			}
			
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 
	 * @param regUserId
	 * @param type Operate.LOGIN ADD MOD DEL ERROR
	 * @param information
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
	
	protected boolean isSelf(int objectId){
		return false;
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
