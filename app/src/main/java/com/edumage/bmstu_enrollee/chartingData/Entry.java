package com.edumage.bmstu_enrollee.chartingData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Entry extends com.github.mikephil.charting.data.Entry implements  Comparable<Entry>, Cloneable{
    @Override
    public int compareTo(Entry o) {
        if (o.getX()==getX() && o.getY()==getY()){
            return 0;
        } else {
            return 1;
        }

    }

    public Entry(float x, float y){
        super(x,y);
    }


    public static List<com.github.mikephil.charting.data.Entry> toEntryList(List<Entry> entries){
        List<com.github.mikephil.charting.data.Entry> res=  new ArrayList<>();
        for (Entry entry:entries){
            res.add(new com.github.mikephil.charting.data.Entry(entry.getX(),entry.getY()));
        }
        return res;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Entry(getX(),getY());
    }
}
