package com.edumage.bmstu_enrollee;

public class CafedraItem {

    String nameCaf, nomerCaf;


    public CafedraItem(String nomer, String name){
        nameCaf = name;
        nomerCaf = nomer;
    }

    public String getNameCaf() {
        return nameCaf;
    }

    public String getColCaf() {
        return nomerCaf;
    }

}
