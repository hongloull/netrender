package test.test;
import org.hibernate.Transaction;
import org.junit.Test;

import com.webrender.dao.CommandmodelDAO;
import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.NodegroupDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;
import com.webrender.dao.RightDAO;
public class ReguserManyToMany {

	@Test
	public void testAddReguser()
	{
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser reguser = null;
		reguser = regUserDAO.findByRegName("test20090715");
		if (reguser ==null) reguser = new Reguser("test20090715","test");
		RightDAO rightDAO = new RightDAO();
		CommandmodelDAO modelDAO = new CommandmodelDAO();
		NodegroupDAO groupDAO = new NodegroupDAO();
		
		Transaction tx = HibernateSessionFactory.getSession().beginTransaction();
	
		reguser.getRights().addAll(rightDAO.findAll());
		reguser.getModels().addAll(modelDAO.findAll());
		reguser.getNodegroups().addAll(groupDAO.findAll());
		regUserDAO.save(reguser);
		tx.commit();
		HibernateSessionFactory.closeSession();
	}
	@Test
	public void testDelReguser(){
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser reguser = regUserDAO.findByRegName("test20090715");
		if (reguser==null) return;
		Transaction tx = HibernateSessionFactory.getSession().beginTransaction();
		regUserDAO.delete(reguser);
		tx.commit();
		HibernateSessionFactory.closeSession();
	}
}
