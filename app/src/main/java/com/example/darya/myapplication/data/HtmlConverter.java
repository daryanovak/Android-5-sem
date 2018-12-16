package com.example.darya.myapplication.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlConverter {
    private Document document;

    public HtmlConverter(String html){
        document = Jsoup.parse(html);
    }

    public String getImageSource(){
        return document.select("img").first().attr("src");
    }

    public String getText(){
        Document newDoc = document.clone();
        newDoc.select("img").remove();
        newDoc.select("br").remove();
        return  newDoc.text();
    }
}
