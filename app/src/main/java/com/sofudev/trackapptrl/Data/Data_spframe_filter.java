package com.sofudev.trackapptrl.Data;

public class Data_spframe_filter {
    String username, title, salesarea;
    String stDate, edDate;
    int condition, level;

    public Data_spframe_filter(String username, String title, String salesarea, String stDate, String edDate, int condition, int level) {
        this.username = username;
        this.title = title;
        this.salesarea = salesarea;
        this.stDate = stDate;
        this.edDate = edDate;
        this.condition = condition;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalesarea() {
        return salesarea;
    }

    public void setSalesarea(String salesarea) {
        this.salesarea = salesarea;
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
}
