package com.sofudev.trackapptrl.LocalDb.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_CODE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_DISCOUNT;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_DISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_IMG;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_NAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_NEWDISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_NEWPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_PRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_QTY;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_STOCK;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_WEIGHT;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.TABLE_ADDCART;

public class AddCartHelper {
    private static final String DATABASE_TABLE = TABLE_ADDCART;
    private static DbTrlHelper dbTrlHelper;
    private static AddCartHelper INSTANCE;

    private static SQLiteDatabase database;

    private AddCartHelper(Context context)
    {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static AddCartHelper getINSTANCE(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null)
                {
                    INSTANCE = new AddCartHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
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
    public List<ModelAddCart> getAllCart() {
        List<ModelAddCart> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                PRODUCT_ID + " ASC",
                null);
        cursor.moveToFirst();
        ModelAddCart addCart;
        if (cursor.getCount() > 0) {
            do {
                addCart = new ModelAddCart();
                addCart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                addCart.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
                addCart.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CODE)));
                addCart.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_QTY)));
                addCart.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)));
                addCart.setProductDiscPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_DISCPRICE)));
                addCart.setProductDisc(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISCOUNT)));
                addCart.setNewProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWPRICE)));
                addCart.setNewProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWDISCPRICE)));
                addCart.setProductStock(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_STOCK)));
                addCart.setProductWeight(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_WEIGHT)));
                addCart.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_IMG)));

                arrayList.add(addCart);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    @SuppressLint("Range")
    public ModelAddCart getAddCart(int id)
    {
        Cursor cursor = null;
        ModelAddCart item = new ModelAddCart();
        try {
            cursor = database.rawQuery("select * from trl_addcart WHERE product_id=?" , new String[] {id + ""});

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

    public long insertAddCart(ModelAddCart addCart) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_ID, addCart.getProductId());
        args.put(PRODUCT_NAME, addCart.getProductName());
        args.put(PRODUCT_CODE, addCart.getProductCode());
        args.put(PRODUCT_QTY, addCart.getProductQty());
        args.put(PRODUCT_PRICE, addCart.getProductPrice());
        args.put(PRODUCT_DISCPRICE, addCart.getProductDiscPrice());
        args.put(PRODUCT_DISCOUNT, addCart.getProductDisc());
        args.put(PRODUCT_NEWPRICE, addCart.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, addCart.getNewProductDiscPrice());
        args.put(PRODUCT_STOCK, addCart.getProductStock());
        args.put(PRODUCT_WEIGHT, addCart.getProductWeight());
        args.put(PRODUCT_IMG, addCart.getProductImage());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countAddCart()
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

    public int updateCartQty(ModelAddCart modelAddCart) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_QTY, modelAddCart.getProductQty());
        args.put(PRODUCT_NEWPRICE, modelAddCart.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, modelAddCart.getNewProductDiscPrice());
        return database.update(DATABASE_TABLE, args, PRODUCT_ID + "= '" + modelAddCart.getProductId() + "'", null);
    }

    public int truncCart() {
        return database.delete(TABLE_ADDCART, null, null);
    }

    public int deleteWishlist(int id) {
        return database.delete(TABLE_ADDCART, PRODUCT_ID + " = '" + id + "'", null);
    }
}

