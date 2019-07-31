package com.sofudev.trackapptrl.Data;

/**
 * Created by Fuddins on 28/09/2017.
 */

public class Data_uac {
    private String title, image, status, username;

    public Data_uac() {

    }

    public Data_uac(String title, String image, String status, String username) {
        this.title = title;
        this.image = image;
        this.status = status;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
