package com.webrender.dao;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for Right
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.webrender.dao.Right
 * @author MyEclipse Persistence Tools
 */

public class RightDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(RightDAO.class);
	// property constants
	public static final String INSTRUCTION = "instruction";

	public void save(Right transientInstance) {
		LOG.debug("saving Right instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Right persistentInstance) {
		LOG.debug("deleting Right instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Right findById(java.lang.Integer id) {
		LOG.debug("getting Right instance with id: " + id);
		try {
			Right instance = (Right) getSession().get(
					"com.webrender.dao.Right", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Right instance) {
		LOG.debug("finding Right instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Right").add(Example.create(instance))
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
		LOG.debug("finding Right instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Right as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByInstruction(Object instruction) {
		return findByProperty(INSTRUCTION, instruction);
	}

	public List findAll() {
		LOG.debug("finding all Right instances");
		try {
			String queryString = "from Right as o order by o.rightId asc";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Right merge(Right detachedInstance) {
		LOG.debug("merging Right instance");
		try {
			Right result = (Right) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Right instance) {
		LOG.debug("attaching dirty Right instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Right instance) {
		LOG.debug("attaching clean Right instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
}