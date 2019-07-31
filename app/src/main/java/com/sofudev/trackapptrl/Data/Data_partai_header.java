package com.sofudev.trackapptrl.Data;

public class Data_partai_header {
    private String orderNumber, opticName, opticAddress, opticCity, phoneNumber, shippingName, opticProvince, payment_cashcarry;
    private int idParty, shippingId, shippingPrice, totalPrice;

    public String getPayment_cashcarry() {
        return payment_cashcarry;
    }

    public void setPayment_cashcarry(String payment_cashcarry) {
        this.payment_cashcarry = payment_cashcarry;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getOpticCity() {
        return opticCity;
    }

    public void setOpticCity(String opticCity) {
        this.opticCity = opticCity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getOpticProvince() {
        return opticProvince;
    }

    public void setOpticProvince(String opticProvince) {
        this.opticProvince = opticProvince;
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
}
