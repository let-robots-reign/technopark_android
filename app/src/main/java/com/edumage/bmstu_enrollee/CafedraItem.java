package com.edumage.bmstu_enrollee;

public class CafedraItem {

    String nameCaf, shortDesc;


    public CafedraItem(String name, String shortDesribe){
        nameCaf = name;
        shortDesc = shortDesribe;
    }

    public String getNameCaf() {
        return nameCaf;
    }
    public String getShortDesc() { return shortDesc;}
}
