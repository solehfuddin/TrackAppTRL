package com.sofudev.trackapptrl.Data;

public class Data_filter_deliverytrack {
    String title, username, condition, mytoken, tikitoken;

    public Data_filter_deliverytrack(String title, String username, String condition, String mytoken, String tikitoken) {
        this.title = title;
        this.username = username;
        this.condition = condition;
        this.mytoken = mytoken;
        this.tikitoken = tikitoken;
    }

    public String getTikitoken() {
        return tikitoken;
    }

    public void setTikitoken(String tikitoken) {
        this.tikitoken = tikitoken;
    }

    public String getMytoken() {
        return mytoken;
    }

    public void setMytoken(String mytoken) {
        this.mytoken = mytoken;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
