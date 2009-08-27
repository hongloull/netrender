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
 * Timegroup entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Timegroup
 * @author MyEclipse Persistence Tools
 */

public class TimegroupDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(TimegroupDAO.class);
	// property constants
	public static final String TIME_GROUP_NAME = "timeGroupName";
	public static final String TIME_VALUE = "timeValue";

	public void save(Timegroup transientInstance) {
		LOG.debug("saving Timegroup instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Timegroup persistentInstance) {
		LOG.debug("deleting Timegroup instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Timegroup findById(java.lang.Integer id) {
		LOG.debug("getting Timegroup instance with id: " + id);
		try {
			Timegroup instance = (Timegroup) getSession().get(
					"com.webrender.dao.Timegroup", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Timegroup instance) {
		LOG.debug("finding Timegroup instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Timegroup")
					.add(Example.create(instance)).list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		LOG.debug("finding Timegroup instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Timegroup as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public Timegroup findByTimeGroupName(Object timeGroupName) {
		List lis =  findByProperty(TIME_GROUP_NAME, timeGroupName);
		if(lis.size()==1) return (Timegroup)lis.get(0);
		return null;
	}

	public List findByTimeValue(Object timeValue) {
		return findByProperty(TIME_VALUE, timeValue);
	}

	public List findAll() {
		LOG.debug("finding all Timegroup instances");
		try {
			String queryString = "from Timegroup";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Timegroup merge(Timegroup detachedInstance) {
		LOG.debug("merging Timegroup instance");
		try {
			Timegroup result = (Timegroup) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Timegroup instance) {
		LOG.debug("attaching dirty Timegroup instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Timegroup instance) {
		LOG.debug("attaching clean Timegroup instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
}