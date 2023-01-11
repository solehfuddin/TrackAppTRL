package com.sofudev.trackapptrl.LocalDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract;
import com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract;
import com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract;
import com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract;

import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.PRODUCT_STOCK;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddCartContract.TABLE_ADDCART;
import static com.sofudev.trackapptrl.LocalDb.Contract.AddFrameSpContract.TABLE_FRAMESP;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.TABLE_LENSPARTAI;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_NAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_QTY;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.TABLE_LENSSATUAN;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.SEARCH_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.SEARCH_TITLE;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.TABLE_RECENT_SEARCH;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.TABLE_RECENT_VIEW;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.VIEW_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.VIEW_TITLE;
import static com.sofudev.trackapptrl.LocalDb.Contract.WishlistContract.TABLE_WISHLIST;


public class DbTrlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbtrl";
    // Last DB Version = 4 (04-01-2022)
    private static final int DATABASE_VERSION = 5;

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
                    " %s DOUBLE," +
                    " %s DOUBLE," +
                    " %s DOUBLE," +
                    " %s DOUBLE," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s VARCHAR (200)," +
                    " %s INTEGER (11)," +
                    " %s DOUBLE," +
                    " %s TEXT," +
                    " %s DOUBLE)",
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
            LensPartaiContract.PRODUCT_STOCK,
            LensPartaiContract.PRODUCT_WEIGHT,
            LensPartaiContract.PRODUCT_TITLESALE,
            LensPartaiContract.PRODUCT_DISCSALE,
            LensPartaiContract.PRODUCT_DISCPRICESALE,
            LensPartaiContract.PRODUCT_IMG,
            LensPartaiContract.PRODUCT_TEST
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
            AddCartContract.PRODUCT_STOCK,
            AddCartContract.PRODUCT_WEIGHT,
            AddCartContract.PRODUCT_IMG
    );

    private static final String CREATE_TBL_FRAMESP = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " %s INTEGER," +
                    " %s VARCHAR (200)," +
                    " %s VARCHAR (150)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11)," +
                    " %s TEXT)",
            TABLE_FRAMESP,
            AddFrameSpContract._ID,
            AddFrameSpContract.PRODUCT_ID,
            AddFrameSpContract.PRODUCT_NAME,
            AddFrameSpContract.PRODUCT_CODE,
            AddFrameSpContract.PRODUCT_QTY,
            AddFrameSpContract.PRODUCT_PRICE,
            AddFrameSpContract.PRODUCT_DISCPRICE,
            AddFrameSpContract.PRODUCT_DISCOUNT,
            AddFrameSpContract.PRODUCT_NEWPRICE,
            AddFrameSpContract.PRODUCT_NEWDISCPRICE,
            AddFrameSpContract.PRODUCT_STOCK,
            AddFrameSpContract.PRODUCT_WEIGHT,
            AddFrameSpContract.PRODUCT_IMG
    );

    private static final String CREATE_TBL_LENSSATUAN = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT," +
                    " %s INTEGER (11)," +
                    " %s INTEGER (11))",
            TABLE_LENSSATUAN,
            PRODUCT_ID,
            PRODUCT_NAME,
            PRODUCT_QTY,
            PRODUCT_STOCK
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
        sqLiteDatabase.execSQL(CREATE_TBL_FRAMESP);
        sqLiteDatabase.execSQL(CREATE_TBL_LENSPARTAI);
        sqLiteDatabase.execSQL(CREATE_TBL_LENSSATUAN);
        sqLiteDatabase.execSQL(CREATE_TBL_RECENTSEARCH);
        sqLiteDatabase.execSQL(CREATE_TBL_RECENTVIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < 4)
        {
            //Update sql version fix
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDCART);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FRAMESP);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LENSPARTAI);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LENSSATUAN);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_SEARCH);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_VIEW);
//            sqLiteDatabase.execSQL(DATABASE_ALTER_TBL_LENSPARTAI_2);
        }
        else
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDCART);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FRAMESP);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LENSPARTAI);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LENSSATUAN);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_SEARCH);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_VIEW);
        }

        onCreate(sqLiteDatabase);
    }
}
