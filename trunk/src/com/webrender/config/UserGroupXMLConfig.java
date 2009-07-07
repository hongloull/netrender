package com.webrender.config;

import java.io.File;
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

import com.webrender.axis.beanxml.RoleUtil;
import com.webrender.dao.Node;
import com.webrender.dao.Right;
import com.webrender.dao.RightDAO;
import com.webrender.dao.Role;
import com.webrender.dao.RoleDAO;

public class UserGroupXMLConfig extends XMLConfig {
	private static final Log log = LogFactory.getLog(UserGroupXMLConfig.class);
	private static List lis_Roles = (new RoleDAO()).findAll();
	@Override
	public void deleteExtraData() {
		
	}

	@Override
	public void loadFromXML(File file) throws JDOMException {
		log.debug("loadFromXML");
		SAXBuilder sb =  new SAXBuilder();
		Document doc = sb.build(file);
		Iterator<Element> ite_Groups = doc.getRootElement().getChildren("Group").iterator();
		RoleDAO roleDAO = new RoleDAO();
		RightDAO rightDAO = new RightDAO();
		
		Transaction tx = null;
		while(ite_Groups.hasNext()){
			try{
				tx = getTransaction();
				Element element = ite_Groups.next();
				Role role = RoleUtil.xml2Bean(element);
				roleDAO.save(role);
				Set set_Rights = role.getRights();
				HashSet<Right> set_RetainRights = new HashSet<Right>();
				Iterator ite_Rights = element.getChildren("Right").iterator();
				Right right = null;
				while(ite_Rights.hasNext()){
					try{
						Element ele_Right = (Element) ite_Rights.next();
						String id = ele_Right.getAttributeValue("id");
						right = rightDAO.findById(Integer.parseInt(id));
						set_RetainRights.add(right);
						if(!set_Rights.contains(right)){
							set_Rights.add(right);
						}
					}catch(Exception e){
						log.error("RoleAddRight error",e);
					}
				}
				set_Rights.retainAll(set_RetainRights);
				tx.commit();
				lis_Roles.remove(role);
				log.debug("loadRole success");
			}catch(Exception e)
			{
				log.error("loadRole fail",e);
				if (tx != null) 
				{
					tx.rollback();
				}
			}
		}// end while roles
		//deleteExtraData
		log.debug("deleteExtraRole");
		try{
			tx = getTransaction();
			Iterator ite_Roles = lis_Roles.iterator();
			while(ite_Roles.hasNext()){
				Role role = (Role)ite_Roles.next();
				log.debug("deleteExtraRole id : "+role.getRoleId());
				roleDAO.delete( role );
			}
			tx.commit();
			log.debug("deleteExtraRole success");
		}catch(Exception e){
			log.error("deleteExtraRole fail",e);
			if(tx!=null){
				tx.rollback();
			}
		}
		log.debug("loadFromXML success");
	}
}
