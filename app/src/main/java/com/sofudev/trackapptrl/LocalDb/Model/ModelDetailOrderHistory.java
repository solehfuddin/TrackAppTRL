package com.sofudev.trackapptrl.LocalDb.Model;

/**
 * Created by sholeh on 17/12/2018.
 */

public class ModelDetailOrderHistory {
    String orderNumber, itemCode, description, power, uom, qty, price, total, margin, extraMargin;

    public ModelDetailOrderHistory() {
    }

    public ModelDetailOrderHistory(String orderNumber, String itemCode, String description, String power, String uom, String qty, String price, String total, String margin, String extraMargin) {
        this.orderNumber = orderNumber;
        this.itemCode = itemCode;
        this.description = description;
        this.power = power;
        this.uom = uom;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.margin = margin;
        this.extraMargin = extraMargin;
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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getExtraMargin() {
        return extraMargin;
    }

    public void setExtraMargin(String extraMargin) {
        this.extraMargin = extraMargin;
    }
}
