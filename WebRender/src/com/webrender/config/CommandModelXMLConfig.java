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
import org.jdom.input.SAXBuilder;

import com.webrender.axis.beanxml.CommandmodelUtils;
import com.webrender.axis.beanxml.CommandmodelargUtils;
import com.webrender.dao.Commandmodel;
import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.Commandmodelarg;
import com.webrender.dao.CommandmodelargDAO;
public class CommandModelXMLConfig extends XMLConfig {
	private static List lisCMs = (new CommandmodelDAO()).findAll();
	private static final Log LOG = LogFactory.getLog(CommandModelXMLConfig.class);
	private CommandmodelDAO commandModelDAO = new CommandmodelDAO(); 
	public void loadFromXML(File file) {
		LOG.debug("loadFromXML "+file.getName());
		int index = file.getName().lastIndexOf(".xml");
		if (index == -1){
			LOG.debug("not xml return");
			return;
		}
		Transaction tx = null;
		Commandmodel cM = null;
		String commandModelName = file.getName().substring(0, index);
		try{
			SAXBuilder sb =  new SAXBuilder();
			Document doc = sb.build(file);
			if(file.canWrite()) LOG.debug(file.getAbsoluteFile()+": canWrite");
			else{
				LOG.error(file.getAbsoluteFile()+": cannot Write");
			}
			
			CommandmodelDAO cMDAO = new CommandmodelDAO();
			CommandmodelargDAO cMADAO = new CommandmodelargDAO();
			
			tx = getTransaction();
			Element element = doc.getRootElement();
			if ( element.getAttribute("commandModelName") == null){
				element.addAttribute("commandModelName",commandModelName);				
			}
			else{
				element.getAttribute("commandModelName").setValue(commandModelName);
			}
			cM = (new CommandmodelUtils()).xml2bean(element);
			
			cMDAO.save(cM);
//			if ( ! cM.getCommandModelId().toString().equals(  element.getAttributeValue("commandModelId") ) ){ // 保存后的CommandModelId 与XML内的Id不同。需要将数据库中ID存入XML中
//				if (element.getAttribute("commandModelId")!=null )element.getAttribute("commandModelId").setValue(cM.getCommandModelId().toString());
//				else element.addAttribute("commandModelId", cM.getCommandModelId().toString());
//			}
//			element.removeAttribute("commandModelName");
			List<Element> lis_args = element.getChildren("Commandmodelarg");
			int argsSize = lis_args.size();
			Set set_CMAs = cM.getCommandmodelargs();
			for(int i =0;i<argsSize;i++)
			{
				Commandmodelarg cMArg = (new CommandmodelargUtils()).xml2bean(lis_args.get(i),cM.getCommandModelId() );
				cMArg.setCommandmodel(cM);
				cM.getCommandmodelargs().add(cMArg);
				cMADAO.save(cMArg);
				set_CMAs.remove(cMArg);
//				if ( ! cMArg.getCommandModelArgId().toString().equals(  lis_args.get(i).getAttributeValue("commandModelArgId") ) ){ // 保存后的CommandModelArgId 与XML内的Id不同。需要将数据库中ID存入XML中
//					if(lis_args.get(i).getAttribute("commandModelArgId")!=null) lis_args.get(i).getAttribute("commandModelArgId").setValue(cMArg.getCommandModelArgId().toString());
//					else lis_args.get(i).addAttribute("commandModelArgId",cMArg.getCommandModelArgId().toString());
//				}
			}
			Iterator ite_CMAs = set_CMAs.iterator();
			while(ite_CMAs.hasNext()){
				cMADAO.delete( (Commandmodelarg)ite_CMAs.next() );
			}
			tx.commit();
//			XMLOut.outputToFile(doc,file);
		}
		catch(Exception e)
		{
			LOG.error("loadFromXML fail "+file.getName(),e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}finally{
			if(cM!=null){
				lisCMs.remove(cM);				
			}else{
				lisCMs.remove( commandModelDAO.findByCommandModelName(commandModelName) );
			}
		}
		LOG.debug("loadFromXML "+file.getName()+ "success");
		return;
	}
	
	public void deleteExtraData(){
		Transaction tx = null;
		LOG.debug("deleteExtraModel");
		try{
			tx = getTransaction();
			Iterator ite_CMs = lisCMs.iterator();
//			CommandmodelDAO cmDAO = new CommandmodelDAO();
			while(ite_CMs.hasNext()){
				Commandmodel model = (Commandmodel)ite_CMs.next();
				LOG.info("delete commandmodel: "+model.getCommandModelName());
				commandModelDAO.delete( model );
			}
			tx.commit();
			LOG.debug("deleteExtraModel success");
		}
		catch(Exception e)
		{
			LOG.error("deleteExtraModel fail", e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}	
	}
	
	
}
