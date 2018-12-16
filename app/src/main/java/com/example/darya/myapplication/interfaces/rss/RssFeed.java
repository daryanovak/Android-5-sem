package com.example.darya.myapplication.interfaces.rss;

import com.example.darya.myapplication.data.models.RssFeedModel;

import java.util.List;

public interface RssFeed {
    String getRssResource();
    void redirectedToSettings();
    void saveRssInCache(List<RssFeedModel> feeds);
    void checkInternet();
}
