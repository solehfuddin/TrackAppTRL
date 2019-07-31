package com.sofudev.trackapptrl.LocalDb.Model;

public class ModelWishlist {
    private String productcode, productname, productimg;
    private Integer productid, productprice, productdiscount;

    public ModelWishlist() {
    }

    public ModelWishlist(Integer productid, String productcode, String productname, String productimg, Integer productprice, Integer productdiscount) {
        this.productcode = productcode;
        this.productname = productname;
        this.productimg = productimg;
        this.productprice = productprice;
        this.productdiscount = productdiscount;
        this.productid = productid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductimg() {
        return productimg;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public Integer getProductprice() {
        return productprice;
    }

    public void setProductprice(Integer productprice) {
        this.productprice = productprice;
    }

    public Integer getProductdiscount() {
        return productdiscount;
    }

    public void setProductdiscount(Integer productdiscount) {
        this.productdiscount = productdiscount;
    }
}
