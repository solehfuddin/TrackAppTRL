package com.sofudev.trackapptrl.Data;

/**
 * Created by sholeh on 5/3/2019.
 */

public class Data_color_filter {
    private int colorId;
    private String colorCode, colorName;

    public Data_color_filter(int colorId, String colorCode, String colorName) {
        this.colorId = colorId;
        this.colorCode = colorCode;
        this.colorName = colorName;
    }

    public Data_color_filter() {
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
