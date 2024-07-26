package com.sofudev.trackapptrl.Data;

public class Data_notification_center {
    private String title, username, level, idparty, custname;
    private int categoryType;

    public Data_notification_center(String title, String username, String level, String idparty, String custname, int categoryType) {
        this.title = title;
        this.username = username;
        this.level = level;
        this.idparty = idparty;
        this.custname = custname;
        this.categoryType = categoryType;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIdparty() {
        return idparty;
    }

    public void setIdparty(String idparty) {
        this.idparty = idparty;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }
}
