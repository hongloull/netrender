package com.webrender.dao;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for Node
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.webrender.dao.Node
 * @author MyEclipse Persistence Tools
 */

public class NodeDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(NodeDAO.class);
	// property constants
	public static final String NODE_NAME = "nodeName";
	public static final String NODE_IP = "nodeIp";
	public static final String OS = "os";
	public static final String FILE_BASE = "fileBase";
	public static final String CORE_NUM = "coreNum";
	public static final String REAL_TIME = "realTime";

	public void save(Node transientInstance) {
		log.debug("saving Node instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Node persistentInstance) {
		log.debug("deleting Node instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Node findById(java.lang.Integer id) {
		log.debug("getting Node instance with id: " + id);
		try {
			Node instance = (Node) getSession().get("com.webrender.dao.Node",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Node instance) {
		log.debug("finding Node instance by example");
		try {
			List results = getSession()
					.createCriteria("com.webrender.dao.Node").add(
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
		log.debug("finding Node instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Node as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNodeName(Object nodeName) {
		return findByProperty(NODE_NAME, nodeName);
	}
	
	public Node findByNodeIp(Object nodeIp) {
		List lis_IPs =  findByProperty(NODE_IP, nodeIp);
		if ( lis_IPs.size()==1 ) return (Node)lis_IPs.get(0);
		else return null;
	}

	public List findByOs(Object os) {
		return findByProperty(OS, os);
	}

	public List findByFileBase(Object fileBase) {
		return findByProperty(FILE_BASE, fileBase);
	}

	public List findByCoreNum(Object coreNum) {
		return findByProperty(CORE_NUM, coreNum);
	}

	public List findByRealTime(Object realTime) {
		return findByProperty(REAL_TIME, realTime);
	}

	public List findAll() {
		log.debug("finding all Node instances");
		try {
			String queryString = "from Node";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Node merge(Node detachedInstance) {
		log.debug("merging Node instance");
		try {
			Node result = (Node) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Node instance) {
		log.debug("attaching dirty Node instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Node instance) {
		log.debug("attaching clean Node instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
//	public List getIdleNodes(Command instance)
//	{
//		log.debug("getIdleNodes for instance");
//		try
//		{
//			return getSession().createQuery("from Node as node where node.status.statusId=41").list();
//		}catch(RuntimeException re){
//			log.error("getIdleNodes failed",re);
//			throw re;
//		}
//	}

//	public void pauseNode(Node node) {
//		log.debug("pauseNode");
//		try {
//			StatusDAO statusDAO = new StatusDAO();
//			node.setStatus(statusDAO.findById(43));
//			this.attachDirty(node);
//			log.debug("pauseNode successful");
//		} catch (RuntimeException re) {
//			log.error("pauseNode failed", re);
//			throw re;
//		}
//	}
//	public void resumeNode(Node node)
//	{
//		log.debug("resumeNode");
//		try {
//			StatusDAO statusDAO = new StatusDAO();
//			node.setStatus(statusDAO.findById(41));
//			this.attachDirty(node);
//			log.debug("resumeNode successful");
//		} catch (RuntimeException re) {
//			log.error("resumeNode failed", re);
//			throw re;
//		}
//	}
//	
//	
}