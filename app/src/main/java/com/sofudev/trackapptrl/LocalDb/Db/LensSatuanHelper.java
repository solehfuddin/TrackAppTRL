package com.sofudev.trackapptrl.LocalDb.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.Data.Data_stok_satuan;
import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_NAME;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_QTY;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.PRODUCT_STOCK;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensSatuanContract.TABLE_LENSSATUAN;

public class LensSatuanHelper {
    private static final String DATABASE_TABLE = TABLE_LENSSATUAN;
    private static DbTrlHelper dbTrlHelper;
    private static LensSatuanHelper INSTANCE;

    private static SQLiteDatabase database;

    private LensSatuanHelper(Context context) {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static LensSatuanHelper getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null)
                {
                    INSTANCE = new LensSatuanHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open() throws SQLiteException {
        database = dbTrlHelper.getWritableDatabase();
    }

    public void close()
    {
        dbTrlHelper.close();

        if (database.isOpen())
        {
            database.close();
        }
    }

    public List<Data_stok_satuan> getAllLensSatuan() {
        List<Data_stok_satuan> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                PRODUCT_ID + " ASC",
                null);
        cursor.moveToFirst();
        Data_stok_satuan stokSatuan;
        if (cursor.getCount() > 0) {
            do {
                stokSatuan = new Data_stok_satuan();
                stokSatuan.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                stokSatuan.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
                stokSatuan.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_QTY)));
                stokSatuan.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_STOCK)));

                arrayList.add(stokSatuan);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertLensSatuan(Data_stok_satuan stokSatuan) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_ID, stokSatuan.getId());
        args.put(PRODUCT_NAME, stokSatuan.getDescription());
        args.put(PRODUCT_QTY, stokSatuan.getQty());
        args.put(PRODUCT_STOCK, stokSatuan.getStock());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countLensSatuan()
    {
        Cursor cursor = database.rawQuery("select count(*) from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int truncAllLensSatuan() {
        return database.delete(TABLE_LENSSATUAN, null, null);
    }
}
