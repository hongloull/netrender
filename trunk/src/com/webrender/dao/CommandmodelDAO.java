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
	private static final Log log = LogFactory.getLog(CommandmodelDAO.class);
	// property constants
	public static final String COMMAND_MODEL_NAME = "commandModelName";
	public static final String DESCRIPTION = "description";

	public void save(Commandmodel transientInstance) {
		log.debug("saving Commandmodel instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Commandmodel persistentInstance) {
		log.debug("deleting Commandmodel instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Commandmodel findById(java.lang.Integer id) {
		log.debug("getting Commandmodel instance with id: " + id);
		try {
			Commandmodel instance = (Commandmodel) getSession().get(
					"com.webrender.dao.Commandmodel", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Commandmodel instance) {
		log.debug("finding Commandmodel instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Commandmodel").add(
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
		log.debug("finding Commandmodel instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Commandmodel as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
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
		log.debug("finding all Commandmodel instances");
		try {
			String queryString = "from Commandmodel";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Commandmodel merge(Commandmodel detachedInstance) {
		log.debug("merging Commandmodel instance");
		try {
			Commandmodel result = (Commandmodel) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Commandmodel instance) {
		log.debug("attaching dirty Commandmodel instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Commandmodel instance) {
		log.debug("attaching clean Commandmodel instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}