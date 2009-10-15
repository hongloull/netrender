package com.webrender.axis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.NodeUtils;
import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.QuestargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Command;
import com.webrender.dao.CommandDAO;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.Executelog;
import com.webrender.dao.ExecutelogDAO;
import com.webrender.dao.Node;
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
import com.webrender.logic.CalcFrame;
import com.webrender.server.ControlThreadServer;
import com.webrender.server.Dispatcher;
import com.webrender.axis.beanxml.ChunkDetailUtils;

public class QuestOperate extends BaseAxis {
	
	private static final Log LOG = LogFactory.getLog(QuestOperate.class);
	//String NotLogin = "<ResultSet>NotLogin</ResultSet>";
	public String CommitQuest(String questXML)
	{
		if (  !this.canVisit(0) &&  !this.canVisit(10) ) return BaseAxis.RIGHTERROR;
		
		LOG.debug("CommitQuest");
		int regUserId =  this.getLoginUserId();
		
	
		
		try{
			
			this.closeSession();
			StatusDAO statusDAO = new StatusDAO();
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());
			Document doc = builder.build(inputStream);
			Element ele_quest = doc.getRootElement();
			Quest quest = QuestUtils.xml2bean(ele_quest);
			String nodeGroupName = ele_quest.getAttributeValue("Nodes");
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup nG =  nGDAO.findByNodeGroupName(nodeGroupName);
			quest.setNodegroup(nG);
						
			Element ele_model = ele_quest.getChild("Commandmodel");
			Commandmodel model = CommandmodelUtils.xml2bean(ele_model);
			quest.setCommandmodel(model);
			
			ReguserDAO reguserDAO = new ReguserDAO();
			Reguser logUser = reguserDAO.findById( regUserId );
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
				
				for(int j =0 ; j<questargssize ;j++)
				{
					Questarg questarg = QuestargUtils.xml2bean(lis_questargs.get(j));
					questarg.setQuest(quest);
					quest.getQuestargs().add(questarg);
					questargDAO.save(questarg);
				}	
				// AddCommand  -start -end -byFrame   frame/size
				CalcFrame calcFrame = new CalcFrame();
				calcFrame.calcFrames(quest,quest.getPacketSize());
				logOperate(getLoginUserId(),Operatelog.ADD,"AddQuest "+quest.getQuestName());
				tx.commit();
				LOG.debug("CommitQuest save successful");
			}
			catch(Exception e)
			{
				LOG.error("commitQuest fail",e);
				if (tx != null) 
				{
					tx.rollback();
				}
				LOG.error(" CommitQuest save quest failed",e);
				return BaseAxis.ACTIONFAILURE;
			}
		
//			ControlThreadServer.getInstance().resume();
//			Dispatcher.getInstance().exeCommands();
			
