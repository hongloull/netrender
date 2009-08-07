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
 * Nodegroup entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Nodegroup
 * @author MyEclipse Persistence Tools
 */

public class NodegroupDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(NodegroupDAO.class);
	// property constants
	public static final String NODE_GROUP_NAME = "nodeGroupName";

	public void save(Nodegroup transientInstance) {
		LOG.debug("saving Nodegroup instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Nodegroup persistentInstance) {
		LOG.debug("deleting Nodegroup instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Nodegroup findById(java.lang.Integer id) {
		LOG.debug("getting Nodegroup instance with id: " + id);
		try {
			Nodegroup instance = (Nodegroup) getSession().get(
					"com.webrender.dao.Nodegroup", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Nodegroup instance) {
		LOG.debug("finding Nodegroup instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Nodegroup")
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
		LOG.debug("finding Nodegroup instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Nodegroup as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public Nodegroup findByNodeGroupName(Object nodeGroupName) {
		List lis_NodeGroups = findByProperty(NODE_GROUP_NAME, nodeGroupName);
		if( lis_NodeGroups.size()==1) return (Nodegroup)lis_NodeGroups.get(0);
		return null;	
	}
	
	public List findAll() {
		LOG.debug("finding all Nodegroup instances");
		try {
			String queryString = "from Nodegroup";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	
	public Nodegroup merge(Nodegroup detachedInstance) {
		LOG.debug("merging Nodegroup instance");
		try {
			Nodegroup result = (Nodegroup) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Nodegroup instance) {
		LOG.debug("attaching dirty Nodegroup instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Nodegroup instance) {
		LOG.debug("attaching clean Nodegroup instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
}