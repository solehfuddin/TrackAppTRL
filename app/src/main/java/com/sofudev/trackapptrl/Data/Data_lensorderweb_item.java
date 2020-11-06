package com.sofudev.trackapptrl.Data;


public class Data_lensorderweb_item {
    private String orderNumber, itemId, itemCode, description, rl, discountName;
    private int qty, unitStandardPrice, unitStandardWeight, discountSale, totalWeight, tintingPrice, amount;
    private double discount;

    public int getDiscountSale() {
        return discountSale;
    }

    public void setDiscountSale(int discountSale) {
        this.discountSale = discountSale;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUnitStandardPrice() {
        return unitStandardPrice;
    }

    public void setUnitStandardPrice(int unitStandardPrice) {
        this.unitStandardPrice = unitStandardPrice;
    }

    public int getUnitStandardWeight() {
        return unitStandardWeight;
    }

    public void setUnitStandardWeight(int unitStandardWeight) {
        this.unitStandardWeight = unitStandardWeight;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getTintingPrice() {
        return tintingPrice;
    }

    public void setTintingPrice(int tintingPrice) {
        this.tintingPrice = tintingPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
