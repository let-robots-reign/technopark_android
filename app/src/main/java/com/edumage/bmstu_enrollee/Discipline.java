package com.edumage.bmstu_enrollee;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class Discipline implements Serializable {

    private String fullName;
    private String name;
    private String number;
    private String form;
    private boolean enabled = false;
    private int[] subjects;

    public static final int NUMBER_OF_PASSING_EXAMS=3;

    public boolean getStatus() {
        return enabled;
    }

    public void setStatus(boolean enabled) {
        this.enabled = enabled;
    }

    public Discipline(String fullName, String name, String number, String form) {
        this.fullName = fullName;
        this.name = name;
        this.number = number;
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getForm() {
        return form;
    }

    public String getFullName() {
        return fullName;
    }

    public void setSubjects(int[] subjects) {
        this.subjects = subjects;
    }

    public int[] getSubjects() {
        return subjects;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Discipline){
            Discipline discipline =(Discipline)obj;
            return discipline.fullName.equals(fullName);
        } else {
            return false;
        }

    }
}
