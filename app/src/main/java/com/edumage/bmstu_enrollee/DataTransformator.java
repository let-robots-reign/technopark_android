package com.edumage.bmstu_enrollee;

import android.content.Context;
import android.content.Entity;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class DataTransformator {

    Context context;









    public static class PassScoreComponent{

        final int BUDGET_TYPE=0;
        final int TARGET_TYPE=1;

        ArrayList<Integer> years;
        ArrayList<Integer> score;
        ArrayList<Entry> entries;

        public ArrayList<Entry> getEntries(){
            return entries;
        }

        private PassScoreComponent(ArrayList<Integer> years, ArrayList<Integer> score){
            entries= new ArrayList<>();
            for (int i=0; i<years.size(); i++){
                entries.add(new Entry(years.get(i),score.get(i)));
            }

        }


    }



    public static PassScoreComponent LoadSetPassScore(String discipline, int type){

       //temporary realization

       ArrayList<Integer> years =  new ArrayList<>();
       for (int i=2009; i<2020; i++)years.add(i);


       ArrayList<Integer> score = new ArrayList<>();
       for (int i=0; i<years.size(); i++){
           score.add((int)(250+Math.round(Math.random()*50)));
       }
        PassScoreComponent scoreSet =  new PassScoreComponent(years,score);

        return scoreSet;

    }













}
