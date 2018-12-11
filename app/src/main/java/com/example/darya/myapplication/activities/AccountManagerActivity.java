package com.example.darya.myapplication.activities;

import android.os.Bundle;

import com.example.darya.myapplication.data.AccountsManager;
import com.example.darya.myapplication.data.db.UserSQLiteRepository;

import androidx.appcompat.app.AppCompatActivity;

public class AccountManagerActivity extends AppCompatActivity {
    protected final String IS_LOGOUT_KEY = "is_logout";
    protected UserSQLiteRepository userSQLiteRepository;
    protected AccountsManager accountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountsManager(this);
        userSQLiteRepository = new UserSQLiteRepository(this);
    }
}
