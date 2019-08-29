package com.sofudev.trackapptrl.LocalDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract;
import com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract;
import com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract;

import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.TABLE_ADDCART;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.TABLE_LENSPARTAI;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.SEARCH_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.SEARCH_TITLE;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.TABLE_RECENT_SEARCH;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.TABLE_RECENT_VIEW;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.VIEW_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.VIEW_TITLE;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.TABLE_WISHLIST;


public class DbTrlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbtrl";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TBL_WISHLIST = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s VARCHAR (150)," +
                    " %s VARCHAR (200)," +
                    " %s TEXT," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11))",
            TABLE_WISHLIST,
            WishlistContract.PRODUCT_ID,
            WishlistContract.PRODUCT_CODE,
            WishlistContract.PRODUCT_NAME,
            WishlistContract.PRODUCT_IMG,
            WishlistContract.PRODUCT_PRICE,
            WishlistContract.PRODUCT_DISCOUNT
    );

    private static final String CREATE_TBL_LENSPARTAI = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s VARCHAR (50)," +
                    " %s VARCHAR (200)," +
                    " %s VARCHAR (20)," +
                    " %s VARCHAR (20)," +
                    " %s VARCHAR (20)," +
                    " %s VARCHAR (3)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s TEXT)",
            TABLE_LENSPARTAI,
            LensPartaiContract.PRODUCT_ID,
            LensPartaiContract.PRODUCT_CODE,
            LensPartaiContract.PRODUCT_DESC,
            LensPartaiContract.PRODUCT_SPH,
            LensPartaiContract.PRODUCT_CYL,
            LensPartaiContract.PRODUCT_ADD,
            LensPartaiContract.PRODUCT_SIDE,
            LensPartaiContract.PRODUCT_QTY,
            LensPartaiContract.PRODUCT_PRICE,
            LensPartaiContract.PRODUCT_DISC,
            LensPartaiContract.PRODUCT_DISCPRICE,
            LensPartaiContract.PRODUCT_NEWPRICE,
            LensPartaiContract.PRODUCT_NEWDISCPRICE,
            LensPartaiContract.PRODUCT_IMG
    );

    private static final String CREATE_TBL_ADDCART = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s VARCHAR (200)," +
                    " %s VARCHAR (150)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s TEXT)",
            TABLE_ADDCART,
            AddCartContract.PRODUCT_ID,
            AddCartContract.PRODUCT_NAME,
            AddCartContract.PRODUCT_CODE,
            AddCartContract.PRODUCT_QTY,
            AddCartContract.PRODUCT_PRICE,
            AddCartContract.PRODUCT_DISCPRICE,
            AddCartContract.PRODUCT_DISCOUNT,
            AddCartContract.PRODUCT_NEWPRICE,
            AddCartContract.PRODUCT_NEWDISCPRICE,
            AddCartContract.PRODUCT_IMG
    );

    private static final String CREATE_TBL_RECENTSEARCH = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT)",
            TABLE_RECENT_SEARCH,
            SEARCH_ID,
            SEARCH_TITLE
    );

    private static final String CREATE_TBL_RECENTVIEW = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT)",
            TABLE_RECENT_VIEW,
            VIEW_ID,
            VIEW_TITLE
    );

    public DbTrlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TBL_WISHLIST);
        sqLiteDatabase.execSQL(CREATE_TBL_ADDCART);
        sqLiteDatabase.execSQL(CREATE_TBL_LENSPARTAI);
        sqLiteDatabase.execSQL(CREATE_TBL_RECENTSEARCH);
        sqLiteDatabase.execSQL(CREATE_TBL_RECENTVIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_WISHLIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_ADDCART);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_LENSPARTAI);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_RECENT_SEARCH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_RECENT_VIEW);
        onCreate(sqLiteDatabase);
    }
}
