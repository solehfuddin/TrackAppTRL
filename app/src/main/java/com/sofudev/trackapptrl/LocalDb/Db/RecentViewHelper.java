package com.sofudev.trackapptrl.LocalDb.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.Data.Data_recent_view;
import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.TABLE_RECENT_VIEW;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.VIEW_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentViewContract.VIEW_TITLE;

public class RecentViewHelper {
    private static final String DATABASE_TABLE = TABLE_RECENT_VIEW;

    private static DbTrlHelper dbTrlHelper;
    private static RecentViewHelper INSTANCE;

    private static SQLiteDatabase database;

    private RecentViewHelper(Context context)
    {
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static RecentViewHelper getINSTANCE(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (SQLiteOpenHelper.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new RecentViewHelper(context);
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

    public List<Data_recent_view> getAllRecentView() {
        List<Data_recent_view> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                VIEW_ID + " DESC",
                null);
        cursor.moveToFirst();
        Data_recent_view recentView;
        if (cursor.getCount() > 0) {
            do {
                recentView = new Data_recent_view();
                recentView.setId(cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_ID)));
                recentView.setImage(cursor.getString(cursor.getColumnIndexOrThrow(VIEW_TITLE)));

                arrayList.add(recentView);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Data_recent_view getRecentView(int id)
    {
        Cursor cursor = null;
        Data_recent_view item = new Data_recent_view();
        try {
            cursor = database.rawQuery("select * from tbl_recentsearch WHERE search_id=?" , new String[] {id + ""});

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("view_id")));
                item.setImage(cursor.getString(cursor.getColumnIndexOrThrow("view_title")));
            }

            return item;
        }
        finally {
            cursor.close();
        }
    }

    public int getIsDuplicate(String title)
    {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from tbl_recentview WHERE view_id=?" , new String[] {title + ""});

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                return 0;
            }

            return 1;
        }
        finally {
            cursor.close();
        }
    }

    public long insertRecentView(Data_recent_view recentView) {
        ContentValues args = new ContentValues();
        args.put(VIEW_ID, recentView.getId());
        args.put(VIEW_TITLE, recentView.getImage());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countRecentView()
    {
        Cursor cursor = database.rawQuery("select count(*) from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int truncAllRecentView() {
        return database.delete(TABLE_RECENT_VIEW, null, null);
    }

    public int deleteRecentView(int id) {
        return database.delete(TABLE_RECENT_VIEW, VIEW_ID + " = '" + id + "'", null);
    }
}
