package com.example.darya.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.darya.myapplication.R;
import com.example.darya.myapplication.data.models.User;
import com.example.darya.myapplication.interfaces.UserInfoFragment;

import androidx.fragment.app.Fragment;



public class FragmentUser extends Fragment {

    private UserInfoFragment userInfoFragment;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_user, container, false);
        User user = userInfoFragment.getUser();

        TextView name = view.findViewById(R.id.name_value);
        name.setText(String.format("%s %s",
                user.getFirstName(), user.getLastName()));
        TextView email = view.findViewById(R.id.email_value);
        email.setText(String.format("%s",
                user.getEmail()));
        TextView phone = view.findViewById(R.id.phone_value);
        phone.setText(String.format("%s",
                user.getPhone()));

        view.findViewById(R.id.button_click_edit).setOnClickListener(
                v -> userInfoFragment.ButtonEditClick());

        userInfoFragment.loadUserAvatar(view.findViewById(R.id.avatar_image_view));

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserInfoFragment) {
            userInfoFragment = (UserInfoFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement editUserInfo");
        }
    }

}
