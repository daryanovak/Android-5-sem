package com.example.darya.myapplication.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserRepository {
    private UserSqlHelper userSqlHelper;

    public UserRepository(Context contex){
        userSqlHelper = new UserSqlHelper(contex);
    }

    public User getUser(){
        SQLiteDatabase db = userSqlHelper.getReadableDatabase();
        try {
        String request = "SELECT * FROM " + userSqlHelper.NAME_OF_TABLE;
        Cursor cursor = db.rawQuery(request, null);
        if (cursor.moveToNext()){
            int id = cursor.getInt(SqlUserFields.id.ordinal());
            String first_name = cursor.getString(SqlUserFields.first_name.ordinal());
            String last_name = cursor.getString(SqlUserFields.last_name.ordinal());
            String email = cursor.getString(SqlUserFields.email.ordinal());
            String phone = cursor.getString(SqlUserFields.phone.ordinal());
            return new User(id, first_name, last_name, email, phone);
        }
        return null;
        } finally {
            db.close();
        }
    }
}
