package com.example.darya.myapplication.interfaces;

import android.widget.ImageView;

import com.example.darya.myapplication.data.models.User;

public interface UserEditFragment {
    User getUser();

    void ClickButtonUdpate(User user);
    void ClickButtonBack();
    void loadUserAvatar(ImageView view);
    void updatePhoto();
}
