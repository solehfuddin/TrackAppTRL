package com.sofudev.trackapptrl.Data;

public class Data_notification_item {
    private String id, username, title, message, image, type, redirectTo, isClick, waktu;

    public Data_notification_item() {
    }

    public Data_notification_item(String id, String username, String title, String message, String image, String type, String redirectTo, String isClick, String waktu) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.message = message;
        this.image = image;
        this.type = type;
        this.redirectTo = redirectTo;
        this.isClick = isClick;
        this.waktu = waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public String getIsClick() {
        return isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
