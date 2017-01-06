package com.hotmail.dimmaryanto.software.belajar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class App {

    public static void main(String[] args) {
        HibernateFactory factory = new HibernateFactory();
        SessionFactory sessionFactory = factory.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.close();
        sessionFactory.close();
    }
}
