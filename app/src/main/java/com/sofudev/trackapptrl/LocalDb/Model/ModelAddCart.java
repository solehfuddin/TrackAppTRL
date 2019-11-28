package com.sofudev.trackapptrl.LocalDb.Model;

public class ModelAddCart {
    private int productId, productQty, productPrice, productDiscPrice, productDisc, newProductPrice, newProductDiscPrice, productStock, productWeight;
    private String productName, productCode, productImage;

    public ModelAddCart() {
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public int getNewProductPrice() {
        return newProductPrice;
    }

    public void setNewProductPrice(int newProductPrice) {
        this.newProductPrice = newProductPrice;
    }

    public int getNewProductDiscPrice() {
        return newProductDiscPrice;
    }

    public void setNewProductDiscPrice(int newProductDiscPrice) {
        this.newProductDiscPrice = newProductDiscPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductDiscPrice() {
        return productDiscPrice;
    }

    public void setProductDiscPrice(int productDiscPrice) {
        this.productDiscPrice = productDiscPrice;
    }

    public int getProductDisc() {
        return productDisc;
    }

    public void setProductDisc(int productDisc) {
        this.productDisc = productDisc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
