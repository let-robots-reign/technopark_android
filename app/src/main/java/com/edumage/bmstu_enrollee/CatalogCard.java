package com.edumage.bmstu_enrollee;

public class CatalogCard {



    public static int CAMPUS_CARD_ID=1;
    public static int NEWS_CARD_ID=2;


    private String title;
    private int image;
    private int id=0;

    public CatalogCard(String text, int img) {
        image = img;
        title = text;
    }

    public CatalogCard(String text, int img, int id) {
        image = img;
        title = text;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
    public int getId(){
        return id;
    }

}
