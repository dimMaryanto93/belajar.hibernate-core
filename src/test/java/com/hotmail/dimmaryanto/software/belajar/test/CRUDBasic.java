package com.hotmail.dimmaryanto.software.belajar.test;

import com.hotmail.dimmaryanto.software.belajar.HibernateFactory;
import com.hotmail.dimmaryanto.software.belajar.model.Negara;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Ignore;
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
    @Ignore
    public void testOpeningSession() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.close();
    }

    @Test
    @Ignore
    public void testSimpanNegaraIndonesia() {
        // instance new object
        Negara indonesia = new Negara();
        indonesia.setArea(62);
        indonesia.setKode("INA");
        indonesia.setNama("Indonesia");

        // open connection
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // save
        session.save(indonesia);
        // commite transaction
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testUpdateNegaraIndonesia() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        // find negara by id
        Negara indonesia = session.get(Negara.class, 62);
        assertEquals("INA", indonesia.getKode());

        indonesia.setKode("IND");
        session.update(indonesia);

        indonesia = session.get(Negara.class, 62);
        assertEquals("IND", indonesia.getKode());
        session.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        sessionFactory.close();
    }
}
