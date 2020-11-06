package com.sofudev.trackapptrl.Data;

public class Data_item_orderdetail {
    private int no, jumlah, harga, diskonFlashSale, tinting, totalAll, category;
    private double diskon;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    private String item_code, deskripsi, titleFlashSale;

    public int getDiskonFlashSale() {
        return diskonFlashSale;
    }

    public void setDiskonFlashSale(int diskonFlashSale) {
        this.diskonFlashSale = diskonFlashSale;
    }

    public String getTitleFlashSale() {
        return titleFlashSale;
    }

    public void setTitleFlashSale(String titleFlashSale) {
        this.titleFlashSale = titleFlashSale;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }

    public int getTinting() {
        return tinting;
    }

    public void setTinting(int tinting) {
        this.tinting = tinting;
    }

    public int getTotalAll() {
        return totalAll;
    }

    public void setTotalAll(int totalAll) {
        this.totalAll = totalAll;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
