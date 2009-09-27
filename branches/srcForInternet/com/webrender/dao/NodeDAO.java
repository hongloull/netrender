package com.webrender.dao;

import java.util.List;
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
	private static final Log LOG = LogFactory.getLog(NodeDAO.class);
	// property constants
	public static final String NODE_NAME = "nodeName";
	public static final String NODE_IP = "nodeIp";
	public static final String OS = "os";
	public static final String FILE_BASE = "fileBase";
	public static final String CORE_NUM = "coreNum";
	public static final String REAL_TIME = "realTime";

	public void save(Node transientInstance) {
		LOG.debug("saving Node instance");
		try {
			getSession().save(transientInstance);
			NodegroupDAO nodeGroupDAO = new  NodegroupDAO();
			Nodegroup all = nodeGroupDAO.findByNodeGroupName("All");
			if(all!=null){
				all.getNodes().add(transientInstance);
				nodeGroupDAO.save(all);
			}
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Node persistentInstance) {
		LOG.debug("deleting Node instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Node findById(java.lang.Integer id) {
		LOG.debug("getting Node instance with id: " + id);
		try {
			Node instance = (Node) getSession().get("com.webrender.dao.Node",
					id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Node instance) {
		LOG.debug("finding Node instance by example");
		try {
			List results = getSession()
					.createCriteria("com.webrender.dao.Node").add(
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
		LOG.debug("finding Node instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Node as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNodeName(Object nodeName) {
		return findByProperty(NODE_NAME, nodeName);
	}
	
//	public Node findByNodeIp(Object nodeIp) {
//		List lis_IPs =  findByProperty(NODE_IP, nodeIp);
//		if ( lis_IPs.size()==1 ) return (Node)lis_IPs.get(0);
//		else return null;
//	}

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
		LOG.debug("finding all Node instances");
		try {
			String queryString = "from Node";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Node merge(Node detachedInstance) {
		LOG.debug("merging Node instance");
		try {
			Node result = (Node) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Node instance) {
		LOG.debug("attaching dirty Node instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Node instance) {
		LOG.debug("attaching clean Node instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	
	public Node runNode(int nodeId,String ip,String name){
		LOG.debug("RunSaveNode nodeId:"+nodeId + " nodeName:"+name+" ip:"+ip);
		try{
			Node node = findById(nodeId);
			if(node==null){
				node = new Node();
			}
			if(name!=null && !("".equals(name)) ) node.setNodeName(name);
			if(ip!=null && !("".equals(ip)) ) node.setNodeIp(ip);
			save(node);
			return node;
		}catch (RuntimeException re) {
			LOG.error("RunSaveNode failed", re);
			throw re;
		}
	}
//	public List getIdleNodes(Command instance)
//	{
//		LOG.debug("getIdleNodes for instance");
//		try
//		{
//			return getSession().createQuery("from Node as node where node.status.statusId=41").list();
//		}catch(RuntimeException re){
//			LOG.error("getIdleNodes failed",re);
//			throw re;
//		}
//	}

//	public void pauseNode(Node node) {
//		LOG.debug("pauseNode");
//		try {
//			StatusDAO statusDAO = new StatusDAO();
//			node.setStatus(statusDAO.findById(43));
//			this.attachDirty(node);
//			LOG.debug("pauseNode successful");
//		} catch (RuntimeException re) {
//			LOG.error("pauseNode failed", re);
//			throw re;
//		}
//	}
//	public void resumeNode(Node node)
//	{
//		LOG.debug("resumeNode");
//		try {
//			StatusDAO statusDAO = new StatusDAO();
//			node.setStatus(statusDAO.findById(41));
//			this.attachDirty(node);
//			LOG.debug("resumeNode successful");
//		} catch (RuntimeException re) {
//			LOG.error("resumeNode failed", re);
//			throw re;
//		}
//	}
//	
//	
}