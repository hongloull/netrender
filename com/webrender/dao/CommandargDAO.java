package com.webrender.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Commandarg entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Commandarg
 * @author MyEclipse Persistence Tools
 */

public class CommandargDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(CommandargDAO.class);
	// property constants
	public static final String VALUE = "value";

	public void save(Commandarg transientInstance) {
		LOG.debug("saving Commandarg instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Commandarg persistentInstance) {
		LOG.debug("deleting Commandarg instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Commandarg findById(java.lang.Integer id) {
		LOG.debug("getting Commandarg instance with id: " + id);
		try {
			Commandarg instance = (Commandarg) getSession().get(
					"com.webrender.dao.Commandarg", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Commandarg instance) {
		LOG.debug("finding Commandarg instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Commandarg").add(
					Example.create(instance)).list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		LOG.debug("finding Commandarg instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Commandarg as model where model."
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
		LOG.debug("finding all Commandarg instances");
		try {
			String queryString = "from Commandarg";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Commandarg merge(Commandarg detachedInstance) {
		LOG.debug("merging Commandarg instance");
		try {
			Commandarg result = (Commandarg) getSession().merge(
					detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Commandarg instance) {
		LOG.debug("attaching dirty Commandarg instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Commandarg instance) {
		LOG.debug("attaching clean Commandarg instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
}