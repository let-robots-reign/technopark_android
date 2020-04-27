package com.edumage.bmstu_enrollee;

public class NewsItem {
    private String imgURL;
    private String title;
    private String linkURL;

    public NewsItem(String img, String text, String link){
        imgURL = img;
        title = text;
        linkURL = link;

    }

    public String getImgURL() {
        return imgURL;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkURL() {
        return linkURL;
    }

}
