package com.edumage.bmstu_enrollee;

import java.io.Serializable;

public class EGESubject implements Serializable {
    String name;
    int score;
    boolean isPassed;
    int img;

    public EGESubject(String name, int img_id) {
        this.name = name;
        this.img = img_id;
        isPassed = false;
    }

    public EGESubject(String name) {
        this.name = name;
        isPassed = false;
    }
}
