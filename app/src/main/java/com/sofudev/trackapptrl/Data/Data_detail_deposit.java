package com.sofudev.trackapptrl.Data;

/**
 * Created by sholeh on 30/06/2020.
 */

public class Data_detail_deposit {
    private int id_mutasi, saldo, id_admin;
    private String ship_number, id_transaksi, bank_name, jenis_pembayaran, keterangan, insert_date, process_by;

    public Data_detail_deposit() {
    }

    public int getId_mutasi() {
        return id_mutasi;
    }

    public void setId_mutasi(int id_mutasi) {
        this.id_mutasi = id_mutasi;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getShip_number() {
        return ship_number;
    }

    public void setShip_number(String ship_number) {
        this.ship_number = ship_number;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getJenis_pembayaran() {
        return jenis_pembayaran;
    }

    public void setJenis_pembayaran(String jenis_pembayaran) {
        this.jenis_pembayaran = jenis_pembayaran;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getProcess_by() {
        return process_by;
    }

    public void setProcess_by(String process_by) {
        this.process_by = process_by;
    }
}
