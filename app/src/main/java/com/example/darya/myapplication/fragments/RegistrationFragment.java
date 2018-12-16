package com.example.darya.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.darya.myapplication.R;
import com.example.darya.myapplication.interfaces.authentication.Registration;
import com.google.android.material.textfield.TextInputLayout;


public class RegistrationFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText firsNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;


    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputLayout firstNameTextInputLayout;
    private TextInputLayout lastNameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;

    private Registration registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        initializationViewComponents(view);

        view.findViewById(R.id.registration_button).setOnClickListener(v -> createNewUser());
//        view.findViewById(R.id.back_from_registration_button).setOnClickListener(
//                v -> registration.onBackButtonClick());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Registration) {
            registration = (Registration) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Registration");
        }
    }

    private boolean isValidForm(){
        String email = emailEditText.getText().toString();
        if (email.isEmpty()){
            emailTextInputLayout.setError(getResources().getString(R.string.message_for_empty_email));
        }

        String password = passwordEditText.getText().toString();
        if (password.isEmpty()){
            passwordTextInputLayout.setError(getResources().getString(R.string.message_for_empty_password));
        }

        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (confirmPassword.isEmpty()){
            confirmPasswordTextInputLayout.setError(getResources().getString(R.string.message_for_empty_confirm_password));
        }

        String firstName = firsNameEditText.getText().toString();
        if (firstName.isEmpty()){
            firstNameTextInputLayout.setError(getResources().getString(R.string.message_for_empty_first_name));
        }

        String lastName = lastNameEditText.getText().toString();
        if (lastName.isEmpty()){
            lastNameTextInputLayout.setError(getResources().getString(R.string.message_for_empty_last_name));
        }

        String phone = phoneEditText.getText().toString();
        if (phone.isEmpty()){
            phoneTextInputLayout.setError(getResources().getString(R.string.message_for_empty_phone));
        }

        if (!password.equals(confirmPassword)){
            passwordTextInputLayout.setError(getResources().getString(R.string.massage_for_not_match_password));
        }

        return !email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()
                && !phone.isEmpty() && password.equals(confirmPassword);
    }

    private void createNewUser(){
        if (isValidForm()){
            registration.onRegistrationButtonClick(
                    firsNameEditText.getText().toString(),
                    lastNameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    passwordEditText.getText().toString());
        }
    }

    private void initializationViewComponents(View view){
        emailEditText = view.findViewById(R.id.email_for_registration);
        emailTextInputLayout = view.findViewById(R.id.email_for_registration_text_input_layout);

        passwordEditText = view.findViewById(R.id.password_for_registration);
        passwordTextInputLayout = view.findViewById(R.id.password_for_registration_text_input_layout);

        confirmPasswordEditText = view.findViewById(R.id.confirm_password_for_registration);
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirm_password_for_registration_text_input_layout);

        firsNameEditText = view.findViewById(R.id.first_name_for_registration);
        firstNameTextInputLayout = view.findViewById(R.id.first_name_for_registration_text_input_layout);

        lastNameEditText = view.findViewById(R.id.last_name_for_registration);
        lastNameTextInputLayout = view.findViewById(R.id.last_name_for_registration_text_input_layout);

        phoneEditText = view.findViewById(R.id.phone_for_registration);
        phoneTextInputLayout = view.findViewById(R.id.phone_for_registration_text_input_layout);
    }

}
