package com.sofudev.trackapptrl.LocalDb.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelFrameSp;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_CODE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_DISCOUNT;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_DISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_IMG;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_NAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_NEWDISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_NEWPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_PRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_QTY;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_STOCK;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.PRODUCT_WEIGHT;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.TABLE_FRAMESP;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract._ID;

public class AddFrameSpHelper {
    private static final String DATABASE_TABLE = TABLE_FRAMESP;
    private static DbTrlHelper dbTrlHelper;
    private static AddFrameSpHelper INSTANCE;

    private static SQLiteDatabase database;

    private AddFrameSpHelper(Context context)
    {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static AddFrameSpHelper getINSTANCE(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null)
                {
                    INSTANCE = new AddFrameSpHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dbTrlHelper.getWritableDatabase();
    }

    public void close() {
        dbTrlHelper.close();

        if (database.isOpen())
        {
            database.close();
        }
    }

    @SuppressLint("Range")
    public List<ModelFrameSp> getAllFrameSp() {
        List<ModelFrameSp> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
//                PRODUCT_ID + " ASC",
                null);
        cursor.moveToFirst();
        ModelFrameSp addFrameSp;
        if (cursor.getCount() > 0) {
            do {
                addFrameSp = new ModelFrameSp();
                addFrameSp.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                addFrameSp.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
                addFrameSp.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CODE)));
                addFrameSp.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_QTY)));
                addFrameSp.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)));
                addFrameSp.setProductDiscPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_DISCPRICE)));
                addFrameSp.setProductDisc(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISCOUNT)));
                addFrameSp.setNewProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWPRICE)));
                addFrameSp.setNewProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWDISCPRICE)));
                addFrameSp.setProductStock(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_STOCK)));
                addFrameSp.setProductWeight(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_WEIGHT)));
                addFrameSp.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_IMG)));

                arrayList.add(addFrameSp);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    @SuppressLint("Range")
    public ModelFrameSp getAddFrameSp(int id)
    {
        Cursor cursor = null;
        ModelFrameSp item = new ModelFrameSp();
        try {
            cursor = database.rawQuery("select * from trl_framesp WHERE product_id=?" , new String[] {id + ""});

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                item.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                item.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                item.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow("product_code")));
                item.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow("product_qty")));
                item.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow("product_price")));
                item.setProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow("product_discprice")));
                item.setProductDisc(cursor.getInt(cursor.getColumnIndexOrThrow("product_discount")));
                item.setNewProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow("product_newprice")));
                item.setNewProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow("product_newdiscprice")));
                item.setProductStock(cursor.getInt(cursor.getColumnIndexOrThrow("product_stock")));
                item.setProductWeight(cursor.getInt(cursor.getColumnIndex("product_weight")));
                item.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow("product_image")));
            }

            return item;
        }
        finally {
            cursor.close();
        }
    }

    public int checkAddFrameSp(int id)
    {
        Cursor cursor = null;
        cursor = database.rawQuery("select * from trl_framesp WHERE product_id=?" , new String[] {id + ""});

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public long insertAddFrameSp(ModelFrameSp addFrameSp) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_ID, addFrameSp.getProductId());
        args.put(PRODUCT_NAME, addFrameSp.getProductName());
        args.put(PRODUCT_CODE, addFrameSp.getProductCode());
        args.put(PRODUCT_QTY, addFrameSp.getProductQty());
        args.put(PRODUCT_PRICE, addFrameSp.getProductPrice());
        args.put(PRODUCT_DISCPRICE, addFrameSp.getProductDiscPrice());
        args.put(PRODUCT_DISCOUNT, addFrameSp.getProductDisc());
        args.put(PRODUCT_NEWPRICE, addFrameSp.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, addFrameSp.getNewProductDiscPrice());
        args.put(PRODUCT_STOCK, addFrameSp.getProductStock());
        args.put(PRODUCT_WEIGHT, addFrameSp.getProductWeight());
        args.put(PRODUCT_IMG, addFrameSp.getProductImage());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countAddFrameSp()
    {
        Cursor cursor = database.rawQuery("select count(*) from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int countTotalPrice()
    {
        Cursor cursor = database.rawQuery("select sum(product_newprice) as total_price from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int countTotalDiscPrice()
    {
        Cursor cursor = database.rawQuery("select sum(product_newdiscprice) as total_discprice from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int updateFrameSpQty(ModelFrameSp modelFrameSp) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_QTY, modelFrameSp.getProductQty());
        args.put(PRODUCT_NEWPRICE, modelFrameSp.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, modelFrameSp.getNewProductDiscPrice());
        return database.update(DATABASE_TABLE, args, PRODUCT_ID + "= '" + modelFrameSp.getProductId() + "'", null);
    }

    public int truncCart() {
        return database.delete(TABLE_FRAMESP, null, null);
    }

    public int deleteWishlist(int id) {
        return database.delete(TABLE_FRAMESP, PRODUCT_ID + " = '" + id + "'", null);
    }
}
