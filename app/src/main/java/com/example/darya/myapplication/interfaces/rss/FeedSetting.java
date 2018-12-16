package com.example.darya.myapplication.interfaces.rss;

import com.example.darya.myapplication.data.models.User;

public interface FeedSetting {
    void saveNewsResources(String resource);
    User getUser();
}
