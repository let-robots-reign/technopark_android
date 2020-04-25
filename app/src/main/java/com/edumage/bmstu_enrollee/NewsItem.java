package com.edumage.bmstu_enrollee;

public class NewsItem {
    private int imgNews;
    private String textNews;

    public NewsItem(int img, String text){
        imgNews = img;
        textNews = text;
    }

    public int getImgNews() {
        return imgNews;
    }

    public String getTextNews() {
        return textNews;
    }
}
