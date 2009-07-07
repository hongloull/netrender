package com.webrender.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.webrender.config.GenericConfig;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution.  Follows the Thread Local Session
 * pattern, see {@link http://hibernate.org/42.html }.
 */
public class HibernateSessionFactory {

    /** 
     * Location of hibernate.cfg.xml file.
     * Location should be on the classpath as Hibernate uses  
     * #resourceAsStream style lookup for its configuration file. 
     * The default classpath location of the hibernate config file is 
     * in the default package. Use #setConfigFile() to update 
     * the location of the configuration file for the current session.   
     */
//	private static final Log log = LogFactory.getLog(HibernateSessionFactory.class);
    private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private  static Configuration configuration = new Configuration();    
    private static org.hibernate.SessionFactory sessionFactory;
    private static String configFile = CONFIG_FILE_LOCATION;

	static {
    	try {
    		
			configuration.configure(configFile);
			String databaseServer = GenericConfig.getInstance().getDatabaseServer();
			configuration.setProperty("hibernate.connection.url", "jdbc:mysql://"+databaseServer+":3306/distributedmanagement?useUnicode=true&characterEncoding=UTF-8");
			sessionFactory = configuration.buildSessionFactory();
//			sessionFactory.getStatistics().setStatisticsEnabled(true);
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
    }
    private HibernateSessionFactory() {
    }
	
	/**
     * Returns the ThreadLocal Session instance.  Lazy initialize
     * the <code>SessionFactory</code> if needed.
     *
     *  @return Session
     *  @throws HibernateException
     */
    public static Session getSession() throws HibernateException {
//    	log.debug("getSession");
        Session session = (Session) threadLocal.get();
     //   System.out.println("OPEN            CLOSE");
      //  System.out.println( sessionFactory.getStatistics().getSessionCloseCount()+"         "+ sessionFactory.getStatistics().getSessionOpenCount());
        
		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
//			log.info("getSession  session close threadLocal.set(session)");
			threadLocal.set(session);
		}
//		log.debug("getSession success");
        return session;
    }

	/**
     *  Rebuild hibernate session factory
     *
     */
	public static void rebuildSessionFactory() {
		try {
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
			
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	/**
     *  Close the single hibernate session instance.
     *
     *  @throws HibernateException
     */
    public static void closeSession() throws HibernateException {
//    	log.debug("closeSession");
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
//        	log.info("closeSession session.close");
            session.close();
        }
//        log.debug("closeSession success");
    }

	/**
     *  return session factory
     *
     */
	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
     *  return session factory
     *
     *	session factory will be rebuilded in the next call
     */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
     *  return hibernate configuration
     *
     */
	public static Configuration getConfiguration() {
		return configuration;
	}

}