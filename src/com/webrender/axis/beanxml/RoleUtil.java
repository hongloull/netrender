package com.webrender.axis.beanxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import com.webrender.dao.Role;
import com.webrender.dao.RoleDAO;

public class RoleUtil {
	private static final Log log = LogFactory.getLog(RoleUtil.class);
	
	public static Role xml2Bean(Element element){
		try{
			RoleDAO roleDAO = new RoleDAO();
			String name = element.getAttributeValue("name");
			if (name==null) throw new NullPointerException("Name null");
			Role role = roleDAO.findByRoleName(name);
			if (role==null) role=new Role(name);
			Short highestPri = Short.parseShort(element.getAttributeValue("pri"));
			role.setHighestPri(highestPri );
			return role;		
		}catch(Exception e){
			log.error("xml2Bean error",e);
			return null;
		}
	}
}
