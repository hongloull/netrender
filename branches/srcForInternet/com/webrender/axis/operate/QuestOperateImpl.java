package com.webrender.axis.operate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.ChunkDetailUtils;
import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.QuestargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Operatelog;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.logic.CalcCommands;
import com.webrender.logic.CalcFrames;
import com.webrender.logic.CalcManyToMany;
import com.webrender.logic.CalcOneToMany;
import com.webrender.server.ControlThreadServer;
import com.webrender.tool.NameMap;

public class QuestOperateImpl extends BaseOperate {
	
	private static final Log LOG = LogFactory.getLog(QuestOperateImpl.class);
	//String NotLogin = "<ResultSet>NotLogin</ResultSet>";
	private QuestUtils questUtils = new QuestUtils();
	private CommandmodelUtils commandmodelUtils = new CommandmodelUtils();
	private QuestargUtils questargUtils = new QuestargUtils();
	private XMLOut xmlOut = new XMLOut();
	public String CommitQuest(String questXML,int regUserId)
	{
		//TODO  没权限的人不能提交Edit其他人的任务
		LOG.debug("commitQuest begin");		
		try{
			
			this.closeSession();
			StatusDAO statusDAO = new StatusDAO();
			ReguserDAO reguserDAO = new ReguserDAO();
			Reguser logUser = reguserDAO.findById( regUserId );
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());
			Document doc = builder.build(inputStream);
			Element ele_quest = doc.getRootElement();
			Quest quest = questUtils.xml2bean(ele_quest);
			String nodeGroupName = ele_quest.getAttributeValue("Nodes");
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup nG =  nGDAO.findByNodeGroupName(nodeGroupName);
			if( !logUser.getNodegroups().contains(nG)){
				LOG.warn(BaseOperate.ACTIONFAILURE+"Not permitted to use Pool '"+nodeGroupName+"' ");
				return BaseOperate.ACTIONFAILURE+"Not permitted to use Pool '"+nodeGroupName+"' ";
			}
			quest.setNodegroup(nG);
			Element ele_model = ele_quest.getChild("Commandmodel");
			Commandmodel model = commandmodelUtils.xml2bean(ele_model);
			if( !logUser.getModels().contains(model)){
				LOG.warn(BaseOperate.ACTIONFAILURE+"Not permitted to use Model '"+model.getCommandModelName()+"' ");
				return BaseOperate.ACTIONFAILURE+"Not permitted to use Model '"+model.getCommandModelName()+"' ";
			}
			String modelType = model.getType();
			quest.setCommandmodel(model);
			quest.setStatus(statusDAO.findById(50));
			quest.setReguser(logUser);
			
			quest.setCommitTime(new Date());

			Transaction tx = null;
			try{
				tx = getTransaction();
				
				QuestDAO questDAO = new QuestDAO();
				questDAO.save(quest);
				questDAO.delQuestRel(quest);
				
				List<Element> lis_questargs = ele_quest.getChildren("Questarg");
				
				int questargssize = lis_questargs.size();
				
				QuestargDAO questargDAO = new QuestargDAO();
				Questarg questarg = null;
				CalcCommands calcCommands = null;
				if(NameMap.RENDER.equalsIgnoreCase(modelType)){
					for(int j =0 ; j<questargssize ;j++)
					{
						questarg = questargUtils.xml2bean(lis_questargs.get(j));
						questarg.setQuest(quest);
						quest.getQuestargs().add(questarg);
						questargDAO.save(questarg);
					}
					Command command = new Command(quest);
					command.setType(NameMap.PRELIGHT);
					command.setStatus(statusDAO.findById(70));
					(new CommandDAO()).save(command);
					calcCommands = new CalcFrames(); 
				}
				else if(NameMap.MANYTOMANY.equalsIgnoreCase(modelType)){
					LinkedHashSet questargs = new LinkedHashSet();
					for(int j =0 ; j<questargssize ;j++)
					{
						questarg = questargUtils.xml2bean(lis_questargs.get(j));
						questarg.setQuest(quest);
						questargDAO.save(questarg);
						questargs.add(questarg);
					}
					
					quest.setQuestargs(questargs);
					
					calcCommands = new CalcManyToMany();
				}
				else if(NameMap.ONETOMANY.equalsIgnoreCase(modelType)){
					for(int j =0 ; j<questargssize ;j++)
					{
						questarg = questargUtils.xml2bean(lis_questargs.get(j));
						questarg.setQuest(quest);
						quest.getQuestargs().add(questarg);
						questargDAO.save(questarg);
					}
					calcCommands = new CalcOneToMany();
				}
				int result = calcCommands.calc(quest);
				int totalSize = calcCommands.getTotalSize();
				quest.setTotalFrames(totalSize);
				questDAO.save(quest);
				switch(result){
				case CalcCommands.SUCCESS:logOperate(regUserId,Operatelog.ADD,"Add Quest "+quest.getQuestName()+" success.");
				break;
				case CalcCommands.LACKFRAME:logOperate(regUserId,Operatelog.MOD,"Quest "+quest.getQuestName()+" lack frame conf");
				break;
				case CalcCommands.NEEDARGS:logOperate(regUserId,Operatelog.ERROR,"Quest "+quest.getQuestName()+" lack user's arg. please check it.");
				break;
				case CalcCommands.NumberFormatException:logOperate(regUserId,Operatelog.ERROR,"Quest "+quest.getQuestName()+" number format error.");
				break;
				}
				tx.commit();
				LOG.debug("CommitQuest save successful");
			}
			catch(Exception e)
			{
				if (tx != null) 
				{
					tx.rollback();
				}
				LOG.error(" CommitQuest save quest failed",e);
				return ACTIONFAILURE+e.getMessage();
			}
		
			ControlThreadServer.getInstance().notifyResume();
//			Dispatcher.getInstance().exeCommands();
			return quest.getQuestId().toString();
			
		} catch (Exception e) {
			LOG.error("commitQuest fail",e);
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
		
	}

