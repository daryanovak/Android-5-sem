package com.example.darya.myapplication.interfaces.authentication;


import com.example.darya.myapplication.exceptions.EmailNotFoundException;
import com.example.darya.myapplication.exceptions.PasswordDoesNotMatchException;

public interface Registration {
    void onRegistrationButtonClick(String firstName, String lastName, String email, String phone, String password);
    void onBackButtonClick();

}
