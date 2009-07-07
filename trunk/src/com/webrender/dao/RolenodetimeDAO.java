package com.webrender.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Rolenodetime entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.webrender.dao.Rolenodetime
 * @author MyEclipse Persistence Tools
 */

public class RolenodetimeDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(RolenodetimeDAO.class);

	// property constants

	public void save(Rolenodetime transientInstance) {
		log.debug("saving Rolenodetime instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Rolenodetime persistentInstance) {
		log.debug("deleting Rolenodetime instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Rolenodetime findById(java.lang.Integer id) {
		log.debug("getting Rolenodetime instance with id: " + id);
		try {
			Rolenodetime instance = (Rolenodetime) getSession().get(
					"com.webrender.dao.Rolenodetime", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Rolenodetime instance) {
		log.debug("finding Rolenodetime instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Rolenodetime").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Rolenodetime instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Rolenodetime as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all Rolenodetime instances");
		try {
			String queryString = "from Rolenodetime";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Rolenodetime merge(Rolenodetime detachedInstance) {
		log.debug("merging Rolenodetime instance");
		try {
			Rolenodetime result = (Rolenodetime) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Rolenodetime instance) {
		log.debug("attaching dirty Rolenodetime instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Rolenodetime instance) {
		log.debug("attaching clean Rolenodetime instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}