			return quest.getQuestId().toString();
		} catch (Exception e) {
			LOG.error("commitQuest fail",e);
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
		
	}

	public String deleteQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
					return BaseAxis.RIGHTERROR;
		
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
			logOperate(getLoginUserId(),Operatelog.DEL,"Delete quest "+questName);
			tx.commit();
			LOG.debug("deleteQuest success");
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("deleteQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
	}
	public String pauseQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		
		LOG.debug("pauseQuest");
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.pauseQuest(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Pause quest "+quest.getQuestName());
			tx.commit();
			LOG.debug("pauseQuest success");
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("pauseQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String resumeQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		
		LOG.debug("resumeQuest");

		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.resumeQuest(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Resume quest "+quest.getQuestName());
			tx.commit();
//			ControlThreadServer.getInstance().resume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("resumeQuest success");
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("resumeQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String reinitQuest(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		LOG.debug("reinitQuest");

		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.reinitQuest(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Reinit quest "+quest.getQuestName());
			tx.commit();
//			ControlThreadServer.getInstance().resume();
//			Dispatcher.getInstance().exeCommands();
			
			LOG.debug("reinitQuest success");
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("reinitQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String setFinish(String questId)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		LOG.debug("setFinish");

		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.setFinish(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"setFinish quest "+quest.getQuestName());
			tx.commit();
			
			LOG.debug("setFinish success");
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			LOG.error("setFinish fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String changeName(String questId,String name){
		
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;
		
		Transaction tx = null;
		
		
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			if (quest==null){
				return BaseAxis.ACTIONFAILURE+" QuestNotExist";
			}
			quest.setQuestName(name);
			questDAO.attachDirty(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Change "+quest.getQuestName()+" to "+name);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String  changePriority(String questId,String pri)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		
		Transaction tx = null;
		
//		try{
//			if (!this.canVisit(4)){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
		
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			quest.setPri(Short.parseShort(pri));
			questDAO.attachDirty(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Change quest "+quest.getQuestName()+"'s priority to "+pri);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  changeMaxNodes(String questId,String maxNodes)
	{
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		
		Transaction tx = null;
		
//		try{
//			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
//		
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			quest.setMaxNodes(Integer.parseInt(maxNodes));
			questDAO.attachDirty(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Change quest "+quest.getQuestName()+"'s maxNodes to "+maxNodes);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String changePool(String questId , String poolName)
	{
		
		if (  ( !this.canVisit(0) && !this.canVisit(12) ) && ( !this.canVisit(11) || !this.isSelf(Integer.parseInt(questId)) ) )
			return BaseAxis.RIGHTERROR;

		
		Transaction tx = null;
		
//		try{
//			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
//		
		try
		{
			QuestDAO questDAO = new QuestDAO();
			NodegroupDAO nGDAO = new NodegroupDAO();
			Nodegroup nG = nGDAO.findByNodeGroupName(poolName);
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			if(quest==null || nG==null){
				return BaseAxis.ACTIONFAILURE+" questId or poolName not exist";
			}
			tx = getTransaction();
			quest.setNodegroup(nG);
			questDAO.attachDirty(quest);
			logOperate(getLoginUserId(),Operatelog.MOD,"Change quest "+quest.getQuestName()+"'s pool to "+poolName);
			tx.commit();
			return BaseAxis.ACTIONSUCCESS;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}
		finally
		{
			this.closeSession();
		}
		
	}
	
	public String getDetail(String questId)
	{
		
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		LOG.debug("getDetail");
		
//		try{
//			if (!this.canVisit(7) ){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
		
		try{
			this.closeSession();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			Element root = QuestUtils.bean2xml(quest);
			
			Document doc = new Document(root);
			
			Commandmodel cm = quest.getCommandmodel();
			root.addContent(CommandmodelUtils.bean2xml(cm));
			Iterator ite_Questargs = quest.getQuestargs().iterator();
			while ( ite_Questargs.hasNext() )
			{
				Questarg questArg = (Questarg)ite_Questargs.next();
				root.addContent(QuestargUtils.bean2xml(questArg));
			}
			LOG.debug("getDetail success");
			return XMLOut.outputToString(doc);
		}
		catch(Exception e)
		{
			LOG.error("getDetail fail",e);
			return BaseAxis.ACTIONFAILURE;
		}finally
		{
			this.closeSession();
		}
		
		
	}
	
	public String getChunkDetail(String questId)
	{
		if ( this.getLoginUserId()==0 )	return BaseAxis.NOTLOGIN;

		LOG.debug("getChunkDetail");
		
//		try{
//			if (!this.canVisit(7)){
//				return BaseAxis.RIGHTERROR;
//			}			
//		}catch(Exception e){
//			LOG.error("RightVisit error",e);
//			return BaseAxis.RIGHTERROR;
//		}
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
			while(ite_Commands.hasNext())
			{
				command = (Command)ite_Commands.next();
				element = ChunkDetailUtils.getElement(command);
				if(element!=null){
					element.addAttribute("id",i+"");
					element.addAttribute("commandId",command.getCommandId().toString());
					root.addContent(element);
					i++;
				}
			}
			LOG.debug("getChunkDetail success");
			return XMLOut.outputToString(doc);
		}
		catch(Exception e)
		{
			LOG.error("getChunkDetail fail",e);
			
			return BaseAxis.ACTIONFAILURE+e.getMessage();
		}finally
		{
			this.closeSession();
		}
	}
	
	@Override
	protected boolean isSelf(int questId){
		QuestDAO questDAO = new QuestDAO();
		if (questDAO.findById(questId).getReguser().getRegUserId()==this.getLoginUserId() ){
			return true;
		}
		return false;
	}
}
