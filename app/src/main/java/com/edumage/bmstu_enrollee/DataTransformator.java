package com.edumage.bmstu_enrollee;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class DataTransformator {

    public static class PassScoreComponent {

        final int BUDGET_TYPE = 0;
        final int TARGET_TYPE = 1;

        ArrayList<Integer> years;
        ArrayList<Integer> score;
        ArrayList<Entry> entries;

        public ArrayList<Entry> getEntries() {
            return entries;
        }

        PassScoreComponent(List<Integer> years, List<Integer> score) {
            entries = new ArrayList<>();
            for (int i = 0; i < years.size(); i++) {
                entries.add(new Entry(years.get(i), score.get(i)));
            }
        }
    }

    public static PassScoreComponent LoadSetPassScore(String discipline, int type) {
        //temporary realization
        List<Integer> years = new ArrayList<>();
        for (int i = 2009; i < 2020; i++) years.add(i);

        ArrayList<Integer> score = new ArrayList<>();
        for (int i = 0; i < years.size(); i++) {
            score.add((int) (250 + Math.round(Math.random() * 50)));
        }

        return new PassScoreComponent(years, score);
    }
}
