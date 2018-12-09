package com.example.darya.myapplication.interfaces;

import android.widget.ImageView;

import com.example.darya.myapplication.data.models.User;

public interface UserInfoFragment {
    User getUser();
    void ButtonEditClick();
    void loadUserAvatar(ImageView view);
}
