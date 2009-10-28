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
	private static final Log LOG = LogFactory.getLog(CommandmodelargDAO.class);
	// property constants
	public static final String ARG_NAME = "argName";
	public static final String ARG_INSTRUCTION = "argInstruction";
	public static final String TYPE = "type";

	public void save(Commandmodelarg transientInstance) {
		LOG.debug("saving Commandmodelarg instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Commandmodelarg persistentInstance) {
		LOG.debug("deleting Commandmodelarg instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Commandmodelarg findById(java.lang.Integer id) {
		LOG.debug("getting Commandmodelarg instance with id: " + id);
		try {
			Commandmodelarg instance = (Commandmodelarg) getSession().get(
					"com.webrender.dao.Commandmodelarg", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Commandmodelarg instance) {
		LOG.debug("finding Commandmodelarg instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Commandmodelarg").add(
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
		LOG.debug("finding Commandmodelarg instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Commandmodelarg as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public Commandmodelarg findByArgName(Object argName,int commandModelId) throws Exception {
//		return findByProperty(, argName);
		try {
			String queryString = "from Commandmodelarg as model where model."
					+ ARG_NAME + "= ?" + " and commandmodel.commandModelId = "+ commandModelId;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, argName);
			List list = queryObject.list();
			if(list.size()==1) return (Commandmodelarg) list.get(0);
			else if(list.size()==0){
				return null;
			}
			else{
				LOG.error("finding more than one Commandmodelarg instance with argName: "
				+ argName +" commandModelId: "+commandModelId + ". Please check model error!");
				throw new java.lang.Exception("CommandmodelargError");
			}
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByArgInstruction(Object argInstruction) {
		return findByProperty(ARG_INSTRUCTION, argInstruction);
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findAll() {
		LOG.debug("finding all Commandmodelarg instances");
		try {
			String queryString = "from Commandmodelarg";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}
	
	public List findFinalArgs(Commandmodel commandModel) {
		LOG.debug("finding findFinalArgs instances commandmodelName:" +commandModel.getCommandModelName());
		try {
			String queryString = "from Commandmodelarg as arg where commandmodel.commandModelId="+commandModel.getCommandModelId()+" and status.statusId= 65 ";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find final args failed", re);
			throw re;
		}
	}
	public Commandmodelarg findStartArg(Commandmodel commandModel){
		LOG.debug("finding findStartArg instances commandmodelName:" +commandModel.getCommandModelName());
		try {
			String queryString = "from Commandmodelarg as arg where commandmodel.commandModelId="+commandModel.getCommandModelId()+" and status.statusId= 61 ";
			Query queryObject = getSession().createQuery(queryString);
			List list = queryObject.list();
			if ( list.size()==1){
				return (Commandmodelarg) list.get(0);
			}
			else{
				return null;
			}
			
		} catch (RuntimeException re) {
			LOG.error("find start arg failed", re);
			throw re;
		}
	}
	public Commandmodelarg findEndArg(Commandmodel commandModel){
		LOG.debug("finding findEndArg instances commandmodelName:" +commandModel.getCommandModelName());
		try {
			String queryString = "from Commandmodelarg as arg where commandmodel.commandModelId="+commandModel.getCommandModelId()+" and status.statusId= 62 ";
			Query queryObject = getSession().createQuery(queryString);
			List list = queryObject.list();
			if ( list.size()==1){
				return (Commandmodelarg) list.get(0);
			}
			else{
				return null;
			}
			
		} catch (RuntimeException re) {
			LOG.error("find end arg failed", re);
			throw re;
		}
	}
	public Commandmodelarg findByArg(Commandmodel commandModel){
		LOG.debug("finding findByArg instances commandmodelName:" +commandModel.getCommandModelName());
		try {
			String queryString = "from Commandmodelarg as arg where commandmodel.commandModelId="+commandModel.getCommandModelId()+" and status.statusId= 63 ";
			Query queryObject = getSession().createQuery(queryString);
			List list = queryObject.list();
			if ( list.size()==1){
				return (Commandmodelarg) list.get(0);
			}
			else{
				return null;
			}
			
		} catch (RuntimeException re) {
			LOG.error("find  byArg failed", re);
			throw re;
		}
	}
	public Commandmodelarg merge(Commandmodelarg detachedInstance) {
		LOG.debug("merging Commandmodelarg instance");
		try {
			Commandmodelarg result = (Commandmodelarg) getSession().merge(
					detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Commandmodelarg instance) {
		LOG.debug("attaching dirty Commandmodelarg instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Commandmodelarg instance) {
		LOG.debug("attaching clean Commandmodelarg instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
}