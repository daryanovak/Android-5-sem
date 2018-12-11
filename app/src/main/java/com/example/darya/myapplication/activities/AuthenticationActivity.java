package com.example.darya.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.os.Bundle;

import com.example.darya.myapplication.R;
import com.example.darya.myapplication.interfaces.authentication.Authorization;
import com.example.darya.myapplication.interfaces.authentication.Registration;

public class AuthenticationActivity extends AppCompatActivity
        implements Authorization, Registration {

    private NavController navController;


    private final String USER_KEY = "user_key";

    private final HashManager hashManager = new HashManager(EncryptionAlgorithm.MD5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(IS_LOGOUT_KEY)){
            sessionController.logOut();
        }

        String userId = sessionController.getIdAuthorizedUser();
        if (userId != null){
            Toast toast = Toast.makeText(this, userId, Toast.LENGTH_SHORT);
            toast.show();
            logIn(userId);
        }

        setContentView(R.layout.activity_authentication);
        navController = Navigation.findNavController(this, R.id.auth_host_fragment);
    }

    @Override
    public void onGoToRegistrationButtonClick(){
        navController.navigate(R.id.registrationFragment);
    }

    @Override
    public void onLogInButtonClick(String email, String password)
            throws PasswordDoesNotMatchException, EmailNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null){
            if (user.getPassword().equals(hashManager.getHash(password))){
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
        sessionController.logIn(id);

        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(CURRENT_USER_ID_KEY, id);
        startActivity(intent);
    }

    @Override
    public void onRegistrationButtonClick(String firstName, String lastName, String email,
                                          String phone, String password) {
        User user = new User(0, firstName, lastName, email, phone, hashManager.getHash(password));
        userRepository.addUser(user);
        int id = userRepository.getUserByEmail(user.getEmail()).getId();
        logIn(String.valueOf(id));
    }

    @Override
    public void onBackButtonClick() {
        navController.popBackStack();
    }
}
