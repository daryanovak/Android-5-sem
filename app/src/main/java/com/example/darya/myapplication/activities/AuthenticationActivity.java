package com.example.darya.myapplication.activities;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.example.darya.myapplication.R;
import com.example.darya.myapplication.data.HashManager;
import com.example.darya.myapplication.data.models.User;
import com.example.darya.myapplication.exceptions.EmailNotFoundException;
import com.example.darya.myapplication.exceptions.PasswordDoesNotMatchException;
import com.example.darya.myapplication.interfaces.authentication.Authorization;
import com.example.darya.myapplication.interfaces.authentication.Registration;

public class AuthenticationActivity extends AccountManagerActivity
        implements Authorization, Registration {

    private NavController navController;


    private final String USER_KEY = "user_key";

    private final HashManager hashManager = new HashManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(IS_LOGOUT_KEY)){
            accountManager.logOut();
        }

        String userId = accountManager.getIdAuthorizedUser();
        if (userId != null){
            logIn(userId);
        }

        setContentView(R.layout.activity_authorization);
        navController = Navigation.findNavController(this, R.id.auth_host_fragment);
    }

    @Override
    public void onGoToRegistrationButtonClick(){
        navController.navigate(R.id.registrationFragment);
    }

    @Override
    public void onLogInButtonClick(String email, String password)
            throws PasswordDoesNotMatchException, EmailNotFoundException {
        User user = userSQLiteRepository.getUserByEmail(email);
        if (user != null){
            if (user.getHashedPassword().equals(hashManager.getHash(password))){
                logIn(String.valueOf(user.getId()));
            }
            else {
                throw new PasswordDoesNotMatchException();
            }
        }
        else {
            throw new EmailNotFoundException();
        }
    }

    public void logIn(String id){
        accountManager.logIn(id);

        Intent intent = new Intent(this, Main2Activity.class);
//        intent.putExtra(CURRENT_USER_ID_KEY, id);
        startActivity(intent);
    }

    @Override
    public void onRegistrationButtonClick(String firstName, String lastName, String email,
                                          String phone, String password) {
        User user = new User(0, firstName, lastName, email, phone, hashManager.getHash(password));
        userSQLiteRepository.addUser(user);
        int id = userSQLiteRepository.getUserByEmail(user.getEmail()).getId();
        logIn(String.valueOf(id));
    }

    @Override
    public void onBackButtonClick() {
        navController.popBackStack();
    }
}
