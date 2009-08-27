package test.dao;

import java.util.Date;

import org.hibernate.Transaction;
import org.junit.Test;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Operatelog;
import com.webrender.dao.OperatelogDAO;
import com.webrender.dao.Reguser;
import com.webrender.dao.ReguserDAO;

public class TestOperatelogDAO {
	@Test
	public void testSave(){
		
		ReguserDAO regUserDAO = new ReguserDAO();
		Reguser regUser = regUserDAO.findById(1);
		Transaction tx = HibernateSessionFactory.getSession().beginTransaction();
		OperatelogDAO operateLogDAO = new OperatelogDAO();
		Operatelog transientInstance = new Operatelog(regUser,Operatelog.ADD,new Date());
		transientInstance.setOperateInformation("111");
		operateLogDAO.save(transientInstance);
		tx.commit();
		
	}
}
