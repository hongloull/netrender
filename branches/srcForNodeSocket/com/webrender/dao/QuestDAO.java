package com.webrender.dao;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for Quest
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.webrender.dao.Quest
 * @author MyEclipse Persistence Tools
 */

public class QuestDAO extends BaseHibernateDAO {
	private static final Log LOG = LogFactory.getLog(QuestDAO.class);
	// property constants
	public static final String QUEST_NAME = "questName";
	public static final String INFORMATION = "information";
	public static final String PRI = "pri";
	public static final String MAX_NODES = "maxNodes";
	public static final String PACKET_SIZE = "packetSize";

	public void save(Quest transientInstance) {
		LOG.debug("saving Quest instance");
		try {
			getSession().save(transientInstance);
			LOG.debug("save successful");
		} catch (RuntimeException re) {
			LOG.error("save failed", re);
			throw re;
		}
	}

	public void delete(Quest persistentInstance) {
		LOG.debug("deleting Quest instance");
		try {
			getSession().delete(persistentInstance);
			LOG.debug("delete successful");
		} catch (RuntimeException re) {
			LOG.error("delete failed", re);
			throw re;
		}
	}

	public Quest findById(java.lang.Integer id) {
		LOG.debug("getting Quest instance with id: " + id);
		try {
			Quest instance = (Quest) getSession().get(
					"com.webrender.dao.Quest", id);
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Quest instance) {
		LOG.debug("finding Quest instance by example");
		try {
			List results = getSession().createCriteria(
					"com.webrender.dao.Quest").add(Example.create(instance))
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
		LOG.debug("finding Quest instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Quest as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByQuestName(Object questName) {
		return findByProperty(QUEST_NAME, questName);
	}

	public List findByInformation(Object information) {
		return findByProperty(INFORMATION, information);
	}

	public List findByPri(Object pri) {
		return findByProperty(PRI, pri);
	}

	public List findByMaxNodes(Object maxNodes) {
		return findByProperty(MAX_NODES, maxNodes);
	}

	public List findByPacketSize(Object packetSize) {
		return findByProperty(PACKET_SIZE, packetSize);
	}

	public List findAll() {
		LOG.debug("finding all Quest instances");
		try {
			String queryString = "from Quest as  quest order by quest.questId desc";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			LOG.error("find all failed", re);
			throw re;
		}
	}

	public Quest merge(Quest detachedInstance) {
		LOG.debug("merging Quest instance");
		try {
			Quest result = (Quest) getSession().merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Quest instance) {
		LOG.debug("attaching dirty Quest instance");
		try {
			getSession().saveOrUpdate(instance);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Quest instance) {
		LOG.debug("attaching clean Quest instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			LOG.debug("attach successful");
		} catch (RuntimeException re) {
			LOG.error("attach failed", re);
			throw re;
		}
	}
	public void pauseQuest(Quest instance) {
		LOG.debug("pauseQuest");
		try {
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(51));
			this.attachDirty(instance);
			LOG.debug("pauseQuest successful");
		} catch (RuntimeException re) {
			LOG.error("pauseQuest failed", re);
			throw re;
		}
	}
	public void resumeQuest(Quest instance) {
		LOG.debug("resumeQuest");
		try {
			StatusDAO statusDAO = new StatusDAO();
			instance.setStatus(statusDAO.findById(50));
			this.attachDirty(instance);
			LOG.debug("resumeQuest successful");
		} catch (RuntimeException re) {
			LOG.error("resumeQuest failed", re);
			throw re;
		}
	}
	public void reinitQuest(Quest instance)
	{
		LOG.debug("reinitQuest");
		try {
			CommandDAO commandDAO = new CommandDAO();
			Iterator ite_Commands = instance.getCommands().iterator();
			while(ite_Commands.hasNext())
			{
				commandDAO.reinitCommand( (Command)ite_Commands.next() );				
			}
			
			LOG.debug("reinitQuest successful");
		} catch (RuntimeException re) {
			LOG.error("reinitQuest failed", re);
			throw re;
		}
	}

	/**
	 * 获取任务状态
	 * @param quest
	 * @return pause inprogress complete waiting 
	 */
	public String getStatus(Quest quest) {
		LOG.debug("getStatus");
		try {
			int isPause = quest.getStatus().getStatusId()==51?1:0;
			CommandDAO commandDAO = new CommandDAO();
			boolean isProgress = commandDAO.isInProgress(quest);
			int finish = commandDAO.getFinish(quest).size();
			int total = quest.getCommands().size() ;	
			LOG.debug("getStatus successful");
			if (finish ==total) return "Complete";
			else if (isPause==1) return "Pause";
			else if (isProgress == true) return "InProgress";
			else return "Waiting"; 
			
		}
		catch (RuntimeException re) {
			LOG.error("getStatus failed", re);
			throw re;
		}
	}
	public String getProgress(Quest quest){
		CommandDAO commandDAO = new CommandDAO();
		int finish = commandDAO.getFinish(quest).size();
		int total = quest.getCommands().size() ;
		if (total == 0 ) return "error";
		return finish*100/total+"%";
	}

	
}