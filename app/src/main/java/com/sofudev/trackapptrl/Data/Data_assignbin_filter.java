package com.sofudev.trackapptrl.Data;

public class Data_assignbin_filter {
    String username, title, custname, partysiteid, noinv;
    int condition;

    public Data_assignbin_filter(String username, String title, String custname, String partysiteid, String noinv, int condition) {
        this.username = username;
        this.title = title;
        this.custname = custname;
        this.partysiteid = partysiteid;
        this.noinv = noinv;
        this.condition = condition;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getPartysiteid() {
        return partysiteid;
    }

    public void setPartysiteid(String partysiteid) {
        this.partysiteid = partysiteid;
    }

    public String getNoinv() {
        return noinv;
    }

    public void setNoinv(String noinv) {
        this.noinv = noinv;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
