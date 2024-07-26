package com.sofudev.trackapptrl.Data;

public class Data_assignbin_line {
    private String itemId, itemQty, itemCode, itemCategory, itemName, itemDesc, itemOrg, itemNote, totalQty;

    public Data_assignbin_line() {
    }

    public Data_assignbin_line(String itemId, String itemQty, String itemCode, String itemCategory, String itemName, String itemDesc, String itemOrg, String itemNote, String totalQty) {
        this.itemId = itemId;
        this.itemQty = itemQty;
        this.itemCode = itemCode;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemOrg = itemOrg;
        this.itemNote = itemNote;
        this.totalQty = totalQty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemOrg() {
        return itemOrg;
    }

    public void setItemOrg(String itemOrg) {
        this.itemOrg = itemOrg;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }
}
