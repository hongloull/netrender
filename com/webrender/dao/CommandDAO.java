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
	private static final Log LOG = LogFactory.getLog(CommandDAO.class);
	// property constants
	public static final String COMMAND = "command";

	public void save(Command transientInstance) {
		LOG.debug("saving Command instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Command persistentInstance) {
		LOG.debug("deleting Command instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Command findById(java.lang.Integer id) {
		LOG.debug("getting Command instance with id: " + id);
		try {
			Command instance = (Command) getSession().get(
					"com.webrender.dao.Command", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Command instance) {
		LOG.debug("finding Command instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Command").add(Example.create(instance))
					.list();
			LOG.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			LOG.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		LOG.debug("finding Command instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Command as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		LOG.debug("finding all Command instances");
		try {
			String queryString = "from Command";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Command merge(Command detachedInstance) {
		LOG.debug("merging Command instance");
		try {
			Command result = (Command) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Command instance) {
		LOG.debug("attaching dirty Command instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Command instance) {
		LOG.debug("attaching clean Command instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 获取没完成的子命令
	 * @return 待完成的命令列表 但优先级排序
	 */
	public List getWaitingCommands() {
		LOG.debug("getWaitingCommands ");
		try
		{
			return getSession().createQuery("from Command as command where command.status.statusId =70 and command.quest.status.statusId=50 order by command.quest.pri desc , command.commandId asc").list();
			
		}catch(RuntimeException re){
			LOG.error("getWaitingCommands failed",re);
			throw re;
		}	
	}

//	public List getCurrentCommand(Node node) {
//		LOG.debug("getCurrentCommand ");
//		try
//		{
//			Query query = getSession().createQuery("from Command as command where command.status.statusId=71 and command.node.nodeIp=?  order by command.quest.pri desc");
//			query.setParameter(0,node.getNodeIp());
//			LOG.debug("attach successful");
//			return  query.list();
//			
//			
//		}catch(RuntimeException re){
//			LOG.error("getCurrentCommand failed",re);
//			throw re;
//		}	
//	}
	
	
	public void reinitCommand(Command instance) {
		LOG.debug("reinitCommand commandId:"+instance.getCommandId());
		try
		{
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(70));
			this.attachDirty(instance);
			LOG.debug("reinitCommand successful commandId:"+instance.getCommandId());
			
		}catch(RuntimeException re){
			LOG.error("reinitCommand failed commandId:"+instance.getCommandId(),re);
			throw re;
		}	
	}
	public void setFinish(Command instance) {
		LOG.debug("setFinish");
		try
		{
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(72));
			this.attachDirty(instance);
			LOG.debug("setFinish successful");
			
		}catch(RuntimeException re){
			LOG.error("setFinish failed",re);
			throw re;
		}	
	}
	private List getInProgress(Quest quest) {
		LOG.debug("getInProgress  questId:"+quest.getQuestId());
		try
		{
			Query query = getSession().createQuery("from Command as command where command.status.statusId=71 and command.quest.questId=? ");
			query.setParameter(0,quest.getQuestId());
			LOG.debug("getInProgress successful questId:"+quest.getQuestId());
			return  query.list();
			
		}catch(RuntimeException re){
			LOG.error("getInProgress failed questId:"+quest.getQuestId(),re);
			throw re;
		}	
	}

	public List getFinish(Quest quest) {
		LOG.debug("getFinish questId:"+quest.getQuestId());
		try
		{
			Query query = getSession().createQuery("from Command as command where command.status.statusId=72 and command.quest.questId=? order by command.sendTime desc");
			query.setParameter(0,quest.getQuestId());
			LOG.debug("getFinish successful questId:"+quest.getQuestId());
			return  query.list();
			
			
		}catch(RuntimeException re){
			LOG.error("getFinish failed questId:"+quest.getQuestId(),re);
			throw re;
		}	
	}
	public boolean isInProgress(Quest quest){
		LOG.debug("isInProgress questId:"+quest.getQuestId());
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
					LOG.debug("finishCommand happened before "+mins+" mins" );
					if(mins<30)	return true;
					else return false;
				}
				else{
					return false;
				}
			}
		}catch(RuntimeException re){
			LOG.error("isInProgress failed questId:"+quest.getQuestId(),re);
			throw re;
		}
	}
	
	public String getNote(Command command){
		LOG.debug("getNote commandId: "+command.getCommandId());
		StringBuffer note = new StringBuffer();
		note.append(command.getQuest().getQuestId()).append(".").append(command.getCommandId()).append(".").append(command.getQuest().getQuestName()).append(":");
		if( "GETFRAME".equals(command.getType()) ){
			note.append("get frame info. ");
		}
		Iterator<Commandarg> ite_Args = command.getCommandargs().iterator();
		while(ite_Args.hasNext()){
			Commandarg arg = ite_Args.next();
			note.append(" ").append(arg.getCommandmodelarg().getArgName()).append(" ").append(arg.getValue()).append(" ");
		}
		LOG.debug("getNote success commandId: "+command.getCommandId()+" note:"+note.toString());
		return note.toString();
	}
	private void reinitInProgressCommand(){
		LOG.debug("reinitInProgressCommand ");
		try
		{
			StatusDAO statusDAO = new StatusDAO();
			Query query = getSession().createQuery("from Command as command where command.status.statusId=71");
			Status status = statusDAO.findById(70);
			Iterator<Command> ite_Commands = query.list().iterator();
			while( ite_Commands.hasNext() ){
				Command command = ite_Commands.next();
				command.setStatus(status);
			}
			LOG.debug("reinitInProgressCommand successful");
			
		}catch(RuntimeException re){
			LOG.error("reinitInProgressCommand failed",re);
			throw re;
		}	
	}

	
}