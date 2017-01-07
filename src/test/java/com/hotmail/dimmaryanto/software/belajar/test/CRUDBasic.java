package com.hotmail.dimmaryanto.software.belajar.test;

import com.hotmail.dimmaryanto.software.belajar.HibernateFactory;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

/**
 * Created by dimmaryanto93 on 07/01/17.
 */
public class CRUDBasic extends TestCase {

    private SessionFactory sessionFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        HibernateFactory hibernateFactory = new HibernateFactory();
        sessionFactory = hibernateFactory.getSessionFactory();
    }

    @Test
    public void testOpeningSession(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sessionFactory.close();
    }
}
