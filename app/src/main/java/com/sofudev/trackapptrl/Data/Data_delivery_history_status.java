package com.sofudev.trackapptrl.Data;

public class Data_delivery_history_status {
    String tanggal, status, branch_id, branch, status_by, status_note;

    public Data_delivery_history_status() {
    }

    public Data_delivery_history_status(String tanggal, String status, String branch_id, String branch, String status_by, String status_note) {
        this.tanggal = tanggal;
        this.status = status;
        this.branch_id = branch_id;
        this.branch = branch;
        this.status_by = status_by;
        this.status_note = status_note;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus_by() {
        return status_by;
    }

    public void setStatus_by(String status_by) {
        this.status_by = status_by;
    }

    public String getStatus_note() {
        return status_note;
    }

    public void setStatus_note(String status_note) {
        this.status_note = status_note;
    }
}
