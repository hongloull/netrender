package com.webrender.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.webrender.axis.beanxml.ExecutelogUtils;
import com.webrender.server.ExecuteLogServer;

/**
 * A data access object (DAO) providing persistence and search support for
 * Executelog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Executelog
 * @author MyEclipse Persistence Tools
 */

public class ExecutelogDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(ExecutelogDAO.class);
	// property constants
	public static final String NOTE = "note";

	public void save(Executelog transientInstance) {
		LOG.debug("saving Executelog instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
			
			if(transientInstance.getStatus().getStatusId()>=90){
				//executeLog send to client;
				String xmlExecuteLog = (new ExecutelogUtils()).bean2XMLString(transientInstance);
				ExecuteLogServer.getInstance().broadCast(xmlExecuteLog);				
			}
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Executelog persistentInstance) {
		LOG.debug("deleting Executelog instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Executelog findById(java.lang.Integer id) {
		LOG.debug("getting Executelog instance with id: " + id);
		try {
			Executelog instance = (Executelog) getSession().get(
					"com.webrender.dao.Executelog", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Executelog instance) {
		LOG.debug("finding Executelog instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Executelog").add(
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
		LOG.debug("finding Executelog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Executelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNote(Object note) {
		return findByProperty(NOTE, note);
	}

	public List findAll() {
		LOG.debug("finding all Executelog instances");
		try {
			String queryString = "from Executelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Executelog merge(Executelog detachedInstance) {
		LOG.debug("merging Executelog instance");
		try {
			Executelog result = (Executelog) getSession().merge(
					detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Executelog instance) {
		LOG.debug("attaching dirty Executelog instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Executelog instance) {
		LOG.debug("attaching clean Executelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	public Executelog getRealLog(Command command){
		LOG.debug("getRealLog commandId: " +command.getCommandId());
		try{
			String queryString = "from Executelog as model where model.command.commandId= ? and model.status.statusId<90 order by executeLogId desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, command.getCommandId());
			List list = queryObject.list();
			if (list.size() > 0 ){
				return (Executelog)list.get(0);
			}
			else{
				return null;
			}
		}catch(RuntimeException re){
			LOG.error("getRealLog fail",re);
			throw re;
		}
	}
	public boolean hasError(Command command){
		return this.getRealLog(command).getStatus().getStatusId()==80? false:true;
	}
	
	public Executelog findStartLog(Command command) {
		LOG.debug("findStartLog commandId:"+ command.getCommandId());
		try{
			Iterator  logs =  command.getExecutelogs().iterator();
			Executelog exeLog = null;
			while(logs.hasNext())
			{
				exeLog = (Executelog) logs.next();
				if (exeLog!=null && exeLog.getStatus().getStatusId()==90)
				{
					LOG.debug("findStartLog success commandId:"+ command.getCommandId());
					return exeLog;
				}
			}
			LOG.debug("not find StartLog commandId:"+ command.getCommandId());
			return null;
		}catch(Exception e){
			LOG.error("find StartLog fail commandId:"+command.getCommandId());
			return null;
		}
	}
	
	public Executelog findEndLog(Command command) {
		LOG.debug("findEndLog commandId:"+ command.getCommandId());
		try{
			Iterator  logs = command.getExecutelogs().iterator();
			Executelog exeLog = null;
			while(logs.hasNext())
			{
				exeLog = (Executelog) logs.next();
				if (exeLog!=null && exeLog.getStatus().getStatusId()==91)
				{
					LOG.debug("findStartLog success commandId:"+ command.getCommandId());
					return exeLog;
				}
			}
			LOG.debug("not find EndLog commandId:"+ command.getCommandId());
			return null;
		}catch(Exception e){
			LOG.error("find EndLog fail commandId:"+command.getCommandId());
			return null;
		}
	}

}