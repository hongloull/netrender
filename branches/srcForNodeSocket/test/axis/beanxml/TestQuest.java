package test.axis.beanxml;


import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import com.webrender.axis.beanxml.QuestUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Quest;
import com.webrender.dao.QuestDAO;

//'Job Name',               questName 1
//'Job ID',                 questId   1
//'Priority',               pri       1
//'Destination Pool',       nodegroup  
//'Engine',                 Model.commandModelName 2
//'Starting Time',          commitTime 1
//'Progress',               progress   2
//'User Instances',         nodeNum    2
//'Submitted by',           regName    1
//'Staring Frame',          startFrame 2
//'Ending frame',           endFrame   2
//'Total Frames',           totalFrames2
//'Filename'                fileName   2
public class TestQuest {
	@Test
	public void testBean2XML()
	{
		
		//Session session = HibernateSessionFactory.getSession();
		QuestDAO questDAO = new QuestDAO();
		Element root =new Element("Quests");
		Iterator ite_quests =  questDAO.findAll().iterator();
		while(ite_quests.hasNext())
		{
			Quest quest = (Quest) ite_quests.next();
			Element element = QuestUtils.bean2xml(quest);
			element.addAttribute("commandModelName",quest.getCommandmodel().getCommandModelName());
			element.addAttribute("progress", "100" ) ;
			element.addAttribute("nodeNum","3");
			element.addAttribute("startFrame", "10");
			element.addAttribute("endFrame", "250");
			element.addAttribute("totalFrames", "240");
			element.addAttribute("fileName","/project/wolf.ma");
			root.addContent(element);
		}
		Document doc = new Document(root);
		System.out.println( XMLOut.outputToString(doc) ); 
	//	System.out.println( element.toString() );
		
	}

}
