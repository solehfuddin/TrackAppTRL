package com.sofudev.trackapptrl.Data;

public class Data_item_lensstock {
    private int qty;
    private String lensname, lenscode, lensuom;

    public String getLensuom() {
        return lensuom;
    }

    public void setLensuom(String lensuom) {
        this.lensuom = lensuom;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getLensname() {
        return lensname;
    }

    public void setLensname(String lensname) {
        this.lensname = lensname;
    }

    public String getLenscode() {
        return lenscode;
    }

    public void setLenscode(String lenscode) {
        this.lenscode = lenscode;
    }
}
