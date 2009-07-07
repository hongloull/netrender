package com.webrender.axis.beanxml;

import java.text.SimpleDateFormat;
import org.jdom.Element;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;
import com.webrender.dao.QuestargDAO;

public class QuestUtils {
	 public static Element bean2xml(Quest quest){
		 Element root = new Element("Quest");
		 root.addAttribute("questId",quest.getQuestId().toString());
		 root.addAttribute("questName",quest.getQuestName());
		 root.addAttribute("pri", quest.getPri().toString()+"");
		 root.addAttribute("regName",quest.getReguser().getRegName()+"");
		 if (quest.getMaxNodes()!=null) root.addAttribute("maxNode",quest.getMaxNodes().toString());
		 SimpleDateFormat   df=new  SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");   
		 root.addAttribute("commitTime",df.format(quest.getCommitTime())+"");
		 if (quest.getPacketSize()!=null) root.addAttribute("packetSize",quest.getPacketSize().toString());
		 root.addAttribute("Nodes","All");
		 return root;
	 }
		 
		 
		 
	 public static Quest xml2bean(Element element)
	 {
		 Quest quest = null;
		 String questId = element.getAttributeValue("questId");
		 String questName = element.getAttributeValue("questName");
		 String maxNode = element.getAttributeValue("maxNode");
		 String packetSize = element.getAttributeValue("packetSize");
		 String pri = element.getAttributeValue("pri");
		 String information = element.getAttributeValue("information");
		 if(questId!=null)
		 {
			QuestDAO questDAO = new QuestDAO();
			quest = questDAO.findById(Integer.parseInt(questId));
		 }
		 else
		 {
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
		 if(information != null)	quest.setInformation(information);
		 if(questName != null)	quest.setQuestName(questName);

		 return quest;
	 }
	 
	 public static Element bean2xml_State(Quest quest)
	 {
		 QuestargDAO questargDAO = new QuestargDAO();
		 QuestDAO questDAO = new QuestDAO();
		 Element root = bean2xml(quest);
		 root.addAttribute("status",questDAO.getStatus(quest)+"");
		 root.addAttribute("progress",questDAO.getProgress(quest)+"");
		 root.addAttribute("commandModelName",quest.getCommandmodel().getCommandModelName());
		 root.addAttribute("startFrame",questargDAO.getStartFrame(quest)+"");
		 root.addAttribute("endFrame",questargDAO.getEndFrame(quest)+"");
		 root.addAttribute("fileName",questargDAO.getFileName(quest)+"");
		 return root;
	 }
	 

}
