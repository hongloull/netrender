package com.webrender.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Reguser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Reguser
 * @author MyEclipse Persistence Tools
 */

public class ReguserDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(ReguserDAO.class);
	// property constants
	public static final String REG_NAME = "regName";
	public static final String PASS_WORD = "passWord";

	public void save(Reguser transientInstance) {
		LOG.debug("saving Reguser instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Reguser persistentInstance) {
		LOG.debug("deleting Reguser instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Reguser findById(java.lang.Integer id) {
		LOG.debug("getting Reguser instance with id: " + id);
		try {
			Reguser instance = (Reguser) getSession().get(
					"com.webrender.dao.Reguser", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Reguser instance) {
		LOG.debug("finding Reguser instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Reguser").add(Example.create(instance))
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
		LOG.debug("finding Reguser instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Reguser as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public Reguser findByRegName(Object regName) {
		List lis = findByProperty(REG_NAME, regName);
		if( lis.size()==1 ) return (Reguser)lis.get(0);
		return null;
	}

	public List findByPassWord(Object passWord) {
		return findByProperty(PASS_WORD, passWord);
	}

	public List findAll() {
		LOG.debug("finding all Reguser instances");
		try {
			String queryString = "from Reguser";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Reguser merge(Reguser detachedInstance) {
		LOG.debug("merging Reguser instance");
		try {
			Reguser result = (Reguser) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Reguser instance) {
		LOG.debug("attaching dirty Reguser instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Reguser instance) {
		LOG.debug("attaching clean Reguser instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	
	public Set<Integer> getRightValue(Reguser instance){
		LOG.debug("getRightValue");
		Set<Integer> rightValue = new HashSet<Integer>();
		try{
			Iterator ite_Rights = instance.getRights().iterator();
			while( ite_Rights.hasNext() ){
				Right right = (Right) ite_Rights.next();
				rightValue.add(right.getRightId());
			}			
			LOG.debug("getRightValue success");
		}catch(Exception e){
			LOG.error("getRightValue fail",e);
		}
		return rightValue;
	}
}