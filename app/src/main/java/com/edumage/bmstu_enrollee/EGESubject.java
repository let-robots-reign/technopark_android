package com.edumage.bmstu_enrollee;

import java.io.Serializable;

public class EGESubject implements Serializable {
    private String name;
    private int score;
    private boolean isPassed;
    private int img;

    public EGESubject(String name, int img_id) {
        this.name = name;
        this.img = img_id;
        isPassed = false;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public int getImg() {
        return img;
    }
}
