package com.sofudev.trackapptrl.LocalDb.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelReturFrame;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_CODE;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_COLLECT;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_DISCOUNT;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_DISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_FLAG;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_IMG;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_NAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_NEWDISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_NEWPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_PRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_QTY;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_STOCK;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_STOCK_TMP;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.PRODUCT_WEIGHT;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract.TABLE_RETURFRAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.ReturFrameContract._ID;

public class ReturFrameHelper {
    private static final String DATABASE_TABLE = TABLE_RETURFRAME;
    private static DbTrlHelper dbTrlHelper;
    private static ReturFrameHelper INSTANCE;

    private static SQLiteDatabase database;

    private ReturFrameHelper(Context context)
    {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static ReturFrameHelper getINSTANCE(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null)
                {
                    INSTANCE = new ReturFrameHelper(context);
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
    public List<ModelReturFrame> getAllReturFrame() {
        List<ModelReturFrame> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
//                PRODUCT_ID + " ASC",
                null);
        cursor.moveToFirst();
        ModelReturFrame returFrame;
        if (cursor.getCount() > 0) {
            do {
                returFrame = new ModelReturFrame();
                returFrame.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                returFrame.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
                returFrame.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CODE)));
                returFrame.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_QTY)));
                returFrame.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)));
                returFrame.setProductDiscPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_DISCPRICE)));
                returFrame.setProductDisc(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISCOUNT)));
                returFrame.setNewProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWPRICE)));
                returFrame.setNewProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWDISCPRICE)));
                returFrame.setProductStock(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_STOCK)));
                returFrame.setProductTempStock(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_STOCK_TMP)));
                returFrame.setProductWeight(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_WEIGHT)));
                returFrame.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_IMG)));
                returFrame.setProductFlag(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_FLAG)));
                returFrame.setProductCollect(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_COLLECT)));

                arrayList.add(returFrame);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    @SuppressLint("Range")
    public ModelReturFrame getReturFrame(int id)
    {
        Cursor cursor = null;
        ModelReturFrame item = new ModelReturFrame();
        try {
            cursor = database.rawQuery("select * from trl_frameretur WHERE product_id=?" , new String[] {id + ""});

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
                item.setProductTempStock(cursor.getInt(cursor.getColumnIndexOrThrow("product_stock_tmp")));
                item.setProductWeight(cursor.getInt(cursor.getColumnIndex("product_weight")));
                item.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow("product_image")));
                item.setProductFlag(cursor.getString(cursor.getColumnIndexOrThrow("product_flag")));
                item.setProductCollect(cursor.getString(cursor.getColumnIndexOrThrow("product_collect")));
            }

            return item;
        }
        finally {
            cursor.close();
        }
    }

    public int checkReturFrame(int id)
    {
        Cursor cursor = null;
        cursor = database.rawQuery("select * from trl_frameretur WHERE product_id=?" , new String[] {id + ""});

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

    public long insertReturFrame(ModelReturFrame returFrame) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_ID, returFrame.getProductId());
        args.put(PRODUCT_NAME, returFrame.getProductName());
        args.put(PRODUCT_CODE, returFrame.getProductCode());
        args.put(PRODUCT_QTY, returFrame.getProductQty());
        args.put(PRODUCT_PRICE, returFrame.getProductPrice());
        args.put(PRODUCT_DISCPRICE, returFrame.getProductDiscPrice());
        args.put(PRODUCT_DISCOUNT, returFrame.getProductDisc());
        args.put(PRODUCT_NEWPRICE, returFrame.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, returFrame.getNewProductDiscPrice());
        args.put(PRODUCT_STOCK, returFrame.getProductStock());
        args.put(PRODUCT_STOCK_TMP, returFrame.getProductTempStock());
        args.put(PRODUCT_WEIGHT, returFrame.getProductWeight());
        args.put(PRODUCT_IMG, returFrame.getProductImage());
        args.put(PRODUCT_FLAG, returFrame.getProductFlag());
        args.put(PRODUCT_COLLECT, returFrame.getProductCollect());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countReturFrame()
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

    public int countTotalQty()
    {
        Cursor cursor = database.rawQuery("select sum(product_qty) as total_qty from " + DATABASE_TABLE, null);
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

    public int updateReturFrameQty(ModelReturFrame returFrame) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_QTY, returFrame.getProductQty());
        args.put(PRODUCT_NEWPRICE, returFrame.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, returFrame.getNewProductDiscPrice());
        return database.update(DATABASE_TABLE, args, PRODUCT_ID + "= '" + returFrame.getProductId() + "'", null);
    }

    public int updateReturFrameStock(ModelReturFrame returFrame, int newStock){
        ContentValues args = new ContentValues();
        args.put(PRODUCT_STOCK, newStock);
        return database.update(DATABASE_TABLE, args, PRODUCT_ID + "= '" + returFrame.getProductId() + "'", null);
    }

    public int updateReturFrameStockTmp(ModelReturFrame returFrame, int newStock){
        ContentValues args = new ContentValues();
        args.put(PRODUCT_STOCK_TMP, newStock);
        return database.update(DATABASE_TABLE, args, PRODUCT_ID + "= '" + returFrame.getProductId() + "'", null);
    }

    public int truncReturFrame() {
        return database.delete(TABLE_RETURFRAME, null, null);
    }

    public int deleteWishlist(int id) {
        return database.delete(TABLE_RETURFRAME, PRODUCT_ID + " = '" + id + "'", null);
    }
}
