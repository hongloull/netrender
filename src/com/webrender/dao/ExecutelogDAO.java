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
	private static final Log log = LogFactory.getLog(ExecutelogDAO.class);
	// property constants
	public static final String NOTE = "note";

	public void save(Executelog transientInstance) {
		log.debug("saving Executelog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
			
			if(transientInstance.getStatus().getStatusId()>=90){
				//executeLog send to client;
				String xmlExecuteLog = ExecutelogUtils.bean2XMLString(transientInstance);
				ExecuteLogServer.getInstance().broadCast(xmlExecuteLog);				
			}
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Executelog persistentInstance) {
		log.debug("deleting Executelog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Executelog findById(java.lang.Integer id) {
		log.debug("getting Executelog instance with id: " + id);
		try {
			Executelog instance = (Executelog) getSession().get(
					"com.webrender.dao.Executelog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Executelog instance) {
		log.debug("finding Executelog instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Executelog").add(
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
		log.debug("finding Executelog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Executelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNote(Object note) {
		return findByProperty(NOTE, note);
	}

	public List findAll() {
		log.debug("finding all Executelog instances");
		try {
			String queryString = "from Executelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Executelog merge(Executelog detachedInstance) {
		log.debug("merging Executelog instance");
		try {
			Executelog result = (Executelog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Executelog instance) {
		log.debug("attaching dirty Executelog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Executelog instance) {
		log.debug("attaching clean Executelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	public List getRealLog(Command command){
		log.debug("getRealLog commandId: " +command.getCommandId());
		try{
			String queryString = "from Executelog as model where model.command.commandId= ? and model.status.statusId<90 ";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, command.getCommandId());
			return queryObject.list();
		}catch(RuntimeException re){
			log.error("getRealLog",re);
			throw re;
		}
	}
	public boolean hasError(Command command){
		log.debug("hasError commandId: "+command.getCommandId());
		try{
			String queryString = "from Executelog as model where model.command.commandId= ? and model.status.statusId=81 ";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, command.getCommandId());
			return queryObject.list().size()==0?false:true;
		}catch(RuntimeException re){
			log.error("getRealLog",re);
			throw re;
		}
	}
	
	public Executelog findStartLog(Command command) {
		try{
			Iterator  logs =  command.getExecutelogs().iterator();
			Executelog exeLog = null;
			while(logs.hasNext())
			{
				exeLog = (Executelog) logs.next();
				if (exeLog!=null && exeLog.getStatus().getStatusId()==90)
				{
					return exeLog;
				}
			}
			return null;
		}catch(Exception e){
			return null;			
		}
	}
	
	public Executelog findEndLog(Command command) {
		try{
			Iterator  logs = command.getExecutelogs().iterator();
			Executelog exeLog = null;
			while(logs.hasNext())
			{
				exeLog = (Executelog) logs.next();
				if (exeLog!=null && exeLog.getStatus().getStatusId()==91)
				{
					return exeLog;
				}
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	
}