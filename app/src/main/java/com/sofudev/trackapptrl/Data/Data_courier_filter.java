package com.sofudev.trackapptrl.Data;

public class Data_courier_filter {
    String title, username;
    String stDate, edDate;
    int condition = 0;

    public Data_courier_filter(String title, String username, String stDate, String edDate, int condition) {
        this.title      = title;
        this.username   = username;
        this.stDate     = stDate;
        this.edDate     = edDate;
        this.condition  = condition;
    }

    public String getStDate() {
        return stDate;
    }

    public void setStDate(String stDate) {
        this.stDate = stDate;
    }

    public String getEdDate() {
        return edDate;
    }

    public void setEdDate(String edDate) {
        this.edDate = edDate;
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

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
