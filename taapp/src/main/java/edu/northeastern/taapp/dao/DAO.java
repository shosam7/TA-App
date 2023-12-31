package edu.northeastern.taapp.dao;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import edu.northeastern.taapp.config.HibernateConfig;

public abstract class DAO {
	private static final SessionFactory sessionFactory = HibernateConfig.buildSessionFactory();
	private static final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();

	private static final Logger log = Logger.getAnonymousLogger();

	protected DAO() {
	}

	public static Session getSession() {
		Session session = (Session) DAO.sessionThread.get();

		if (session == null) {
			session = sessionFactory.openSession();
			DAO.sessionThread.set(session);
		}
		return session;
	}

	protected void begin() {
		getSession().beginTransaction();
	}

	protected void commit() {
		getSession().getTransaction().commit();
	}

	protected void rollback() {
		try {
			getSession().getTransaction().rollback();
		} catch (HibernateException e) {
			log.log(Level.WARNING, "Cannot rollback", e);
		}
		try {
			getSession().close();
		} catch (HibernateException e) {
			log.log(Level.WARNING, "Cannot close", e);
		}
		DAO.sessionThread.set(null);
	}

	public static void close() {
		getSession().close();
		DAO.sessionThread.set(null);
	}
}
