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
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.Questarg;
import com.webrender.dao.QuestargDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.StatusDAO;
import com.webrender.logic.CalcFrame;
import com.webrender.server.ControlThreadServer;
import com.webrender.axis.beanxml.ChunkDetailUtils;

public class QuestOperate extends BaseAxis {
	
	private static final Log log = LogFactory.getLog(QuestOperate.class);
	//String NotLogin = "<ResultSet>NotLogin</ResultSet>";
	public String CommitQuest(String questXML)
	{
	//	System.out.println(questXML);
		log.debug("CommitQuest");
		String regUserId = "1"; //this.IsLogin();
		
		try{
			if (!this.canVisit(1)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		
		try{
			
			this.closeSession();
			StatusDAO statusDAO = new StatusDAO();
			SAXBuilder builder = new SAXBuilder();
			InputStream inputStream = new ByteArrayInputStream(questXML.getBytes());
			Document doc = builder.build(inputStream);
			Element ele_quest = doc.getRootElement();
			Quest quest = QuestUtils.xml2bean(ele_quest);
			
			Element ele_model = ele_quest.getChild("Commandmodel");
			Commandmodel model = CommandmodelUtils.xml2bean(ele_model);
			quest.setCommandmodel(model);
			
			ReguserDAO reguserDAO = new ReguserDAO();
			
			quest.setStatus(statusDAO.findById(50));
			quest.setReguser(reguserDAO.findById(Integer.parseInt(regUserId)));
			
			quest.setCommitTime(new Date());

			Transaction tx = null;
			try{
				tx = getTransaction();
				
				QuestDAO questDAO = new QuestDAO();
				questDAO.save(quest);
				
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
				
				tx.commit();
				log.debug("CommitQuest save successful");
			}
			catch(Exception e)
			{
				log.error("commitQuest fail",e);
				if (tx != null) 
				{
					tx.rollback();
				}
				log.error(" CommitQuest save quest failed",e);
				return BaseAxis.ActionFailure;
			}finally
			{
				this.closeSession();
			}
		
			ControlThreadServer.getInstance().resume();
			return BaseAxis.ActionSuccess;
		} catch (Exception e) {
			log.error("commitQuest fail",e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
		
	}

	public String deleteQuest(String questId)
	{
		log.debug("deleteQuest");
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			StatusDAO statusDAO = new StatusDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.delete(quest);
			tx.commit();
			log.debug("deleteQuest success");
			return BaseAxis.ActionSuccess;
		}
		catch(Exception e)
		{
			log.error("deleteQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
	}
	public String pauseQuest(String questId)
	{
		log.debug("pauseQuest");
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.pauseQuest(quest);
			tx.commit();
			log.debug("pauseQuest success");
			return BaseAxis.ActionSuccess;
		}
		catch(Exception e)
		{
			log.error("pauseQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String resumeQuest(String questId)
	{
		log.debug("resumeQuest");
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.resumeQuest(quest);
			tx.commit();
			ControlThreadServer.getInstance().resume();
			log.debug("resumeQuest success");
			return BaseAxis.ActionSuccess;
		}
		catch(Exception e)
		{
			log.error("resumeQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
	}
	
	public String reinitQuest(String questId)
	{
		log.debug("reinitQuest");
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		Transaction tx = null;
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			questDAO.reinitQuest(quest);
			tx.commit();
			ControlThreadServer.getInstance().resume();
			log.debug("reinitQuest success");
			return BaseAxis.ActionSuccess;
		}
		catch(Exception e)
		{
			log.error("reinitQuest fail",e);
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  changePriority(String questId,String pri)
	{
		Transaction tx = null;
		
		try{
			if (!this.canVisit(4)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			quest.setPri(Short.parseShort(pri));
			questDAO.attachDirty(quest);
			tx.commit();
			return BaseAxis.ActionSuccess;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String  changeMaxNodes(String questId,String maxNodes)
	{
		Transaction tx = null;
		
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		try
		{
			tx = getTransaction();
			QuestDAO questDAO = new QuestDAO();
			Quest quest = questDAO.findById(Integer.parseInt(questId));
			quest.setMaxNodes(Integer.parseInt(maxNodes));
			questDAO.attachDirty(quest);
			tx.commit();
			return BaseAxis.ActionSuccess;
		}
		catch(Exception e)
		{
			if (tx != null) 
			{
				tx.rollback();
			}			
			return BaseAxis.ActionFailure;
		}
		finally
		{
			this.closeSession();
		}
	}
	public String changePool(String questId , String nodeGroupId)
	{
		try{
			if (!this.canVisit(3) && ( !this.canVisit(2) || !this.isSelf(Integer.parseInt(questId)) ) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
		return BaseAxis.ActionFailure;
	}
	
	public String getDetail(String questId)
	{
		log.debug("getDetail");
		
		try{
			if (!this.canVisit(7) ){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		
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
			log.debug("getDetail success");
			return XMLOut.outputToString(doc);
		}
		catch(Exception e)
		{
			log.error("getDetail fail",e);
			return BaseAxis.ActionFailure;
		}finally
		{
			this.closeSession();
		}
		
		
	}
	
	public String getChunkDetail(String questId)
	{
		try{
			if (!this.canVisit(7)){
				return BaseAxis.RightError;
			}			
		}catch(Exception e){
			log.error("RightVisit error",e);
			return BaseAxis.RightError;
		}
		log.debug("getChunkDetail");
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
			log.debug("getChunkDetail success");
			return XMLOut.outputToString(doc);
		}
		catch(Exception e)
		{
			log.error("getChunkDetail fail",e);
			
			return BaseAxis.ActionFailure;
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
