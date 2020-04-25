package com.edumage.bmstu_enrollee;

public class NewsItem {
    private String imgURL;
    private String textNews;

    public NewsItem(String img, String text){
        imgURL = img;
        textNews = text;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getTextNews() {
        return textNews;
    }
}
