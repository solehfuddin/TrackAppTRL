package com.sofudev.trackapptrl.Data;

/**
 * Created by sholeh on 8/27/2019.
 */

public class Data_searchnow {
    private int id;
    private String keyword;

    public Data_searchnow(int id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public Data_searchnow() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
