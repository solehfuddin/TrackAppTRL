package com.sofudev.trackapptrl.Data;

public class Data_filter_estatement {
    private String filtername, username, blnAwal, blnAkhir, thn, divisi;

    public Data_filter_estatement() {
    }

    public Data_filter_estatement(String filtername, String username, String blnAwal, String blnAkhir, String thn, String divisi) {
        this.filtername = filtername;
        this.username = username;
        this.blnAwal = blnAwal;
        this.blnAkhir = blnAkhir;
        this.thn = thn;
        this.divisi = divisi;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBlnAwal() {
        return blnAwal;
    }

    public void setBlnAwal(String blnAwal) {
        this.blnAwal = blnAwal;
    }

    public String getBlnAkhir() {
        return blnAkhir;
    }

    public void setBlnAkhir(String blnAkhir) {
        this.blnAkhir = blnAkhir;
    }

    public String getThn() {
        return thn;
    }

    public void setThn(String thn) {
        this.thn = thn;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }
}