	public String deleteQuest(String questId,int regUserId){
		LOG.debug("deleteQuest");
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
//			StatusDAO statusDAO = new StatusDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			String questName = quest.getQuestName();
			questDAO.delete(quest);
			logOperate(regUserId,Operatelog.DEL,"Delete questName "+questName);
			tx.commit();
			LOG.debug("deleteQuest success");
			return ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("deleteQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
	public String pauseQuest(String questId,int regUserId)
	{
		LOG.debug("pauseQuest");
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.pauseQuest(quest);
			logOperate(regUserId,Operatelog.MOD,"Pause quest "+quest.getQuestName());
			tx.commit();
			LOG.debug("pauseQuest success");
			return ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("pauseQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	public String resumeQuest(String questId,int regUserId)
	{
	
		
		LOG.debug("resumeQuest");

		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.resumeQuest(quest);
			logOperate(regUserId,Operatelog.MOD,"Resume quest "+quest.getQuestName());
			tx.commit();
			ControlThreadServer.getInstance().notifyResume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("resumeQuest success");
			return ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("resumeQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String reinitQuest(String questId , int regUserId)
	{
		LOG.debug("reinitQuest");

		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.reinitQuest(quest);
			logOperate(regUserId,Operatelog.MOD,"Reinit quest "+quest.getQuestName());
			tx.commit();
			ControlThreadServer.getInstance().notifyResume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("reinitQuest success");
			return ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("reinitQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String setFinish(String questId ,int regUserId)
	{
		LOG.debug("setFinish");

		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.setFinish(quest);
			logOperate(regUserId,Operatelog.MOD,"setFinish quest "+quest.getQuestName());
			tx.commit();
			
			LOG.debug("setFinish success");
			return ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("setFinish fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String changeName(String questId,String name,int regUserId){
		
		LOG.debug("changeName questId:"+questId+" to name:"+name);
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			if (quest==null){
				return ACTIONFAILURE+" QuestNotExist";
			}
			quest.setQuestName(name);
			questDAO.attachDirty(quest);
			logOperate(regUserId,Operatelog.MOD,"Change "+quest.getQuestName()+" to "+name);
			tx.commit();
			return ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String  changePriority(String questId,String pri,int regUserId)
	{
		LOG.debug("changePriority questId:"+questId+" to pri:"+pri);
		Transaction tx = null;
		try
		{
			short priValue = Short.parseShort(pri);
			if(priValue<0) return ACTIONFAILURE;		
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			quest.setPri(priValue);
			questDAO.attachDirty(quest);
			logOperate(regUserId,Operatelog.MOD,"Change quest "+quest.getQuestName()+"'s priority to "+pri);
			tx.commit();
			return  ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return  ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  changeMaxNodes(String questId,String maxNodes,int regUserId)
	{
		
		LOG.debug("changeMaxNodes questId:"+questId+" maxNodes:"+maxNodes);
		Transaction tx = null;	
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			quest.setMaxNodes(Integer.parseInt(maxNodes));
			questDAO.attachDirty(quest);
			logOperate(regUserId,Operatelog.MOD,"Change quest "+quest.getQuestName()+"'s maxNodes to "+maxNodes);
			tx.commit();
			ControlThreadServer.getInstance().notifyResume();
			return  ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return  ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	public String changePool(String questId , String poolName,int regUserId)
	{
		LOG.debug("changePool questId:"+questId+" poolName:"+poolName);
		Transaction tx = null;	

		try
		{
			QuestDAO questDAO = new QuestDAO();
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup nG = nGDAO.findByNodeGroupName(poolName);
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			if(quest==null || nG==null){
				return  ACTIONFAILURE+" questId or poolName not exist";
			}
			tx = getTransaction();
			quest.setNodegroup(nG);
			questDAO.attachDirty(quest);
			logOperate(regUserId,Operatelog.MOD,"Change quest "+quest.getQuestName()+"'s pool to "+poolName);
			tx.commit();
			ControlThreadServer.getInstance().notifyResume();
			return  ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return  ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
		
	}
	
	public String getDetail(String questId)
	{
		LOG.debug("getDetail questId:"+questId);
		try{
			this.closeSession();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			Element root = questUtils.bean2xml(quest);
			
			Document doc = new Document(root);
			
			Commandmodel cm = quest.getCommandmodel();
			root.addContent(commandmodelUtils.bean2xml(cm));
			Iterator ite_Questargs = quest.getQuestargs().iterator();
			while ( ite_Questargs.hasNext() )
			{
				Questarg questArg = (Questarg)ite_Questargs.next();
				root.addContent(questargUtils.bean2xml(questArg));
			}
			LOG.debug("getDetail success");
			return xmlOut.outputToString(doc);
		}
		catch(Exception e)
		{
			LOG.error("getDetail fail",e);
			return  ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
		
		
	}
	
	public String getChunkDetail(String questId)
	{
		LOG.debug("getChunkDetail questId:"+questId);
		try{
			this.closeSession();
			Element root = new Element("Details");
			Document doc = new Document(root);
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			Iterator ite_Commands = quest.getCommands().iterator();
			
			Command command = null;
			Element element = null;
			int i = 1;
			ChunkDetailUtils chunkDetailUtils = new ChunkDetailUtils();
			while(ite_Commands.hasNext())
			{
				command = (Command)ite_Commands.next();
				element = chunkDetailUtils.getElement(command);
				if(element!=null){
					element.addAttribute("id",i+"");
					element.addAttribute("commandId",command.getCommandId().toString());
					root.addContent(element);
					i++;
				}
			}
			LOG.debug("getChunkDetail success");
			return xmlOut.outputToString(doc);
		}
		catch(Exception e)
		{
			LOG.error("getChunkDetail fail",e);
			
			return  ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}

	public String getPreLight(String questId) {
		LOG.debug("getPreLight questId:" + questId);
		try {
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			
			return quest.getPreLight()+"";
		} catch (Exception e) {
			LOG.error("getPreLight fail questId:" + questId, e);
			return ACTIONFAILURE+e.getMessage();
		} finally {
			this.closeSession();
		}
	}
}
