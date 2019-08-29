package com.sofudev.trackapptrl.Data;

public class Data_recent_view {
    private int id;
    private String image;

    public Data_recent_view() {
    }

    public Data_recent_view(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
