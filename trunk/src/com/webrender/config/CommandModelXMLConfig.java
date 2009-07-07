package com.webrender.config;

import java.io.File;
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

import com.webrender.axis.BaseAxis;
import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.CommandmodelargUtils;
import com.webrender.axis.beanxml.XMLOut;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
public class CommandModelXMLConfig extends XMLConfig {
	private static List lis_CMs = (new CommandmodelDAO()).findAll();
	private static final Log log = LogFactory.getLog(CommandModelXMLConfig.class);
	
	public void loadFromXML(File file) throws JDOMException {
		log.debug("loadFromXML");
		SAXBuilder sb =  new SAXBuilder();
		Document doc = sb.build(file);
		int index = file.getName().lastIndexOf(".xml");
		String commandModelName = file.getName().substring(0, index);
		if(file.canWrite()) log.info(file.getAbsoluteFile()+": canWrite");
		else  log.error(file.getAbsoluteFile()+": cannot Write");
	
		Transaction tx = null;
		CommandmodelDAO cMDAO = new CommandmodelDAO();
		CommandmodelargDAO cMADAO = new CommandmodelargDAO();
		try{
			
			tx = getTransaction();
			Element element = doc.getRootElement();
			if ( element.getAttribute("commandModelName") == null){
				element.addAttribute("commandModelName",commandModelName);				
			}
			else{
				element.getAttribute("commandModelName").setValue(commandModelName);
			}
			Commandmodel cM = CommandmodelUtils.xml2bean(element);
			cMDAO.save(cM);
//			if ( ! cM.getCommandModelId().toString().equals(  element.getAttributeValue("commandModelId") ) ){ // 保存后的CommandModelId 与XML内的Id不同。需要将数据库中ID存入XML中
//				if (element.getAttribute("commandModelId")!=null )element.getAttribute("commandModelId").setValue(cM.getCommandModelId().toString());
//				else element.addAttribute("commandModelId", cM.getCommandModelId().toString());
//			}
			element.removeAttribute("commandModelName");
			List<Element> lis_args = element.getChildren("Commandmodelarg");
			int argsSize = lis_args.size();
			Set set_CMAs = cM.getCommandmodelargs();
			for(int i =0;i<argsSize;i++)
			{
				Commandmodelarg cMArg = CommandmodelargUtils.xml2bean(lis_args.get(i));
				cMArg.setCommandmodel(cM);
				cM.getCommandmodelargs().add(cMArg);
				cMADAO.save(cMArg);
				set_CMAs.remove(cMArg);
				if ( ! cMArg.getCommandModelArgId().toString().equals(  lis_args.get(i).getAttributeValue("commandModelArgId") ) ){ // 保存后的CommandModelArgId 与XML内的Id不同。需要将数据库中ID存入XML中
					if(lis_args.get(i).getAttribute("commandModelArgId")!=null) lis_args.get(i).getAttribute("commandModelArgId").setValue(cMArg.getCommandModelArgId().toString());
					else lis_args.get(i).addAttribute("commandModelArgId",cMArg.getCommandModelArgId().toString());
				}
			}
			Iterator ite_CMAs = set_CMAs.iterator();
			while(ite_CMAs.hasNext()){
				cMADAO.delete( (Commandmodelarg)ite_CMAs.next() );
			}
			tx.commit();
			XMLOut.outputToFile(doc,file);
			lis_CMs.remove(cM);
			
			
		}
		catch(Exception e)
		{
			log.error("",e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}					
		return;
	}
	
	public void deleteExtraData(){
		Transaction tx = null;
		log.debug("deleteExtraModel");
		try{
			tx = getTransaction();
			Iterator ite_CMs = lis_CMs.iterator();
			CommandmodelDAO cmDAO = new CommandmodelDAO();
			while(ite_CMs.hasNext()){
				cmDAO.delete( (Commandmodel)ite_CMs.next() );
			}
			tx.commit();
			log.debug("deleteExtraModel success");
		}
		catch(Exception e)
		{
			log.error("deleteExtraModel fail", e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}	
	}
	
	
}
