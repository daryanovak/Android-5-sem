package com.example.darya.myapplication.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.darya.myapplication.data.models.RssFeedModel;

import java.util.ArrayList;
import java.util.List;

public class RssSQLiteRepository {
    private final RssDbHelper rssDbHelper;

    public RssSQLiteRepository(Context context){
        rssDbHelper = new RssDbHelper(context);
    }

    public List<RssFeedModel> getAllFeedFromCache(){
        ArrayList<RssFeedModel> allRssFeed = new ArrayList<>();

        try (SQLiteDatabase db = rssDbHelper.getReadableDatabase()) {
            String request = "SELECT * FROM " + rssDbHelper.getTableName() + ";";
            Cursor cursor = db.rawQuery(request, null);
            while (cursor.moveToNext()){
                int id = cursor.getInt(RssColumnsTable.id.ordinal());
                String title = cursor.getString(RssColumnsTable.title.ordinal());
                String description = cursor.getString(RssColumnsTable.description.ordinal());
                String link = cursor.getString(RssColumnsTable.link.ordinal());
                String date = cursor.getString(RssColumnsTable.date.ordinal());
                allRssFeed.add(new RssFeedModel(title, description, link, date));
            }
        }
        return allRssFeed;
    }


    public void addRss(RssFeedModel rssFeed){
        try (SQLiteDatabase db = rssDbHelper.getReadableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(RssColumnsTable.title.toString(), rssFeed.title);
            contentValues.put(RssColumnsTable.description.toString(), rssFeed.description);
            contentValues.put(RssColumnsTable.link.toString(), rssFeed.link);
            contentValues.put(RssColumnsTable.date.toString(), rssFeed.date);

            db.insert(rssDbHelper.getTableName(), null, contentValues);
        }
    }
}
