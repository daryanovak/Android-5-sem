package com.example.darya.myapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.darya.myapplication.R;
import com.example.darya.myapplication.interfaces.rss.FeedSetting;
import com.google.android.material.textfield.TextInputLayout;


public class FeedSettingFragment extends Fragment {
    private FeedSetting feedSetting;

    private TextView newsSourceTextView;
    private TextInputLayout newsSourceTextInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_setting, container, false);

        newsSourceTextView = view.findViewById(R.id.first_news_source);
        newsSourceTextInputLayout = view.findViewById(R.id.first_news_source_text_input_layout);

        newsSourceTextView.setText(feedSetting.getUser().getNewsResource());
        view.findViewById(R.id.save_news_resource_button).setOnClickListener(v ->{
            if (isValidSource()){
                feedSetting.saveNewsResources(newsSourceTextView.getText().toString());
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FeedSetting) {
            feedSetting = (FeedSetting) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FeedSetting");
        }
    }

    public boolean isValidSource(){
        if (!newsSourceTextView.getText().toString().isEmpty()){
            return true;
        }
        newsSourceTextInputLayout.setError("Ooops");
        return false;
    }
}
