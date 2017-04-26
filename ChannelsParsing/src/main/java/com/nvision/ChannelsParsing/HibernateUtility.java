package com.nvision.ChannelsParsing;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

public class HibernateUtility {

	public static SessionFactory factory;

	private HibernateUtility() {
	}

	public static synchronized SessionFactory getSessionFactory(String dbPath, String dbName, String userName, String password)
			throws ServiceException {

		if (factory == null) {
			Configuration cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			cfg.getProperties().setProperty("hibernate.connection.url", "jdbc:mysql://" + dbPath + "/" + dbName);
			cfg.getProperties().setProperty("hibernate.connection.password", userName);
			cfg.getProperties().setProperty("hibernate.connection.username", password);
			factory = cfg.buildSessionFactory();
		}

		return factory;

	}

}
