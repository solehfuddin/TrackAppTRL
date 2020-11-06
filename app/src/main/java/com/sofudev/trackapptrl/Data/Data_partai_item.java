package com.sofudev.trackapptrl.Data;

public class Data_partai_item {
    private String orderNumber, itemCode, description, side, discount_name, title_discflashsale;
    private int itemId, qty, price, disc_flashsale;
    private double discount, total_price;

    public String getTitle_discflashsale() {
        return title_discflashsale;
    }

    public void setTitle_discflashsale(String title_discflashsale) {
        this.title_discflashsale = title_discflashsale;
    }

    public int getDisc_flashsale() {
        return disc_flashsale;
    }

    public void setDisc_flashsale(int disc_flashsale) {
        this.disc_flashsale = disc_flashsale;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(String discount_name) {
        this.discount_name = discount_name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
