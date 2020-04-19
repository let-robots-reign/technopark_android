package com.edumage.bmstu_enrollee;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class Discipline implements Serializable {

    private String name;
    private String number;
    private String description;
    private String form;
    private boolean enabled = false;


    public boolean getStatus() {
        return enabled;
    }

    public void setStatus(boolean enabled) {
        this.enabled = enabled;
    }


    public Discipline(String name, String number, String form) {
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

    public String getDescription() {
        return description;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public static ArrayList<Discipline> LoadDisciplines(Context context) {
        String[] array = context.getResources().getStringArray(R.array.disciplines);
        ArrayList<Discipline> list = new ArrayList<>();
        for (String value : array) {
            String[] s = value.split(" ");
            String number = s[0];
            StringBuilder name = new StringBuilder();
            for (int k = 1; k < s.length - 1; k++) {
                name.append(s[k]).append(" ");
            }
            String form = s[s.length - 1];
            form = form.substring(1, form.length() - 1);
            list.add(new Discipline(name.toString(), number, form));
        }
        return list;
    }

    public static String[] LoadStringArray(Context context) {
        return context.getResources().getStringArray(R.array.disciplines);
    }
}
