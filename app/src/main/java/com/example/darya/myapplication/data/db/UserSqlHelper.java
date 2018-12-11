package com.example.darya.myapplication.data.db;

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
                + SqlUserFields.phone + " TEXT NOT NULL, "
                + SqlUserFields.password + " TEXT NOT NULL);";
        db.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
        onCreate(sqLiteDatabase);
    }
}
