package com.hotmail.dimmaryanto.software.belajar.test;

import com.hotmail.dimmaryanto.software.belajar.HibernateFactory;
import com.hotmail.dimmaryanto.software.belajar.model.JenisKelamin;
import com.hotmail.dimmaryanto.software.belajar.model.Nasabah;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dimmaryanto93 on 08/01/17.
 */
public class QueryWithHQL extends TestCase {

    private SessionFactory sessionFactory;
    private Logger console = LoggerFactory.getLogger(QueryWithHQL.class);

    @Override
    protected void setUp() throws Exception {
        this.sessionFactory = new HibernateFactory().getSessionFactory();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.sessionFactory.close();
    }

    @Test
    public void testFindAllNasabah() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Nasabah");
        List<Nasabah> listNasabah = query.getResultList();

        // jumlah nasabahnya
        assertEquals(listNasabah.size(), 5);
        session.close();
    }

    @Test
    public void testFindNasabahWhereJenisKelaminIsLakiLaki() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Nasabah where jenisKelamin = :kelamin");
        query.setParameter("kelamin", JenisKelamin.LAKI_LAKI);
        List<Nasabah> listNasabah = query.getResultList();

        // jumlah nasabahnya
        assertEquals(listNasabah.size(), 3);
        session.close();
    }

    @Test
    public void testFindNasabahWhereJenisKelaminIsLakiLakiAndTempatLahirIsBandung() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("" +
                "from Nasabah " +
                "where jenisKelamin = :kelamin and tempatLahir = :lahir");
        query.setParameter("kelamin", JenisKelamin.LAKI_LAKI);
        query.setParameter("lahir", "Bandung");
        List<Nasabah> listNasabah = query.getResultList();

        // jumlah nasabahnya
        assertEquals(listNasabah.size(), 2);
        session.close();
    }
}
