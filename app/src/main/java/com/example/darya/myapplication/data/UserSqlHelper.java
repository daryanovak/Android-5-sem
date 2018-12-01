package com.example.darya.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSqlHelper extends SQLiteOpenHelper {
    public static final String NAME_OF_TABLE = "user_db";
    public static final int VERSION = 1;

    public UserSqlHelper(Context context) {
        super(context, NAME_OF_TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String request = "CREATE TABLE " + NAME_OF_TABLE + " ("
                + SqlUserFields.id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SqlUserFields.first_name + " TEXT NOT NULL, "
                + SqlUserFields.last_name + " TEXT NOT NULL, "
                + SqlUserFields.email + " TEXT NOT NULL, "
                + SqlUserFields.phone + " TEXT NOT NULL);";
        db.execSQL(request);
        request = "INSERT INTO " +
                NAME_OF_TABLE + " ("
                + SqlUserFields.id + ", "
                + SqlUserFields.first_name + ", "
                + SqlUserFields.last_name + ", "
                + SqlUserFields.email + ", "
                + SqlUserFields.phone + ") "
                + " VALUES ('0', 'default', 'default', 'default', 'default')";
        db.execSQL(request);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
