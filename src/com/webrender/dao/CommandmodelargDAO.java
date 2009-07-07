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
 * Commandmodelarg entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.webrender.dao.Commandmodelarg
 * @author MyEclipse Persistence Tools
 */

public class CommandmodelargDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(CommandmodelargDAO.class);
	// property constants
	public static final String ARG_NAME = "argName";
	public static final String ARG_INSTRUCTION = "argInstruction";
	public static final String TYPE = "type";

	public void save(Commandmodelarg transientInstance) {
		log.debug("saving Commandmodelarg instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Commandmodelarg persistentInstance) {
		log.debug("deleting Commandmodelarg instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Commandmodelarg findById(java.lang.Integer id) {
		log.debug("getting Commandmodelarg instance with id: " + id);
		try {
			Commandmodelarg instance = (Commandmodelarg) getSession().get(
					"com.webrender.dao.Commandmodelarg", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Commandmodelarg instance) {
		log.debug("finding Commandmodelarg instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Commandmodelarg").add(
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
		log.debug("finding Commandmodelarg instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Commandmodelarg as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByArgName(Object argName) {
		return findByProperty(ARG_NAME, argName);
	}

	public List findByArgInstruction(Object argInstruction) {
		return findByProperty(ARG_INSTRUCTION, argInstruction);
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findAll() {
		log.debug("finding all Commandmodelarg instances");
		try {
			String queryString = "from Commandmodelarg";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Commandmodelarg merge(Commandmodelarg detachedInstance) {
		log.debug("merging Commandmodelarg instance");
		try {
			Commandmodelarg result = (Commandmodelarg) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Commandmodelarg instance) {
		log.debug("attaching dirty Commandmodelarg instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Commandmodelarg instance) {
		log.debug("attaching clean Commandmodelarg instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}