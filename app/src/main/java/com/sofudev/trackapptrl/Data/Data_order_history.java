package com.sofudev.trackapptrl.Data;

/**
 * Created by Fuddins on 21/12/2017.
 */

public class Data_order_history {
    private String custname, datetime, lensname, status, statusdesc;

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLensname() {
        return lensname;
    }

    public void setLensname(String lensname) {
        this.lensname = lensname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }
}
