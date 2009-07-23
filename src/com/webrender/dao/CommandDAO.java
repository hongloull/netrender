package com.webrender.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Command entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.webrender.dao.Command
 * @author MyEclipse Persistence Tools
 */

public class CommandDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(CommandDAO.class);
	// property constants
	public static final String COMMAND = "command";

	public void save(Command transientInstance) {
		log.debug("saving Command instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Command persistentInstance) {
		log.debug("deleting Command instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Command findById(java.lang.Integer id) {
		log.debug("getting Command instance with id: " + id);
		try {
			Command instance = (Command) getSession().get(
					"com.webrender.dao.Command", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Command instance) {
		log.debug("finding Command instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Command").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Command instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Command as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCommand(Object command) {
		return findByProperty(COMMAND, command);
	}

	public List findAll() {
		log.debug("finding all Command instances");
		try {
			String queryString = "from Command";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Command merge(Command detachedInstance) {
		log.debug("merging Command instance");
		try {
			Command result = (Command) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Command instance) {
		log.debug("attaching dirty Command instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Command instance) {
		log.debug("attaching clean Command instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 获取没完成的子命令
	 * @return 待完成的命令列表 但优先级排序
	 */
	public List getWaitingCommands() {
		log.debug("getWaitingCommands ");
		try
		{
			return getSession().createQuery("from Command as command where command.status.statusId=70 and command.quest.status.statusId=50 order by command.quest.pri desc , command.commandId asc").list();
			
		}catch(RuntimeException re){
			log.error("getWaitingCommands failed",re);
			throw re;
		}	
	}

//	public List getCurrentCommand(Node node) {
//		log.debug("getCurrentCommand ");
//		try
//		{
//			Query query = getSession().createQuery("from Command as command where command.status.statusId=71 and command.node.nodeIp=?  order by command.quest.pri desc");
//			query.setParameter(0,node.getNodeIp());
//			log.debug("attach successful");
//			return  query.list();
//			
//			
//		}catch(RuntimeException re){
//			log.error("getCurrentCommand failed",re);
//			throw re;
//		}	
//	}
	
	
	public void reinitCommand(Command instance) {
		log.debug("reinitCommand ");
		try
		{
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(70));
			this.attachDirty(instance);
			log.debug("reinitCommand successful");
			
		}catch(RuntimeException re){
			log.error("reinitCommand failed",re);
			throw re;
		}	
	}

	public List getInProgress(Quest quest) {
		log.debug("getInProgress ");
		try
		{
			Query query = getSession().createQuery("from Command as command where command.status.statusId=71 and command.quest.questId=? ");
			query.setParameter(0,quest.getQuestId());
			log.debug("getInProgress successful");
			return  query.list();
			
		}catch(RuntimeException re){
			log.error("getInProgress failed",re);
			throw re;
		}	
	}

	public List getFinish(Quest quest) {
		log.debug("getFinish");
		try
		{
			Query query = getSession().createQuery("from Command as command where command.status.statusId=72 and command.quest.questId=? order by command.sendTime desc");
			query.setParameter(0,quest.getQuestId());
			log.debug("getFinish successful");
			return  query.list();
			
			
		}catch(RuntimeException re){
			log.error("getFinish failed",re);
			throw re;
		}	
	}
	public boolean isInProgress(Quest quest){
		log.debug("isProgress");
		try{
			if( this.getInProgress(quest).size()>0) return true;
			else{
				Iterator ite_Finish=this.getFinish(quest).iterator();
				if(ite_Finish.hasNext()) 
				{
					Command temp = (Command) ite_Finish.next();
					long endTime = temp.getSendTime().getTime();
					long nowTime = (new Date()).getTime();
					double mins = (double)(nowTime-endTime)/1000/60;
					log.debug("finishCommand happened before "+mins+" mins" );
					if(mins<30)	return true;
					else return false;
				}
				else{
					return false;
				}
			}
		}catch(RuntimeException re){
			log.error("getFinish failed",re);
			throw re;
		}
	}
	
	public String getNote(Command command){
		Iterator<Commandarg> ite_Args = command.getCommandargs().iterator();
		StringBuffer note = new StringBuffer(command.getQuest().getQuestName()+":");
		while(ite_Args.hasNext()){
			Commandarg arg = ite_Args.next();
			note.append(" ").append(arg.getCommandmodelarg().getArgName()).append(" ").append(arg.getValue()).append(" ");
		}
		return note.toString();
	}

	
}