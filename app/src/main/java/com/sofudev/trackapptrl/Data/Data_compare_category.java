package com.sofudev.trackapptrl.Data;

public class Data_compare_category {
    String id, title;
    private boolean checked = false;

    public Data_compare_category() {
    }

    public Data_compare_category(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
}
