package com.covoiturage.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory buildSessionFactory() {
        try {
            System.out.println("Attempting to build SessionFactory...");
            Configuration configuration = new Configuration();

            // Chargement explicite du fichier de configuration
            configuration.configure("hibernate.cfg.xml");
            System.out.println("Hibernate configuration loaded.");

            // Charger les informations de connexion à partir des variables d'environnement
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbUrl != null && !dbUrl.isEmpty()) {
                System.out.println("Using DB_URL from environment: " + dbUrl);
                configuration.setProperty("hibernate.connection.url", dbUrl);
            } else {
                System.out.println("Using DB_URL from hibernate.cfg.xml");
            }
            if (dbUser != null && !dbUser.isEmpty()) {
                System.out.println("Using DB_USER from environment");
                configuration.setProperty("hibernate.connection.username", dbUser);
            } else {
                System.out.println("Using DB_USER from hibernate.cfg.xml");
            }
            if (dbPassword != null && !dbPassword.isEmpty()) {
                System.out.println("Using DB_PASSWORD from environment");
                configuration.setProperty("hibernate.connection.password", dbPassword);
            } else {
                System.out.println("Using DB_PASSWORD from hibernate.cfg.xml");
            }

            // Afficher les propriétés de configuration
            System.out.println("Hibernate configuration properties:");
            configuration.getProperties().forEach((key, value) ->
                    System.out.println(key + " : " + (key.toString().toLowerCase().contains("password") ? "****" : value)));

            SessionFactory factory = configuration.buildSessionFactory();
            System.out.println("SessionFactory created successfully");
            return factory;
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed.");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            System.out.println("SessionFactory is null, attempting to rebuild...");
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            System.out.println("Closing SessionFactory...");
            getSessionFactory().close();
            System.out.println("SessionFactory closed.");
        }
    }

}