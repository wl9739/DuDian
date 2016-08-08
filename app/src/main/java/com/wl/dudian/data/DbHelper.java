package com.wl.dudian.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Qiushui on 16/8/8.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Dudian.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String BOOLEAN_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_NEWS =
            "CREATE TABLE " + PersistenceContract.NewsEntry.TABLE_NAME + " (" +
                    PersistenceContract.NewsEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.NewsEntry.COLUMN_NAME_NEWS_ID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.NewsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.NewsEntry.COLUMN_NAME_IMAGEURL + TEXT_TYPE + COMMA_SEP +
                    " )";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
