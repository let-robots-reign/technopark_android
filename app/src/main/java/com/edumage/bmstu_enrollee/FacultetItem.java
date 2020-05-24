package com.edumage.bmstu_enrollee;

public class FacultetItem {

    String nameShort;
    String nameLong;
    int image;

    public FacultetItem(String nm, String name, int img){
        nameShort = nm;
        nameLong = name;
        image = img;
    }

    public String getNameShort() {
        return nameShort;
    }

    public String getNameLong() {
        return nameLong;
    }

    public int getImage() {
        return image;
    }
}
