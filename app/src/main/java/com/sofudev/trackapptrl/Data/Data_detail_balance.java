package com.sofudev.trackapptrl.Data;

/**
 * Created by sholeh on 6/18/2019.
 */

public class Data_detail_balance {
    String tanggal, deskripsi;
    int nominal;

    public Data_detail_balance() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
