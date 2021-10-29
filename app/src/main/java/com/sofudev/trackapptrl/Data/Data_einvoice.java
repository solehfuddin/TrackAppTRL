package com.sofudev.trackapptrl.Data;

public class Data_einvoice {
    String inv_category, inv_number, inv_date, sales_name;
    int inv_totalprice;

    public Data_einvoice() {
    }

    public String getInv_category() {
        return inv_category;
    }

    public void setInv_category(String inv_category) {
        this.inv_category = inv_category;
    }

    public String getInv_number() {
        return inv_number;
    }

    public void setInv_number(String inv_number) {
        this.inv_number = inv_number;
    }

    public String getInv_date() {
        return inv_date;
    }

    public void setInv_date(String inv_date) {
        this.inv_date = inv_date;
    }

    public String getSales_name() {
        return sales_name;
    }

    public void setSales_name(String sales_name) {
        this.sales_name = sales_name;
    }

    public int getInv_totalprice() {
        return inv_totalprice;
    }

    public void setInv_totalprice(int inv_totalprice) {
        this.inv_totalprice = inv_totalprice;
    }
}
