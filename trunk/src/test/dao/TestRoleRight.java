package test.dao;

import java.util.List;

import org.hibernate.Transaction;
import org.junit.Test;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.RightDAO;
import com.webrender.dao.RoleDAO;
import com.webrender.dao.Role;
public class TestRoleRight {
//	@Test
	public void testRoleRight(){
		RoleDAO roleDAO = new RoleDAO();
		Role role = roleDAO.findById(1);
		RightDAO rightDAO = new RightDAO();
		List allRights = rightDAO.findAll();
		Transaction tx = null;
		try{
			tx = HibernateSessionFactory.getSession().beginTransaction();
			if ( role.getRights().containsAll(allRights)) System.out.println("True");
//			roleDAO.save(role);
//			short pri = 20;
//			role.setHighestPri(pri);
//			tx.commit();
		}catch(Exception e){
			if (tx != null) 
			{
				tx.rollback();
			}			
		}finally
		{
			HibernateSessionFactory.closeSession();
		}
	}
//	@Test
	public void testRoleRight2(){
		RoleDAO roleDAO = new RoleDAO();
		Role role = roleDAO.findById(2);
		RightDAO rightDAO = new RightDAO();
		List allRights = rightDAO.findAll();
		Transaction tx = null;
		try{
			tx = HibernateSessionFactory.getSession().beginTransaction();
			if ( role.getRights().containsAll(allRights)){
				System.out.println("True");
			}
			else{
				role.getRights().addAll(allRights);
			}
			roleDAO.save(role);
			short pri = 11;
			role.setHighestPri(pri);
			tx.commit();
		}catch(Exception e){
			if (tx != null) 
			{
				tx.rollback();
			}			
		}finally
		{
			HibernateSessionFactory.closeSession();
		}
	}
	@Test
	public void testDeleteRole(){
		RoleDAO roleDAO = new RoleDAO();
		Role role = roleDAO.findById(3);
		Transaction tx = null;
		try{
			tx = HibernateSessionFactory.getSession().beginTransaction();
			roleDAO.delete(role);
			tx.commit();
		}catch(Exception e){
			if(tx != null){
				tx.rollback();
			}
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
}
