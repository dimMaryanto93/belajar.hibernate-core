package com.hotmail.dimmaryanto.software.belajar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by dimmaryanto93 on 07/01/17. <br>
 * simple class entity for hibernate. <br>
 * it will generate as tables in database same with <br>
 */
@Entity
public class Negara {

    @Id
    @Column(name = "no_area", nullable = false, unique = true, length = 3)
    private Integer area;

    @Column(name = "kode_negara", nullable = false, unique = true, length = 3)
    private String kode;

    @Column(name = "nama_negara", nullable = false)
    private String nama;

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
