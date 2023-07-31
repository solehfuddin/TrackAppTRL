package com.sofudev.trackapptrl.Data;

public class Data_compare {
    String id, title, slug, description, images, type, category, createDate;

    public Data_compare() {
    }

    public Data_compare(String id, String title, String slug, String description, String images, String type, String category, String createDate) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.images = images;
        this.type = type;
        this.category = category;
        this.createDate = createDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
