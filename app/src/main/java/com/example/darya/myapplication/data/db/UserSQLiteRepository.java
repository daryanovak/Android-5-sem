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


    public void savedUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(SqlUserFields.first_name.toString(), user.getFirstName());
        cv.put(SqlUserFields.last_name.toString(), user.getLastName());
        cv.put(SqlUserFields.phone.toString(), user.getPhone());
        cv.put(SqlUserFields.email.toString(), user.getEmail());
        cv.put(SqlUserFields.news_resource_link.toString(), user.getNewsResource());


        try (SQLiteDatabase db = userSqlHelper.getReadableDatabase()) {
            db.update(userSqlHelper.NAME_OF_TABLE, cv,
                    SqlUserFields.id.toString() + "=" + String.valueOf(userId), null);
        }
    }


    public void addUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlUserFields.first_name.toString(), user.getFirstName());
        contentValues.put(SqlUserFields.last_name.toString(), user.getLastName());
        contentValues.put(SqlUserFields.phone.toString(), user.getPhone());
        contentValues.put(SqlUserFields.email.toString(), user.getEmail());
        contentValues.put(SqlUserFields.password.toString(), user.getHashedPassword());
        contentValues.put(SqlUserFields.news_resource_link.toString(), user.getNewsResource());
        try (SQLiteDatabase database = userSqlHelper.getReadableDatabase()) {
            database.insert(userSqlHelper.NAME_OF_TABLE, null, contentValues);
        }
    }


    public User getUserByEmail(String email){
        try (SQLiteDatabase db = userSqlHelper.getReadableDatabase()) {
            String request = "SELECT * FROM " + userSqlHelper.NAME_OF_TABLE
                    + " WHERE " + SqlUserFields.email.toString() + " = '" + email + "';";
            Cursor cursor = db.rawQuery(request, null);
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(SqlUserFields.id.ordinal());
                String firstName = cursor.getString(SqlUserFields.first_name.ordinal());
                String lastName = cursor.getString(SqlUserFields.last_name.ordinal());
                String phone = cursor.getString(SqlUserFields.phone.ordinal());
                String password = cursor.getString(SqlUserFields.password.ordinal());
                String resource = cursor.getString(SqlUserFields.news_resource_link.ordinal());

                return new User(userId, firstName, lastName, email, phone, password, resource);
            }
            return null;
        }
    }


    public User getUserById(int id){
        try (SQLiteDatabase db = userSqlHelper.getReadableDatabase()) {
            String request = "SELECT * FROM " + userSqlHelper.NAME_OF_TABLE
                    + " WHERE " + SqlUserFields.id.toString() + " = " + String.valueOf(id) + ";";
            Cursor cursor = db.rawQuery(request, null);
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(SqlUserFields.id.ordinal());
                String firstName = cursor.getString(SqlUserFields.first_name.ordinal());
                String lastName = cursor.getString(SqlUserFields.last_name.ordinal());
                String phone = cursor.getString(SqlUserFields.phone.ordinal());
                String email = cursor.getString(SqlUserFields.email.ordinal());
                String password = cursor.getString(SqlUserFields.password.ordinal());
                String resource = cursor.getString(SqlUserFields.news_resource_link.ordinal());

                return new User(userId, firstName, lastName, email, phone, password, resource);
            }
            return null;
        }
    }

}
