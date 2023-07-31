package com.sofudev.trackapptrl.Data;

public class Data_news_category {
    String id, title, slug, type;

    public Data_news_category() {
    }

    public Data_news_category(String id, String title, String slug, String type) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
