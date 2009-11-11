package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.QuestargDAO;


public final class QuestUtils {
	private static final Log LOG = LogFactory.getLog(QuestUtils.class);
	 public Element bean2xml(Quest quest){
		 LOG.debug("bean2xml questId"+quest.getQuestId());
		 Element root = new Element("Quest");
		 root.addAttribute("questId",quest.getQuestId().toString());
		 root.addAttribute("questName",quest.getQuestName());
		 root.addAttribute("pri", quest.getPri().toString()+"");
		 root.addAttribute("regName",quest.getReguser().getRegName()+"");
		 if (quest.getMaxNodes()!=null) root.addAttribute("maxNode",quest.getMaxNodes().toString());
		 SimpleDateFormat   df=new  SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");   
		 root.addAttribute("commitTime",df.format(quest.getCommitTime())+"");
		 if (quest.getPacketSize()!=null) root.addAttribute("packetSize",quest.getPacketSize().toString());
		 if(quest.getNodegroup()!=null)root.addAttribute("Nodes",quest.getNodegroup().getNodeGroupName());
		 LOG.debug("bean2xml success questId"+quest.getQuestId());
		 return root;
	 }
		 
		 
		 
	 public Quest xml2bean(Element element)
	 {
		 LOG.debug("xml2bean");
		 Quest quest = null;
		 String questId = element.getAttributeValue("questId");
		 String questName = element.getAttributeValue("questName");
		 String maxNode = element.getAttributeValue("maxNode");
		 String packetSize = element.getAttributeValue("packetSize");
		 String pri = element.getAttributeValue("pri");
		 if(questId!=null)
		 {
			QuestDAO questDAO = new QuestDAO();
			quest = questDAO.findById(Integer.parseInt(questId));
		 }
		 if(quest==null)
		 {
			 LOG.info("xml2bean new quest "+ questName );
			 quest = new Quest();
		 }
		 try{
			 if( maxNode!=null) quest.setMaxNodes(Integer.parseInt(maxNode));			 
		 }catch(NumberFormatException e){
			 
		 }
		 try{
			 if(packetSize!=null) quest.setPacketSize(Short.parseShort(packetSize));			 
		 }catch(NumberFormatException e){
		 }
		 try{
			 if(pri!=null)	quest.setPri(Short.parseShort(pri));		 
		 }catch(NumberFormatException e){
		 }
		 if(questName != null)	quest.setQuestName(questName);
		 LOG.debug("xml2bean success questName:"+ questName);
		 return quest;
	 }
	 
	 public Element bean2xmlWithState(Quest quest)
	 {
		 LOG.debug("bean2xmlWithState questId:"+quest.getQuestId());
		 QuestargDAO questargDAO = new QuestargDAO();
		 QuestDAO questDAO = new QuestDAO();
		 Element root = bean2xml(quest);
		 root.addAttribute("status",questDAO.getStatus(quest)+"");
		 root.addAttribute("progress",questDAO.getProgress(quest)+"");
		 root.addAttribute("commandModelName",quest.getCommandmodel().getCommandModelName());
//		 String startFrame = questargDAO.getStartFrame(quest)+"";
//		 String endFrame   = questargDAO.getEndFrame(quest)+"";
		 try{
//			 CalcFrame cF = new CalcFrame();
			 root.addAttribute("totalFrames",quest.getTotalFrames()+"");
		 }catch(Exception e){
			 
		 }
		 
//		 root.addAttribute("startFrame",questargDAO.getStartFrame(quest)+"");
//		 root.addAttribute("endFrame",questargDAO.getEndFrame(quest)+"");
		 root.addAttribute("frames",questargDAO.getFramesValue(quest)+"");
		 root.addAttribute("fileName",questargDAO.getFileName(quest)+"");
		 LOG.debug("bean2xmlWithState success questId:"+quest.getQuestId());
		 return root;
	 }
	 

}
