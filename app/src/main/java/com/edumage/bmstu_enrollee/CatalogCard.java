package com.edumage.bmstu_enrollee;

public class CatalogCard {



    public static int ABOUT_CARD_ID = 0;
    public static int NEWS_CARD_ID = 1;
    public static int FACULTIES_CARD_ID = 2;
    public static int CAMPUS_CARD_ID = 3;
    public static int EVENTS_CARD_ID = 4;
    public static int APPLY_CARD_ID = 5;
    public static int INFO_CARD_ID = 6;


    private String title;
    private int image;
    private int id = 0;

    public CatalogCard(String text, int img, int id) {
        image = img;
        title = text;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
