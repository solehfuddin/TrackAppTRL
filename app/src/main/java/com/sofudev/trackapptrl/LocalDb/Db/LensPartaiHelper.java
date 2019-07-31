package com.sofudev.trackapptrl.LocalDb.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelLensPartai;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_ADD;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_CODE;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_CYL;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_DESC;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_DISC;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_DISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_IMG;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_NEWDISCPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_NEWPRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_PRICE;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_QTY;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_SIDE;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.PRODUCT_SPH;
import static com.sofudev.trackapptrl.LocalDb.Contract.LensPartaiContract.TABLE_LENSPARTAI;

public class LensPartaiHelper {
    private static final String DATABASE_TABLE = TABLE_LENSPARTAI;
    private static DbTrlHelper dbTrlHelper;
    private static LensPartaiHelper INSTANCE;

    private SQLiteDatabase database;

    private LensPartaiHelper(Context context) {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static LensPartaiHelper getINSTANCE(Context context) {
        if (INSTANCE == null)
        {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LensPartaiHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open() throws SQLiteException{
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

    public List<ModelLensPartai> getAllPartai() {
        List<ModelLensPartai> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                PRODUCT_ID + " ASC",
                null);
        cursor.moveToFirst();
        ModelLensPartai lensPartai;
        if (cursor.getCount() > 0) {
            do {
                lensPartai = new ModelLensPartai();
                lensPartai.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                lensPartai.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CODE)));
                lensPartai.setProductDesc(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_DESC)));
                lensPartai.setPowerSph(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_SPH)));
                lensPartai.setPowerCyl(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CYL)));
                lensPartai.setPowerAdd(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_ADD)));
                lensPartai.setProductSide(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_SIDE)));
                lensPartai.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_QTY)));
                lensPartai.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)));
                lensPartai.setProductDisc(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISC)));
                lensPartai.setProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISCPRICE)));
                lensPartai.setNewProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWPRICE)));
                lensPartai.setNewProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWDISCPRICE)));
                lensPartai.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_IMG)));

                arrayList.add(lensPartai);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ModelLensPartai getLensPartai(int id)
    {
        Cursor cursor = null;
        ModelLensPartai item = new ModelLensPartai();
        try {
            cursor = database.rawQuery("select * from trl_lenspartai WHERE product_id=?" , new String[] {id + ""});

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                item.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
                item.setProductCode(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CODE)));
                item.setProductDesc(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_DESC)));
                item.setPowerSph(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_SPH)));
                item.setPowerCyl(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CYL)));
                item.setPowerAdd(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_ADD)));
                item.setProductSide(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_SIDE)));
                item.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_QTY)));
                item.setProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)));
                item.setProductDisc(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISC)));
                item.setProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_DISCPRICE)));
                item.setNewProductPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWPRICE)));
                item.setNewProductDiscPrice(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_NEWDISCPRICE)));
                item.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_IMG)));
            }

            return item;
        }
        finally {
            cursor.close();
        }
    }

    public long insertLensPartai(ModelLensPartai lensPartai) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_ID, lensPartai.getProductId());
        args.put(PRODUCT_CODE, lensPartai.getProductCode());
        args.put(PRODUCT_DESC, lensPartai.getProductDesc());
        args.put(PRODUCT_SPH, lensPartai.getPowerSph());
        args.put(PRODUCT_CYL, lensPartai.getPowerCyl());
        args.put(PRODUCT_ADD, lensPartai.getPowerAdd());
        args.put(PRODUCT_SIDE, lensPartai.getProductSide());
        args.put(PRODUCT_QTY, lensPartai.getProductQty());
        args.put(PRODUCT_PRICE, lensPartai.getProductPrice());
        args.put(PRODUCT_DISC, lensPartai.getProductDisc());
        args.put(PRODUCT_DISCPRICE, lensPartai.getProductDiscPrice());
        args.put(PRODUCT_NEWPRICE, lensPartai.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, lensPartai.getNewProductDiscPrice());
        args.put(PRODUCT_IMG, lensPartai.getProductImage());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countLensPartai()
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

    public int updateLensPartaiQty(ModelLensPartai modelLensPartai) {
        ContentValues args = new ContentValues();
        args.put(PRODUCT_QTY, modelLensPartai.getProductQty());
        args.put(PRODUCT_NEWPRICE, modelLensPartai.getNewProductPrice());
        args.put(PRODUCT_NEWDISCPRICE, modelLensPartai.getNewProductDiscPrice());
        return database.update(DATABASE_TABLE, args, PRODUCT_ID + "= '" + modelLensPartai.getProductId() + "'", null);
    }

    public int updateLensPartaiDisc(ModelLensPartai modelLensPartai)
    {
        ContentValues arg = new ContentValues();
        arg.put(PRODUCT_DISC, modelLensPartai.getProductDisc());
        arg.put(PRODUCT_DISCPRICE, modelLensPartai.getProductDiscPrice());
        arg.put(PRODUCT_NEWDISCPRICE, modelLensPartai.getNewProductDiscPrice());
        return database.update(DATABASE_TABLE, arg, PRODUCT_ID + "= '" + modelLensPartai.getProductId() + "'", null);
    }

    public int truncLensPartai() {
        return database.delete(TABLE_LENSPARTAI, null, null);
    }

    public int deleteLensPartai(int id) {
        return database.delete(TABLE_LENSPARTAI, PRODUCT_ID + " = '" + id + "'", null);
    }
}
