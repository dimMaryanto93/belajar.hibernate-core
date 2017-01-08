package com.hotmail.dimmaryanto.software.belajar.model;

import org.hibernate.Session;
import org.hibernate.annotations.*;
import org.hibernate.tuple.ValueGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by dimmaryanto93 on 07/01/17.
 */
@Entity
@Table(name = "m_nasabah", uniqueConstraints = {
        @UniqueConstraint(name = "unique_nasabah", columnNames = {
                "nama_depan", "nama_belakang", "tanggal_lahir_nasabah", "jenis_kelamin"
        })
})
public class Nasabah {

    //    set default value as constructor
    public Nasabah() {
        setBlacklist(false);
    }

    //    set default value as generator
    private static class TimeGenerator implements ValueGenerator<Timestamp> {
        @Override
        public Timestamp generateValue(Session session, Object o) {
            return Timestamp.valueOf(LocalDateTime.now());
        }
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "nomor_register_nasabah")
    private String noRegister;

    @Column(name = "waktu_register", updatable = false)
    @GeneratorType(type = TimeGenerator.class, when = GenerationTime.INSERT)
    private Timestamp waktuRegister;

    @Column(name = "nama_identitas_nasabah", nullable = false, length = 25)
    private String namaIdentitas;

    @Column(name = "nama_depan", nullable = false)
    private String namaDepan;

    @Column(name = "nama_belakang", nullable = false)
    private String namaBelakang;

    @Formula(value = "concat(nama_depan, ' ', nama_belakang)")
    private String namaLengkap;

    @Column(name = "diblacklist", nullable = false)
    private Boolean blacklist;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;

    @Column(name = "tempat_lahir")
    private String tempatLahir;

    @Column(name = "tanggal_lahir_nasabah", nullable = false)
    private Date tanggalLahir;

    public String getNoRegister() {
        return noRegister;
    }

    public void setNoRegister(String noRegister) {
        this.noRegister = noRegister;
    }

    public Timestamp getWaktuRegister() {
        return waktuRegister;
    }

    public void setWaktuRegister(Timestamp waktuRegister) {
        this.waktuRegister = waktuRegister;
    }

    public String getNamaIdentitas() {
        return namaIdentitas;
    }

    public void setNamaIdentitas(String namaIdentitas) {
        this.namaIdentitas = namaIdentitas;
    }

    public String getNamaDepan() {
        return namaDepan;
    }

    public void setNamaDepan(String namaDepan) {
        this.namaDepan = namaDepan;
    }

    public String getNamaBelakang() {
        return namaBelakang;
    }

    public void setNamaBelakang(String namaBelakang) {
        this.namaBelakang = namaBelakang;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public Boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Boolean blacklist) {
        this.blacklist = blacklist;
    }

    public JenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(JenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}

