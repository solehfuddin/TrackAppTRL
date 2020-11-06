package com.sofudev.trackapptrl.Data;

public class Data_orderhistory_optic {
    private String tanggalOrder, nomorOrder, totalBiaya, statusOrder, iconOrder, namaPasien, paymentType;

    public Data_orderhistory_optic() {
    }

    public Data_orderhistory_optic(String tanggalOrder, String nomorOrder, String totalBiaya, String statusOrder, String iconOrder, String namaPasien, String paymentType) {
        this.tanggalOrder = tanggalOrder;
        this.nomorOrder = nomorOrder;
        this.totalBiaya = totalBiaya;
        this.statusOrder = statusOrder;
        this.iconOrder = iconOrder;
        this.namaPasien = namaPasien;
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getTanggalOrder() {
        return tanggalOrder;
    }

    public void setTanggalOrder(String tanggalOrder) {
        this.tanggalOrder = tanggalOrder;
    }

    public String getNomorOrder() {
        return nomorOrder;
    }

    public void setNomorOrder(String nomorOrder) {
        this.nomorOrder = nomorOrder;
    }

    public String getTotalBiaya() {
        return totalBiaya;
    }

    public void setTotalBiaya(String totalBiaya) {
        this.totalBiaya = totalBiaya;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getIconOrder() {
        return iconOrder;
    }

    public void setIconOrder(String iconOrder) {
        this.iconOrder = iconOrder;
    }
}
