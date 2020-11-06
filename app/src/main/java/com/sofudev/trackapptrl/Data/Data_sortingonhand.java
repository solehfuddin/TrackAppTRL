package com.sofudev.trackapptrl.Data;

public class Data_sortingonhand {
    private String idSort, description;

    public Data_sortingonhand(String idSort, String description) {
        this.idSort = idSort;
        this.description = description;
    }

    public String getIdSort() {
        return idSort;
    }

    public void setIdSort(String idSort) {
        this.idSort = idSort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
