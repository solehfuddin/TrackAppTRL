package com.sofudev.trackapptrl.LocalDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.Model.ModelOrderHistory;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "trl_lokal";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ModelOrderHistory.CREATE_TABLE);
        db.execSQL(ModelOrderHistory.CREATE_REALTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + ModelOrderHistory.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + ModelOrderHistory.REAL_TABLE);

        onCreate(db);
    }

    public String insertOrder(ModelOrderHistory item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ModelOrderHistory.COLUMN_ORDERNUMBER, item.getOrder_number());
        values.put(ModelOrderHistory.COLUMN_LENSDESCRIPTION, item.getLens_description());
        values.put(ModelOrderHistory.COLUMN_LENSCATEGORY, item.getLens_category());
        values.put(ModelOrderHistory.COLUMN_PRICELENS, item.getPrice_lens());
        values.put(ModelOrderHistory.COLUMN_PRICEDISCOUNT, item.getPrice_discount());
        values.put(ModelOrderHistory.COLUMN_PRICEFACET, item.getPrice_facet());
        values.put(ModelOrderHistory.COLUMN_PRICETINTING, item.getPrice_tinting());
        values.put(ModelOrderHistory.COLUMN_PRICESHIPMENT, item.getPrice_shipment());
        values.put(ModelOrderHistory.COLUMN_PRICETOTAL, item.getPrice_total());
        values.put(ModelOrderHistory.COLUMN_STATUS, item.getStatus());
        values.put(ModelOrderHistory.COLUMN_WEIGHT, item.getWeight());
        values.put(ModelOrderHistory.COLUMN_DATE, item.getDate());

        db.insert(ModelOrderHistory.TABLE_NAME, null, values);

        return item.getOrder_number();
    }

    public String insertRealOrder(ModelOrderHistory item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ModelOrderHistory.COLUMN_ORDERNUMBER, item.getOrder_number());
        values.put(ModelOrderHistory.COLUMN_LENSDESCRIPTION, item.getLens_description());
        values.put(ModelOrderHistory.COLUMN_LENSCATEGORY, item.getLens_category());
        values.put(ModelOrderHistory.COLUMN_PRICELENS, item.getPrice_lens());
        values.put(ModelOrderHistory.COLUMN_PRICEDISCOUNT, item.getPrice_discount());
        values.put(ModelOrderHistory.COLUMN_PRICEFACET, item.getPrice_facet());
        values.put(ModelOrderHistory.COLUMN_PRICETINTING, item.getPrice_tinting());
        values.put(ModelOrderHistory.COLUMN_PRICESHIPMENT, item.getPrice_shipment());
        values.put(ModelOrderHistory.COLUMN_PRICETOTAL, item.getPrice_total());
        values.put(ModelOrderHistory.COLUMN_STATUS, item.getStatus());
        values.put(ModelOrderHistory.COLUMN_WEIGHT, item.getWeight());
        values.put(ModelOrderHistory.COLUMN_DATE, item.getDate());

        db.insert(ModelOrderHistory.REAL_TABLE, null, values);

        return item.getOrder_number();
    }

    public String updateOrder(ModelOrderHistory item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ModelOrderHistory.COLUMN_LENSDESCRIPTION, item.getLens_description());
        values.put(ModelOrderHistory.COLUMN_LENSCATEGORY, item.getLens_category());
        values.put(ModelOrderHistory.COLUMN_PRICELENS, item.getPrice_lens());
        values.put(ModelOrderHistory.COLUMN_PRICEDISCOUNT, item.getPrice_discount());
        values.put(ModelOrderHistory.COLUMN_PRICEFACET, item.getPrice_facet());
        values.put(ModelOrderHistory.COLUMN_PRICETINTING, item.getPrice_tinting());
        values.put(ModelOrderHistory.COLUMN_PRICESHIPMENT, item.getPrice_shipment());
        values.put(ModelOrderHistory.COLUMN_PRICETOTAL, item.getPrice_total());
        values.put(ModelOrderHistory.COLUMN_STATUS, item.getStatus());
        values.put(ModelOrderHistory.COLUMN_WEIGHT, item.getWeight());
        values.put(ModelOrderHistory.COLUMN_DATE, item.getDate());

        db.update(ModelOrderHistory.TABLE_NAME, values, ModelOrderHistory.COLUMN_ORDERNUMBER + " = ?",
                new String[]{item.getOrder_number()});

        db.close();

        return item.getOrder_number();
    }

    public String deleteOrder(ModelOrderHistory item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ModelOrderHistory.TABLE_NAME, ModelOrderHistory.COLUMN_ORDERNUMBER + " = ?",
                new String[]{item.getOrder_number()});
        db.close();

        return item.getOrder_number();
    }

    public void deleteAllOrder()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ModelOrderHistory.TABLE_NAME);
        db.close();
    }

    public ModelOrderHistory getOrderHistory(String orderNumber)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ModelOrderHistory item = new ModelOrderHistory("Data not found");
        Cursor cursor = db.query(ModelOrderHistory.TABLE_NAME, new String[]{ModelOrderHistory.COLUMN_ORDERNUMBER,
                                ModelOrderHistory.COLUMN_LENSDESCRIPTION, ModelOrderHistory.COLUMN_LENSCATEGORY,
                                ModelOrderHistory.COLUMN_PRICELENS, ModelOrderHistory.COLUMN_PRICEDISCOUNT,
                                ModelOrderHistory.COLUMN_PRICEFACET, ModelOrderHistory.COLUMN_PRICETINTING,
                                ModelOrderHistory.COLUMN_PRICESHIPMENT, ModelOrderHistory.COLUMN_PRICETOTAL,
                                ModelOrderHistory.COLUMN_STATUS, ModelOrderHistory.COLUMN_WEIGHT,
                                ModelOrderHistory.COLUMN_DATE}, ModelOrderHistory.COLUMN_ORDERNUMBER + "=?",
                                new String[]{orderNumber}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            item = new ModelOrderHistory(
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_ORDERNUMBER)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_LENSDESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_LENSCATEGORY)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_PRICELENS)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_PRICEDISCOUNT)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_PRICEFACET)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_PRICETINTING)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_PRICESHIPMENT)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_PRICETOTAL)),
                    cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_WEIGHT))
            );

            cursor.close();
        }

        return item;
    }

    public List<ModelOrderHistory> getAllOrderHistory()
    {
        List<ModelOrderHistory> orderHistories = new ArrayList<>();
        String query = "SELECT * FROM " + ModelOrderHistory.TABLE_NAME + " ORDER BY " + ModelOrderHistory.COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ModelOrderHistory data = new ModelOrderHistory();
                data.setWeight(cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_WEIGHT)));

                orderHistories.add(data);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return orderHistories;
    }

    public List<ModelOrderHistory> getAllOrderHistory(String date)
    {
        List<ModelOrderHistory> orderHistories = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ModelOrderHistory.TABLE_NAME, new String[]{ModelOrderHistory.COLUMN_ORDERNUMBER,
                        ModelOrderHistory.COLUMN_LENSDESCRIPTION, ModelOrderHistory.COLUMN_LENSCATEGORY,
                        ModelOrderHistory.COLUMN_PRICELENS, ModelOrderHistory.COLUMN_PRICEDISCOUNT,
                        ModelOrderHistory.COLUMN_PRICEFACET, ModelOrderHistory.COLUMN_PRICETINTING,
                        ModelOrderHistory.COLUMN_PRICESHIPMENT, ModelOrderHistory.COLUMN_PRICETOTAL,
                        ModelOrderHistory.COLUMN_STATUS, ModelOrderHistory.COLUMN_WEIGHT,
                        ModelOrderHistory.COLUMN_DATE}, ModelOrderHistory.COLUMN_DATE + "=?",
                        new String[]{date}, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do {
                ModelOrderHistory data = new ModelOrderHistory();
                data.setWeight(cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_WEIGHT)));

                orderHistories.add(data);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return orderHistories;
    }

    public List<ModelOrderHistory> getAllOrderHistoryWithLike(String date)
    {
        List<ModelOrderHistory> orderHistories = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ModelOrderHistory.TABLE_NAME, new String[]{ModelOrderHistory.COLUMN_ORDERNUMBER,
                        ModelOrderHistory.COLUMN_LENSDESCRIPTION, ModelOrderHistory.COLUMN_LENSCATEGORY,
                        ModelOrderHistory.COLUMN_PRICELENS, ModelOrderHistory.COLUMN_PRICEDISCOUNT,
                        ModelOrderHistory.COLUMN_PRICEFACET, ModelOrderHistory.COLUMN_PRICETINTING,
                        ModelOrderHistory.COLUMN_PRICESHIPMENT, ModelOrderHistory.COLUMN_PRICETOTAL,
                        ModelOrderHistory.COLUMN_STATUS, ModelOrderHistory.COLUMN_WEIGHT,
                        ModelOrderHistory.COLUMN_DATE}, ModelOrderHistory.COLUMN_DATE + " LIKE ?",
                        new String[]{date+"%"}, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do {
                ModelOrderHistory data = new ModelOrderHistory();
                data.setWeight(cursor.getString(cursor.getColumnIndex(ModelOrderHistory.COLUMN_WEIGHT)));

                orderHistories.add(data);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return orderHistories;
    }

    public long countOrderHistory()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, ModelOrderHistory.TABLE_NAME);
        db.close();
        return count;
    }

    public long countOrderHistory(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, ModelOrderHistory.TABLE_NAME,
                ModelOrderHistory.COLUMN_DATE + " = ?", new String[]{date});
        db.close();
        return count;
    }

    public long countOrderHistoryWithLike(String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, ModelOrderHistory.REAL_TABLE,
                ModelOrderHistory.COLUMN_DATE + " LIKE ?", new String[]{date+"%"});
        db.close();
        return count;
    }

    public int countTotalWeight(String date)
    {
        String countQuery = "SELECT SUM(weight) FROM " + ModelOrderHistory.TABLE_NAME + " WHERE date like '" + date + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public void moveToRealTbl()
    {
        String query = "INSERT INTO " + ModelOrderHistory.REAL_TABLE + " SELECT * FROM " + ModelOrderHistory.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
}
