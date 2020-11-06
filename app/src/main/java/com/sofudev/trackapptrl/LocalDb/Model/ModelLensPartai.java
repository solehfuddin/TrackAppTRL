package com.sofudev.trackapptrl.LocalDb.Model;

public class ModelLensPartai {
    private int productId, productPrice, productQty, productStock, productWeight,
                productDiscSale;
    private double productDisc, productDiscPrice, newProductDiscPrice, productDiscPriceSale, newProductPrice;
    private String productCode;
    private String productDesc;
    private String powerSph;
    private String powerCyl;
    private String powerAdd;
    private String productSide;
    private String productImage;

    public double getProductDiscPriceSale() {
        return productDiscPriceSale;
    }

    public void setProductDiscPriceSale(double productDiscPriceSale) {
        this.productDiscPriceSale = productDiscPriceSale;
    }

    public int getProductDiscSale() {
        return productDiscSale;
    }

    public void setProductDiscSale(int productDiscSale) {
        this.productDiscSale = productDiscSale;
    }

    public String getProductTitleSale() {
        return productTitleSale;
    }

    public void setProductTitleSale(String productTitleSale) {
        this.productTitleSale = productTitleSale;
    }

    private String productTitleSale;

    public ModelLensPartai() {
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

    public double getProductDiscPrice() {
        return productDiscPrice;
    }

    public void setProductDiscPrice(double productDiscPrice) {
        this.productDiscPrice = productDiscPrice;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public double getProductDisc() {
        return productDisc;
    }

    public void setProductDisc(double productDisc) {
        this.productDisc = productDisc;
    }

    public double getNewProductPrice() {
        return newProductPrice;
    }

    public void setNewProductPrice(double newProductPrice) {
        this.newProductPrice = newProductPrice;
    }

    public double getNewProductDiscPrice() {
        return newProductDiscPrice;
    }

    public void setNewProductDiscPrice(double newProductDiscPrice) {
        this.newProductDiscPrice = newProductDiscPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getPowerSph() {
        return powerSph;
    }

    public void setPowerSph(String powerSph) {
        this.powerSph = powerSph;
    }

    public String getPowerCyl() {
        return powerCyl;
    }

    public void setPowerCyl(String powerCyl) {
        this.powerCyl = powerCyl;
    }

    public String getPowerAdd() {
        return powerAdd;
    }

    public void setPowerAdd(String powerAdd) {
        this.powerAdd = powerAdd;
    }

    public String getProductSide() {
        return productSide;
    }

    public void setProductSide(String productSide) {
        this.productSide = productSide;
    }
}
