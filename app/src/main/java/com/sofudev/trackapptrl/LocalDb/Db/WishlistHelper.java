package com.sofudev.trackapptrl.LocalDb.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelWishlist;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.PRODUCT_CODE;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.PRODUCT_DISCOUNT;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.PRODUCT_IMG;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.PRODUCT_NAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.PRODUCT_PRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.TABLE_WISHLIST;

public class WishlistHelper {
    private static final String DATABASE_TABLE = TABLE_WISHLIST;
    private static DbTrlHelper dbTrlHelper;
    private static WishlistHelper INSTANCE;

    private static SQLiteDatabase database;

    private WishlistHelper(Context context) {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static WishlistHelper getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null)
                {
                    INSTANCE = new WishlistHelper(context);
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

    public List<ModelWishlist> getAllWishlist() {
        List<ModelWishlist> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                PRODUCT_ID + " ASC",
                null);
        cursor.moveToFirst();
        ModelWishlist wishlist;
        if (cursor.getCount() > 0) {
            do {
                wishlist = new ModelWishlist();
                wishlist.setProductid(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                wishlist.setProductcode(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CODE)));
                wishlist.setProductname(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
                wishlist.setProductimg(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_IMG)));
                wishlist.setProductprice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)));
                wishlist.setProductdiscount(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISCOUNT)));

                arrayList.add(wishlist);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ModelWishlist getWishlist(int id)
    {
        Cursor cursor = null;
        ModelWishlist item = new ModelWishlist();
        try {
            cursor = database.rawQuery("select * from trl_wishlist WHERE product_id=?" , new String[] {id + ""});

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                item.setProductid(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                item.setProductcode(cursor.getString(cursor.getColumnIndexOrThrow("product_code")));
                item.setProductname(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                item.setProductimg(cursor.getString(cursor.getColumnIndexOrThrow("product_image")));
                item.setProductprice(cursor.getInt(cursor.getColumnIndexOrThrow("product_price")));
                item.setProductdiscount(cursor.getInt(cursor.getColumnIndexOrThrow("product_discount")));
            }

            return item;
        }
        finally {
            cursor.close();
        }
    }

    public long insertWishlist(ModelWishlist wishlist) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_ID, wishlist.getProductid());
        args.put(PRODUCT_CODE, wishlist.getProductcode());
        args.put(PRODUCT_NAME, wishlist.getProductname());
        args.put(PRODUCT_IMG, wishlist.getProductimg());
        args.put(PRODUCT_PRICE, wishlist.getProductprice());
        args.put(PRODUCT_DISCOUNT, wishlist.getProductdiscount());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countWishlist()
    {
        Cursor cursor = database.rawQuery("select count(*) from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int deleteWishlist(int id) {
        return database.delete(TABLE_WISHLIST, PRODUCT_ID + " = '" + id + "'", null);
    }
}
