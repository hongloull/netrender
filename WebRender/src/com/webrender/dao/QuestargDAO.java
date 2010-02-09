package com.webrender.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Questarg entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Questarg
 * @author MyEclipse Persistence Tools
 */

public class QuestargDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(QuestargDAO.class);
	// property constants
	public static final String VALUE = "value";
	
	public void save(Questarg transientInstance) {
		LOG.debug("saving Questarg instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}
	
	public void delete(Questarg persistentInstance) {
		LOG.debug("deleting Questarg instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}
	
	public Questarg findById(java.lang.Integer id) {
		LOG.debug("getting Questarg instance with id: " + id);
		try {
			Questarg instance = (Questarg) getSession().get(
					"com.webrender.dao.Questarg", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}
	
	public List findByExample(Questarg instance) {
		LOG.debug("finding Questarg instance by example");
		try {
			List results = getSession().createCriteria(
			"com.webrender.dao.Questarg").add(Example.create(instance))
			.list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		LOG.debug("finding Questarg instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Questarg as model where model."
				+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List findByValue(Object value) {
		return findByProperty(VALUE, value);
	}
	
	public List findAll() {
		LOG.debug("finding all Questarg instances");
		try {
			String queryString = "from Questarg";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}
	
	public Questarg merge(Questarg detachedInstance) {
		LOG.debug("merging Questarg instance");
		try {
			Questarg result = (Questarg) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}
	
	public void attachDirty(Questarg instance) {
		LOG.debug("attaching dirty Questarg instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	
	public void attachClean(Questarg instance) {
		LOG.debug("attaching clean Questarg instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	public List getConstantArgs(Quest quest)
	{
		LOG.debug("getConstantArgs questId:"+quest.getQuestId());
		try{
			// 获取某Quest中的常数变量
			Query query = getSession().createQuery("from Questarg as arg where arg.quest.questId = ? and arg.commandmodelarg.status.statusId in(60,64) order by arg.commandmodelarg.status.statusId asc");
			query.setParameter(0, quest.getQuestId());
			return query.list();
			
		} catch (RuntimeException re) {
			LOG.error("getConstantArgs failed questId:"+quest.getQuestId(), re);
			throw re;
		}
		
	}
	public Questarg getFramesValue(Quest quest)
	//TODO optimize 
	{
		LOG.debug("getStartFrame questId:"+quest.getQuestId());
		try{
			// 获取某Quest中StartFrame;
			Query query = getSession().createQuery("from Questarg as arg where arg.quest.questId = ? and arg.commandmodelarg.status.statusId = 61");
			query.setParameter(0, quest.getQuestId());
			List list = query.list();
			int size = list.size();
			if (size==1)
			{
				return  ( (Questarg)list.get(0) );
			}
			else if (size>1){
				LOG.error("QuestId:"+quest.getQuestId()+"has more than one frames value");
				return null;
			}else{
				return null;
			}
		} catch (RuntimeException re) {
			LOG.error("getStartFrame failed questId:"+quest.getQuestId(), re);
			throw re;
		}
	}
//	public String getEndFrame(Quest quest)
//	{
//		LOG.debug("getEndFrame questId:"+quest.getQuestId());
//		try{
//			// 获取某Quest中getEndFrame;
//			Query query = getSession().createQuery("from Questarg as arg where arg.quest.questId = ? and arg.commandmodelarg.status.statusId = 62 ");
//			query.setParameter(0, quest.getQuestId());
//			List list = query.list();
//			int size = list.size();
//			if (size==1)
//			{
//				return  ( (Questarg)list.get(0) ).getValue();
//			}
//			else
//			{
//				return null;
//			}
//		} catch (RuntimeException re) {
//			LOG.error("getEndFrame failed questId:"+quest.getQuestId(), re);
//			throw re;
//		}
//	}
	public Questarg getByFrame(Quest quest)
	{
		LOG.debug("getByFrame questId:"+quest.getQuestId());
		try{
			// 获取某Quest中getByFrame;
			Query query = getSession().createQuery("from Questarg as arg where arg.quest.questId = ? and arg.commandmodelarg.status.statusId = 63 ");
			query.setParameter(0, quest.getQuestId());
			List list = query.list();
			int size = list.size();
			if (size==1)
			{
				return  ( (Questarg)list.get(0) );
			}
			else
			{
				return null;
			}
		} catch (RuntimeException re) {
			LOG.error("getByFrame failed questId:"+quest.getQuestId(), re);
			throw re;
		}
	}

//	public String getFileName(Quest quest) {
//		//TODO optimize 
//		LOG.debug("getFileName questId:"+quest.getQuestId());
//		try{
//			// 获取某Quest中getByFrame;
//			Query query = getSession().createQuery("from Questarg as arg where arg.quest.questId = ? and arg.commandmodelarg.status.statusId = 64 ");
//			query.setParameter(0, quest.getQuestId());
//			List list = query.list();
//			int size = list.size();
//			if (size>=1)
//			{
//				String result = ( (Questarg)list.get(0) ).getValue();
//				if(result.length()>65){
//					result = result.substring(0,65)+"...";
//				}
//				return result;
//			}
//			else
//			{
//				return null;
//			}
//		} catch (RuntimeException re) {
//			LOG.error("getFileName failed questId:"+quest.getQuestId(), re);
//			throw re;
//		}
//	}
	
}