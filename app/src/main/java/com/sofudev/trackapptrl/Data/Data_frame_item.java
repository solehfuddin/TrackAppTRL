package com.sofudev.trackapptrl.Data;

public class Data_frame_item {
    private int lineNumber, frameId, frameQty, frameRealPrice, frameDisc, frameDiscPrice;
    private String orderId, frameCode, frameName;

    public Data_frame_item() {
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public int getFrameQty() {
        return frameQty;
    }

    public void setFrameQty(int frameQty) {
        this.frameQty = frameQty;
    }

    public int getFrameRealPrice() {
        return frameRealPrice;
    }

    public void setFrameRealPrice(int frameRealPrice) {
        this.frameRealPrice = frameRealPrice;
    }

    public int getFrameDisc() {
        return frameDisc;
    }

    public void setFrameDisc(int frameDisc) {
        this.frameDisc = frameDisc;
    }

    public int getFrameDiscPrice() {
        return frameDiscPrice;
    }

    public void setFrameDiscPrice(int frameDiscPrice) {
        this.frameDiscPrice = frameDiscPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFrameCode() {
        return frameCode;
    }

    public void setFrameCode(String frameCode) {
        this.frameCode = frameCode;
    }

    public String getFrameName() {
        return frameName;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }
}
