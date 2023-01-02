package com.sofudev.trackapptrl.Data;

public class Data_item_sp {
    private int lineItem, qty, defaulPrice, amount;
    private String itemId, itemCode, description, umoCode;
    private double discount;

    public int getLineItem() {
        return lineItem;
    }

    public void setLineItem(int lineItem) {
        this.lineItem = lineItem;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getDefaulPrice() {
        return defaulPrice;
    }

    public void setDefaulPrice(int defaulPrice) {
        this.defaulPrice = defaulPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getUmoCode() {
        return umoCode;
    }

    public void setUmoCode(String umoCode) {
        this.umoCode = umoCode;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
