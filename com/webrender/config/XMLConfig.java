package com.webrender.config;

import java.io.File;

import org.hibernate.Transaction;
import org.jdom.JDOMException;

import com.webrender.dao.HibernateSessionFactory;

public abstract class  XMLConfig {
	static Transaction getTransaction() {
		return HibernateSessionFactory.getSession().beginTransaction();
	}
	public abstract void loadFromXML(File file) throws JDOMException;
	public abstract void deleteExtraData();
//	public abstract void saveToXML(File file);
}
