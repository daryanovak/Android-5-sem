package com.example.darya.myapplication.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.darya.myapplication.R;
import com.example.darya.myapplication.data.models.User;
import com.example.darya.myapplication.interfaces.UserEditFragment;

import androidx.fragment.app.Fragment;


public class FragmentEditUserInfo extends Fragment {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    private ImageView avatarView;

    private UserEditFragment userEditFragment;

    @Override
    public void onResume() {
        userEditFragment.setCurrenPage(true);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);
        User user = userEditFragment.getUser();
        initializationViewComponent(view, user);

        view.findViewById(R.id.save_user_info_button)
                .setOnClickListener(v -> {
                    updateUser();
                    userEditFragment.ClickButtonBack();
                });

        view.findViewById(R.id.back_from_edit_button).setOnClickListener(
                v-> userEditFragment.ClickButtonBack());

        userEditFragment.loadUserAvatar(avatarView);
        avatarView.setOnClickListener(v->userEditFragment.updatePhoto());

        if(savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable("image");
            avatarView.setImageBitmap(bitmap);
        }

        return view;
    }

    @Override
    public void onPause() {
        userEditFragment.setCurrenPage(false);
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserEditFragment) {
            userEditFragment = (UserEditFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement editUserInfo");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        BitmapDrawable drawable = (BitmapDrawable) avatarView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }

    private void initializationViewComponent(View parentContainer, User user) {
        avatarView = parentContainer.findViewById(R.id.edit_avatar_image_view);

        firstNameEditText = parentContainer.findViewById(R.id.first_name_edit);
        firstNameEditText.setText(user.getFirstName());

        lastNameEditText = parentContainer.findViewById(R.id.last_name_edit);
        lastNameEditText.setText(user.getLastName());

        phoneEditText = parentContainer.findViewById(R.id.phone_edit);
        phoneEditText.getText().insert(phoneEditText.getSelectionStart(), user.getPhone());

        emailEditText = parentContainer.findViewById(R.id.email_edit);
        emailEditText.setText(user.getEmail());


    }

    private void updateUser(){
        User user = new User(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                phoneEditText.getText().toString(),
                emailEditText.getText().toString()
        );
        userEditFragment.ClickButtonUdpate(user);
    }


}
