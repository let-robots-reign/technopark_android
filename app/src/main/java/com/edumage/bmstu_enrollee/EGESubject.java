package com.edumage.bmstu_enrollee;

public class EGESubject {

    String name;
    int score;
    boolean isPassed;
    int img;


    public EGESubject(String name, int img_id){
       this.name  = name;
       this.img=img_id;
    }

    public EGESubject(String name){
        this.name=name;
        isPassed=false;
    }

}
