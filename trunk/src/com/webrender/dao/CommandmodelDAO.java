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
 * Commandmodel entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.webrender.dao.Commandmodel
 * @author MyEclipse Persistence Tools
 */

public class CommandmodelDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(CommandmodelDAO.class);
	// property constants
	public static final String COMMAND_MODEL_NAME = "commandModelName";
	public static final String DESCRIPTION = "description";

	public void save(Commandmodel transientInstance) {
		LOG.debug("saving Commandmodel instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Commandmodel persistentInstance) {
		LOG.debug("deleting Commandmodel instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Commandmodel findById(java.lang.Integer id) {
		LOG.debug("getting Commandmodel instance with id: " + id);
		try {
			Commandmodel instance = (Commandmodel) getSession().get(
					"com.webrender.dao.Commandmodel", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Commandmodel instance) {
		LOG.debug("finding Commandmodel instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Commandmodel").add(
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
		LOG.debug("finding Commandmodel instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Commandmodel as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public Commandmodel findByCommandModelName(Object commandModelName) {
		List lis =  findByProperty(COMMAND_MODEL_NAME, commandModelName);
		if( lis.size()==1 ) return (Commandmodel) lis.get(0);
		return null;
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findAll() {
		LOG.debug("finding all Commandmodel instances");
		try {
			String queryString = "from Commandmodel";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Commandmodel merge(Commandmodel detachedInstance) {
		LOG.debug("merging Commandmodel instance");
		try {
			Commandmodel result = (Commandmodel) getSession().merge(
					detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Commandmodel instance) {
		LOG.debug("attaching dirty Commandmodel instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Commandmodel instance) {
		LOG.debug("attaching clean Commandmodel instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
}