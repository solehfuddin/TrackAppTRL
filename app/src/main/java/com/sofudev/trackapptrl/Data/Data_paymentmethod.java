package com.sofudev.trackapptrl.Data;

/**
 * Created by sholeh on 26/12/2018.
 */

public class Data_paymentmethod {
    String paymentImage, paymentMethod;

    public Data_paymentmethod(String paymentImage, String paymentMethod) {
        this.paymentImage = paymentImage;
        this.paymentMethod = paymentMethod;
    }

    public Data_paymentmethod() {
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
