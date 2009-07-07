package com.webrender.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Operatelog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Operatelog
 * @author MyEclipse Persistence Tools
 */

public class OperatelogDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(OperatelogDAO.class);
	// property constants
	public static final String OPERATE_INFORMATION = "operateInformation";
	public static final String TYPE = "type";
	public static final String TABLE = "table";
	public static final String TABLE_ID = "tableId";

	public void save(Operatelog transientInstance) {
		log.debug("saving Operatelog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Operatelog persistentInstance) {
		log.debug("deleting Operatelog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Operatelog findById(java.lang.Integer id) {
		log.debug("getting Operatelog instance with id: " + id);
		try {
			Operatelog instance = (Operatelog) getSession().get(
					"com.webrender.dao.Operatelog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Operatelog instance) {
		log.debug("finding Operatelog instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Operatelog").add(
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
		log.debug("finding Operatelog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Operatelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOperateInformation(Object operateInformation) {
		return findByProperty(OPERATE_INFORMATION, operateInformation);
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByTable(Object table) {
		return findByProperty(TABLE, table);
	}

	public List findByTableId(Object tableId) {
		return findByProperty(TABLE_ID, tableId);
	}

	public List findAll() {
		log.debug("finding all Operatelog instances");
		try {
			String queryString = "from Operatelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Operatelog merge(Operatelog detachedInstance) {
		log.debug("merging Operatelog instance");
		try {
			Operatelog result = (Operatelog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Operatelog instance) {
		log.debug("attaching dirty Operatelog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Operatelog instance) {
		log.debug("attaching clean Operatelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}