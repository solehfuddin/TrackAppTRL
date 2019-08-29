package com.sofudev.trackapptrl.LocalDb.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sofudev.trackapptrl.Data.Data_search_product;
import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;

import java.util.ArrayList;
import java.util.List;

import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.SEARCH_ID;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.SEARCH_TITLE;
import static com.sofudev.trackapptrl.LocalDb.Contract.RecentSearchContract.TABLE_RECENT_SEARCH;

public class RecentSearchHelper {
    private static final String DATABASE_TABLE = TABLE_RECENT_SEARCH;
    private static DbTrlHelper dbTrlHelper;
    private static RecentSearchHelper INSTANCE;

    private static SQLiteDatabase database;

    private RecentSearchHelper(Context context){
        dbTrlHelper = new DbTrlHelper(context);
    }

    public static RecentSearchHelper getINSTANCE(Context context){
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null)
                {
                    INSTANCE = new RecentSearchHelper(context);
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

    public List<Data_search_product> getAllRecentSearch() {
        List<Data_search_product> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                SEARCH_ID + " DESC",
                null);
        cursor.moveToFirst();
        Data_search_product recentSearch;
        if (cursor.getCount() > 0) {
            do {
                recentSearch = new Data_search_product();
                recentSearch.setSearchId(cursor.getInt(cursor.getColumnIndexOrThrow(SEARCH_ID)));
                recentSearch.setSearchTitle(cursor.getString(cursor.getColumnIndexOrThrow(SEARCH_TITLE)));

                arrayList.add(recentSearch);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Data_search_product getRecentSearch(int id)
    {
        Cursor cursor = null;
        Data_search_product item = new Data_search_product();
        try {
            cursor = database.rawQuery("select * from tbl_recentsearch WHERE search_id=?" , new String[] {id + ""});

            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                item.setSearchId(cursor.getInt(cursor.getColumnIndexOrThrow("search_id")));
                item.setSearchTitle(cursor.getString(cursor.getColumnIndexOrThrow("search_title")));
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
            cursor = database.rawQuery("select * from tbl_recentsearch WHERE search_title=?" , new String[] {title + ""});

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

    public long insertRecentSearch(Data_search_product recentSearch) {
        ContentValues args = new ContentValues();
        args.put(SEARCH_ID, recentSearch.getSearchId());
        args.put(SEARCH_TITLE, recentSearch.getSearchTitle());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int countRecentSearch()
    {
        Cursor cursor = database.rawQuery("select count(*) from " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int output = cursor.getInt(0);
        cursor.close();

        return output;
    }

    public int truncAllRecentSearch() {
        return database.delete(TABLE_RECENT_SEARCH, null, null);
    }

    public int deleteRecentSearch(int id) {
        return database.delete(TABLE_RECENT_SEARCH, SEARCH_ID + " = '" + id + "'", null);
    }
}
