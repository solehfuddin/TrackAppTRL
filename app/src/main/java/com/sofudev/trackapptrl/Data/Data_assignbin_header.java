package com.sofudev.trackapptrl.Data;

public class Data_assignbin_header {
    private String assignId, assignDate, assignSales, assignBy, assignFlag, totalRow;

    public Data_assignbin_header() {
    }

    public Data_assignbin_header(String assignId, String assignDate, String assignSales, String assignBy, String assignFlag, String totalRow) {
        this.assignId = assignId;
        this.assignDate = assignDate;
        this.assignSales = assignSales;
        this.assignBy = assignBy;
        this.assignFlag = assignFlag;
        this.totalRow = totalRow;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getAssignSales() {
        return assignSales;
    }

    public void setAssignSales(String assignSales) {
        this.assignSales = assignSales;
    }

    public String getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(String assignBy) {
        this.assignBy = assignBy;
    }

    public String getAssignFlag() {
        return assignFlag;
    }

    public void setAssignFlag(String assignFlag) {
        this.assignFlag = assignFlag;
    }

    public String getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(String totalRow) {
        this.totalRow = totalRow;
    }
}
