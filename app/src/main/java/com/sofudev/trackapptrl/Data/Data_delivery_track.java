package com.sofudev.trackapptrl.Data;

public class Data_delivery_track {
    private String id, awbNumber, datePickup, opticName, serviceName, note, ekspedisi;

    public Data_delivery_track() {

    }

    public Data_delivery_track(String id, String awbNumber, String datePickup, String opticName, String serviceName, String note, String ekspedisi) {
        this.id = id;
        this.awbNumber = awbNumber;
        this.datePickup = datePickup;
        this.opticName = opticName;
        this.serviceName = serviceName;
        this.note = note;
        this.ekspedisi = ekspedisi;
    }

    public String getEkspedisi() {
        return ekspedisi;
    }

    public void setEkspedisi(String ekspedisi) {
        this.ekspedisi = ekspedisi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getDatePickup() {
        return datePickup;
    }

    public void setDatePickup(String datePickup) {
        this.datePickup = datePickup;
    }

    public String getOpticName() {
        return opticName;
    }

    public void setOpticName(String opticName) {
        this.opticName = opticName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
