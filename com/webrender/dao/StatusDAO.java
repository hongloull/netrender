package com.webrender.dao;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Status entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Status
 * @author MyEclipse Persistence Tools
 */

public class StatusDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(StatusDAO.class);
	// property constants
	public static final String TYPE = "type";
	public static final String VALUE = "value";

	public void save(Status transientInstance) {
		LOG.debug("saving Status instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Status persistentInstance) {
		LOG.debug("deleting Status instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Status findById(java.lang.Integer id) {
		LOG.debug("getting Status instance with id: " + id);
		try {
			Status instance = (Status) getSession().get(
					"com.webrender.dao.Status", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Status instance) {
		LOG.debug("finding Status instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Status").add(Example.create(instance))
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
		LOG.debug("finding Status instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Status as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByValue(Object value) {
		return findByProperty(VALUE, value);
	}

	public List findAll() {
		LOG.debug("finding all Status instances");
		try {
			String queryString = "from Status";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Status merge(Status detachedInstance) {
		LOG.debug("merging Status instance");
		try {
			Status result = (Status) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Status instance) {
		LOG.debug("attaching dirty Status instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Status instance) {
		LOG.debug("attaching clean Status instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	public void updateSystemVersion(){
		LOG.debug("updateSystemVersion");
		try {
			Status status = findById(100);
			if(status == null){
				status = new Status("System","0");
			}
			Long version = Long.parseLong( status.getValue() );
			LOG.info("UpdateVersion: "+version);
			version++;
			status.setValue( version.toString() );
			attachDirty(status);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
		//	LOG.error("attach failed", re);
			throw re;
		}
	}
}