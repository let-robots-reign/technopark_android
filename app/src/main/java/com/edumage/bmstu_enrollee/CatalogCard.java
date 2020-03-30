package com.edumage.bmstu_enrollee;

public class CatalogCard {
    private String title;
    private int image;

    public CatalogCard(String text, int img) {
        image = img;
        title = text;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
