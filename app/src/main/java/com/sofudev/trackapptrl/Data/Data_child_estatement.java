package com.sofudev.trackapptrl.Data;

public class Data_child_estatement {
    private String childInvoice, childTanggal, childHarga;

    public Data_child_estatement() {
    }

    public Data_child_estatement(String childInvoice, String childTanggal, String childHarga) {
        this.childInvoice = childInvoice;
        this.childTanggal = childTanggal;
        this.childHarga = childHarga;
    }

    public String getChildInvoice() {
        return childInvoice;
    }

    public void setChildInvoice(String childInvoice) {
        this.childInvoice = childInvoice;
    }

    public String getChildTanggal() {
        return childTanggal;
    }

    public void setChildTanggal(String childTanggal) {
        this.childTanggal = childTanggal;
    }

    public String getChildHarga() {
        return childHarga;
    }

    public void setChildHarga(String childHarga) {
        this.childHarga = childHarga;
    }
}

