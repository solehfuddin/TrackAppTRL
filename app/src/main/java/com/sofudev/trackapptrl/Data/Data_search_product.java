package com.sofudev.trackapptrl.Data;

public class Data_search_product {
    private int searchId;
    private String searchTitle;

    public Data_search_product() {
    }

    public Data_search_product(int searchId, String searchTitle) {
        this.searchId = searchId;
        this.searchTitle = searchTitle;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }
}
