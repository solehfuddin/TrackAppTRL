package com.sofudev.trackapptrl.Data;

public class Data_frame_header {
    private int idParty, shippingId, shippingPrice;
    private double totalPrice;
    private String orderId, shippingName, shippingService, opticCity, opticProvince, opticName, opticAddress, paymentCashCarry, customDisc;

    public Data_frame_header() {
    }

    public String getShippingService() {
        return shippingService;
    }

    public void setShippingService(String shippingService) {
        this.shippingService = shippingService;
    }

    public String getCustomDisc() {
        return customDisc;
    }

    public void setCustomDisc(String customDisc) {
        this.customDisc = customDisc;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
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
