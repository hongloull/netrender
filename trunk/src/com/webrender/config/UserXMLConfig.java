package com.webrender.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import com.webrender.axis.beanxml.ReguserUtils;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;


public class UserXMLConfig extends XMLConfig {
	private static final Log log = LogFactory.getLog(UserXMLConfig.class);
	private static List lis_Users = (new ReguserDAO()).findAll();
	@Override
	public void deleteExtraData() {
	}

	@Override
	public void loadFromXML(File file) throws JDOMException {
		log.debug("loadFromXML");
		SAXBuilder sb =  new SAXBuilder();
		Document doc = sb.build(file);
		Iterator<Element> ite_Users = doc.getRootElement().getChildren("User").iterator();
		Transaction tx = null;
		ReguserDAO regUserDAO = new ReguserDAO();
		while(ite_Users.hasNext()){
			try{
				tx = getTransaction();
				Reguser user = ReguserUtils.xml2Bean(ite_Users.next());
				if(user != null){
					regUserDAO.save(user);
				}
				tx.commit();
				log.debug("Users.size(): "+ lis_Users.size() );
				lis_Users.remove(user);
				
			}catch(Exception e)
			{
				log.error("loadRole fail",e);
				if (tx != null) 
				{
					tx.rollback();
				}
			}
		}
		log.debug("loadFromXML success");
		
		log.debug("deleteExtraUsers");
		try{
			tx =  getTransaction();
			log.info("Users.size(): "+ lis_Users.size() );
			Iterator<Reguser> ite_UserPojos = lis_Users.iterator();
			while(ite_UserPojos.hasNext()){
				regUserDAO.delete(  ite_UserPojos.next() );
			}
			tx.commit();
			log.debug("deleteExtraUsers success");
			
		}catch(Exception e)
		{
			log.error("deleteExtraUsers fail", e);
			if (tx != null) 
			{
				tx.rollback();
			}
		}
	}

}
