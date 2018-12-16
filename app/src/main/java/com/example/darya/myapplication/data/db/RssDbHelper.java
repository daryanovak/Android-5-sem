package com.example.darya.myapplication.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RssDbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "rss";
    private static final int VERSION = 1;

    public RssDbHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + "id integer primary key autoincrement,"
                + RssColumnsTable.title + " text,"
                + RssColumnsTable.description + " text,"
                + RssColumnsTable.link + " text,"
                + RssColumnsTable.date + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getTableName(){
        return TABLE_NAME;
    }
}
