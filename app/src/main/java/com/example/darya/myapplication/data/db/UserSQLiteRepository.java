package com.example.darya.myapplication.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.darya.myapplication.data.models.User;

public class UserSQLiteRepository {
    private UserSqlHelper userSqlHelper;
    private int userId;


    public UserSQLiteRepository(Context contex){
        userSqlHelper = new UserSqlHelper(contex);
    }

    public User getUser(){
        SQLiteDatabase db = userSqlHelper.getReadableDatabase();
        try {
        String request = "SELECT * FROM " + userSqlHelper.NAME_OF_TABLE;
        Cursor cursor = db.rawQuery(request, null);
        if (cursor.moveToNext()){
            userId = cursor.getInt(SqlUserFields.id.ordinal());
            String first_name = cursor.getString(SqlUserFields.first_name.ordinal());
            String last_name = cursor.getString(SqlUserFields.last_name.ordinal());
            String email = cursor.getString(SqlUserFields.email.ordinal());
            String phone = cursor.getString(SqlUserFields.phone.ordinal());
            return new User(userId, first_name, last_name, email, phone);
        }
        return null;
        } finally {
            db.close();
        }
    }

    public void savedUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlUserFields.first_name.toString(), user.getFirstName());
        contentValues.put(SqlUserFields.last_name.toString(), user.getLastName());
        contentValues.put(SqlUserFields.phone.toString(), user.getPhone());
        contentValues.put(SqlUserFields.email.toString(), user.getEmail());

        SQLiteDatabase db = userSqlHelper.getReadableDatabase();
        try {
            db.update(userSqlHelper.NAME_OF_TABLE, contentValues,
                    SqlUserFields.id.toString() + "=" + String.valueOf(userId), null);
        }
        finally {
            db.close();
        }
    }
}
