package com.sofudev.trackapptrl.LocalDb.Model;

public class ModelOrderHistory {
    public static final String TABLE_NAME = "tbl_orderHistory";
    public static final String REAL_TABLE = "tbl_realOrder";

    public static final String COLUMN_ORDERNUMBER       = "order_number";
    public static final String COLUMN_LENSDESCRIPTION   = "lens_description";
    public static final String COLUMN_LENSCATEGORY      = "lens_category";
    public static final String COLUMN_PRICELENS         = "price_lens";
    public static final String COLUMN_PRICEDISCOUNT     = "price_discount";
    public static final String COLUMN_PRICEFACET        = "price_facet";
    public static final String COLUMN_PRICETINTING      = "price_tinting";
    public static final String COLUMN_PRICESHIPMENT     = "price_shipment";
    public static final String COLUMN_PRICETOTAL        = "price_total";
    public static final String COLUMN_STATUS            = "status";
    public static final String COLUMN_WEIGHT            = "weight";
    public static final String COLUMN_DATE              = "date";

    private String order_number, lens_description, lens_category, status, date, info;
    private String price_lens, price_discount, price_facet, price_tinting, price_shipment, price_total, weight;

    //SQLITE QUERY
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                                                COLUMN_ORDERNUMBER  + " VARCHAR (50) PRIMARY KEY," +
                                                COLUMN_LENSDESCRIPTION + " VARCHAR (75)," +
                                                COLUMN_LENSCATEGORY + " VARCHAR (20)," +
                                                COLUMN_PRICELENS + " VARCHAR (20)," +
                                                COLUMN_PRICEDISCOUNT + " VARCHAR (20)," +
                                                COLUMN_PRICEFACET + " VARCHAR (20)," +
                                                COLUMN_PRICETINTING + " VARCHAR (20),"+
                                                COLUMN_PRICESHIPMENT + " VARCHAR (20)," +
                                                COLUMN_PRICETOTAL + " VARCHAR (20)," +
                                                COLUMN_STATUS + " VARCHAR (20)," +
                                                COLUMN_WEIGHT + " VARCHAR (20) ," +
                                                COLUMN_DATE + " VARCHAR (25)" + ")";

    public static final String CREATE_REALTABLE = "CREATE TABLE " + REAL_TABLE + "(" +
                                                    COLUMN_ORDERNUMBER  + " VARCHAR (50) PRIMARY KEY," +
                                                    COLUMN_LENSDESCRIPTION + " VARCHAR (75)," +
                                                    COLUMN_LENSCATEGORY + " VARCHAR (20)," +
                                                    COLUMN_PRICELENS + " VARCHAR (20)," +
                                                    COLUMN_PRICEDISCOUNT + " VARCHAR (20)," +
                                                    COLUMN_PRICEFACET + " VARCHAR (20)," +
                                                    COLUMN_PRICETINTING + " VARCHAR (20),"+
                                                    COLUMN_PRICESHIPMENT + " VARCHAR (20)," +
                                                    COLUMN_PRICETOTAL + " VARCHAR (20)," +
                                                    COLUMN_STATUS + " VARCHAR (20)," +
                                                    COLUMN_WEIGHT + " VARCHAR (20) ," +
                                                    COLUMN_DATE + " VARCHAR (25)" + ")";

    public ModelOrderHistory() {
    }

    public ModelOrderHistory(String info)
    {
        this.info = info;
    }

    public ModelOrderHistory(String order_number, String lens_description, String lens_category, String status, String date, String price_lens, String price_discount, String price_facet, String price_tinting, String price_shipment, String price_total, String weight) {
        this.order_number = order_number;
        this.lens_description = lens_description;
        this.lens_category = lens_category;
        this.status = status;
        this.date = date;
        this.price_lens = price_lens;
        this.price_discount = price_discount;
        this.price_facet = price_facet;
        this.price_tinting = price_tinting;
        this.price_shipment = price_shipment;
        this.price_total = price_total;
        this.weight = weight;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getLens_description() {
        return lens_description;
    }

    public void setLens_description(String lens_description) {
        this.lens_description = lens_description;
    }

    public String getLens_category() {
        return lens_category;
    }

    public void setLens_category(String lens_category) {
        this.lens_category = lens_category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice_lens() {
        return price_lens;
    }

    public void setPrice_lens(String price_lens) {
        this.price_lens = price_lens;
    }

    public String getPrice_discount() {
        return price_discount;
    }

    public void setPrice_discount(String price_discount) {
        this.price_discount = price_discount;
    }

    public String getPrice_facet() {
        return price_facet;
    }

    public void setPrice_facet(String price_facet) {
        this.price_facet = price_facet;
    }

    public String getPrice_tinting() {
        return price_tinting;
    }

    public void setPrice_tinting(String price_tinting) {
        this.price_tinting = price_tinting;
    }

    public String getPrice_shipment() {
        return price_shipment;
    }

    public void setPrice_shipment(String price_shipment) {
        this.price_shipment = price_shipment;
    }

    public String getPrice_total() {
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
