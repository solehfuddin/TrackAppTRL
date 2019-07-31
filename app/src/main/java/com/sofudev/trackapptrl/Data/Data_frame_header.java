package com.sofudev.trackapptrl.Data;

public class Data_frame_header {
    int idParty, shippingId, shippingPrice, totalPrice;
    String orderId, shippingName, opticCity, opticProvince, opticName, opticAddress, paymentCashCarry;

    public Data_frame_header() {
    }

    public int getIdParty() {
        return idParty;
    }

    public void setIdParty(int idParty) {
        this.idParty = idParty;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getOpticCity() {
        return opticCity;
    }

    public void setOpticCity(String opticCity) {
        this.opticCity = opticCity;
    }

    public String getOpticProvince() {
        return opticProvince;
    }

    public void setOpticProvince(String opticProvince) {
        this.opticProvince = opticProvince;
    }

    public String getOpticName() {
        return opticName;
    }

    public void setOpticName(String opticName) {
        this.opticName = opticName;
    }

    public String getOpticAddress() {
        return opticAddress;
    }

    public void setOpticAddress(String opticAddress) {
        this.opticAddress = opticAddress;
    }

    public String getPaymentCashCarry() {
        return paymentCashCarry;
    }

    public void setPaymentCashCarry(String paymentCashCarry) {
        this.paymentCashCarry = paymentCashCarry;
    }
}
