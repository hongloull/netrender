package com.webrender.test;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.webrender.dao.HibernateSessionFactory;
import com.webrender.dao.Node;
import com.webrender.dao.NodeDAO;
import com.webrender.dao.Nodegroup;
import com.webrender.dao.Reguser;

public class ManyToMany {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("begin");
		Nodegroup nodegroup1 = new Nodegroup();
		nodegroup1.setNodeGroupId(1);
//		Nodegroup nodegroup2 = new Nodegroup("组2");
//		Node node1 = new Node();
//		Node node2 = new Node("节点2","172.16.20.202");
		NodeDAO nodeDAO = new NodeDAO();
		Node node1 = nodeDAO.findById(3);
		
		nodegroup1.getNodes().add(node1);
//		nodegroup1.getNodes().add(node2);
		
		
		try{
			Session session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction(); 
			session.saveOrUpdate(nodegroup1);
//			session.save(nodegroup2);
//			session.save(node1);
//			session.save(node2);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}

}
