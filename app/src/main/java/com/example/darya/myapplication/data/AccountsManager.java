package com.example.darya.myapplication.data;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

public class AccountsManager {
    private final String CURRENT_USER = "current_user";
    private final String PREFERENCES_NAME = "user's_manager";

    private SharedPreferences preferences;



    public AccountsManager(AppCompatActivity context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }

    public String getIdAuthorizedUser() {
        return preferences.contains(CURRENT_USER) ? preferences.getString(CURRENT_USER, null) : null;

    }

    public void logIn(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENT_USER, id);
        editor.apply();
    }

    public void logOut() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CURRENT_USER);
        editor.apply();
    }
}
