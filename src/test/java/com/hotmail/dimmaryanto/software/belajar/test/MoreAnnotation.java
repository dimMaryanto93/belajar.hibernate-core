package com.hotmail.dimmaryanto.software.belajar.test;

import com.hotmail.dimmaryanto.software.belajar.HibernateFactory;
import com.hotmail.dimmaryanto.software.belajar.model.JenisKelamin;
import com.hotmail.dimmaryanto.software.belajar.model.Nasabah;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by dimmaryanto93 on 07/01/17.
 */
public class MoreAnnotation extends TestCase {

    private SessionFactory sessionFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.sessionFactory = new HibernateFactory().getSessionFactory();
    }

    @Test
    public void testSimpanNasabah() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Nasabah nasabah = new Nasabah();
        nasabah.setJenisKelamin(JenisKelamin.LAKI_LAKI);
        nasabah.setNamaDepan("Dimas");
        nasabah.setNamaBelakang("Maryanto");
        nasabah.setTanggalLahir(Date.valueOf(LocalDate.of(1993, 3, 28)));
        nasabah.setTempatLahir("Jl.Bukit indah no B8 kab.Bandung kec.Cileunyi");
        nasabah.setNamaIdentitas("Dimas Maryanto");
        nasabah.setBlacklist(false);

        session.save(nasabah);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testAmbilDataNasabah(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Nasabah nasabah = session.get(Nasabah.class, "540d869a-98a7-4ab4-8dcb-6bd6a65eb9b1");
        assertEquals(nasabah.getNamaLengkap(), nasabah.getNamaDepan().concat(nasabah.getNamaBelakang()));

        session.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.sessionFactory.close();
    }
}
