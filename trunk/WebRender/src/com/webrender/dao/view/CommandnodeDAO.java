package com.webrender.dao.view;

import com.webrender.dao.BaseHibernateDAO;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Commandnode entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.webrender.dao.view.Commandnode
 * @author MyEclipse Persistence Tools
 */

public class CommandnodeDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(CommandnodeDAO.class);

	// property constants

//	public void save(Commandnode transientInstance) {
//		log.debug("saving Commandnode instance");
//		try {
//			getSession().save(transientInstance);
//			log.debug("save successful");
//		} catch (RuntimeException re) {
//			log.error("save failed", re);
//			throw re;
//		}
//	}
//
//	public void delete(Commandnode persistentInstance) {
//		log.debug("deleting Commandnode instance");
//		try {
//			getSession().delete(persistentInstance);
//			log.debug("delete successful");
//		} catch (RuntimeException re) {
//			log.error("delete failed", re);
//			throw re;
//		}
//	}

	public Commandnode findById(com.webrender.dao.view.CommandnodeId id) {
		log.debug("getting Commandnode instance with id: " + id);
		try {
			Commandnode instance = (Commandnode) getSession().get(
					"com.webrender.dao.view.Commandnode", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Commandnode instance) {
		log.debug("finding Commandnode instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.view.Commandnode").add(
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
		log.debug("finding Commandnode instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Commandnode as model where model."
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
		log.debug("finding all Commandnode instances");
		try {
			String queryString = "from Commandnode";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	public List findByNodeId(Integer nodeId){
		return findByProperty("id.nodeId",nodeId);
	}

//	public Commandnode merge(Commandnode detachedInstance) {
//		log.debug("merging Commandnode instance");
//		try {
//			Commandnode result = (Commandnode) getSession().merge(
//					detachedInstance);
//			log.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			log.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public void attachDirty(Commandnode instance) {
//		log.debug("attaching dirty Commandnode instance");
//		try {
//			getSession().saveOrUpdate(instance);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
//
//	public void attachClean(Commandnode instance) {
//		log.debug("attaching clean Commandnode instance");
//		try {
//			getSession().lock(instance, LockMode.NONE);
//			log.debug("attach successful");
//		} catch (RuntimeException re) {
//			log.error("attach failed", re);
//			throw re;
//		}
//	}
}