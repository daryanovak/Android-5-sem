package com.example.darya.myapplication.interfaces.authentication;

import com.example.darya.myapplication.exceptions.EmailNotFoundException;
import com.example.darya.myapplication.exceptions.PasswordDoesNotMatchException;

public interface Authorization {
    void onGoToRegistrationButtonClick();
    void onLogInButtonClick(String email, String password) throws PasswordDoesNotMatchException, EmailNotFoundException;
}
