package com.webrender.test;

import org.hibernate.Transaction;
import org.junit.Test;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.NodegroupDAO;

public class NodeGroupM2M {
	@Test
	public void testAddNodegroup(){
		NodegroupDAO nodeGroupDAO = new NodegroupDAO();
		NodeDAO nodeDAO = new NodeDAO();
		Nodegroup group = null; 
		group = nodeGroupDAO.findByNodeGroupName("Test20090715");
		if (group == null) group = new Nodegroup("Test20090715");
		Transaction tx = HibernateSessionFactory.getSession().beginTransaction();
		group.getNodes().addAll(nodeDAO.findAll());
		nodeGroupDAO.save(group);
		tx.commit();
		HibernateSessionFactory.closeSession();
	}
	
	@Test 
	public void testDelNodegroup(){
		NodegroupDAO nodeGroupDAO = new NodegroupDAO();
		Nodegroup group = nodeGroupDAO.findByNodeGroupName("Test20090715");
		if (group==null) return ;
		Transaction tx = HibernateSessionFactory.getSession().beginTransaction();
		nodeGroupDAO.delete(group);
		tx.commit();
		HibernateSessionFactory.closeSession();
	}
}
