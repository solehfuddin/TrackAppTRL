package com.sofudev.trackapptrl.Data;

/**
 * Created by sholeh on 5/3/2019.
 */

public class Data_brand_filter {
    private int brandId;
    private String brandName, brandPrefix;

    public Data_brand_filter() {
    }

    public Data_brand_filter(int brandId, String brandName, String brandPrefix) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandPrefix = brandPrefix;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandPrefix() {
        return brandPrefix;
    }

    public void setBrandPrefix(String brandPrefix) {
        this.brandPrefix = brandPrefix;
    }
}
