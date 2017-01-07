package com.hotmail.dimmaryanto.software.belajar.model;

/**
 * Created by dimmaryanto93 on 07/01/17.
 */
public enum JenisKelamin {

    LAKI_LAKI("Laki Laki"),
    PEREMPUAN("Perempuan");

    private String text;

    JenisKelamin(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
