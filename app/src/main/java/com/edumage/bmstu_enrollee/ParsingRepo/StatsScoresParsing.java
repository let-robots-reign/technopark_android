package com.edumage.bmstu_enrollee.ParsingRepo;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsScoresParsing {

    private final String URL = "https://bmstu.ru/abitur/general/passing_scores/";
    private static StatsScoresParsing instance;

    public static StatsScoresParsing getInstance() {
        if (instance == null) {
            instance = new StatsScoresParsing();
        }
        return instance;
    }

    public List<Entry> parseBudgetFundedScores(String programName) throws IOException {
        Document doc = Jsoup.connect(URL).get();

        List<Integer> years = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        Element specialities = doc.select("table").first();
        Elements rows = specialities.select("tr");
        int startYear = Integer.parseInt(rows.get(1).select("th > div")
                .first().text().split(" ")[0]);

        Element row;
        Elements cols;
        String scoreValue;
        for (int i = 3; i < rows.size(); ++i) {
            row = rows.get(i);
            cols = row.select("td");
            if (cols.first().text().equals(programName)) {
                for (int j = 6; j < cols.size(); j += 2) {
                    scoreValue = cols.get(j).text();
                    if (isNumeric(scoreValue)) {
                        years.add(startYear);
                        scores.add(Integer.parseInt(scoreValue));
                    }
                    --startYear;
                }
            }
        }

        // x-values need to be sorted
        Collections.reverse(years);
        Collections.reverse(scores);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < years.size(); ++i) {
            entries.add(new Entry(years.get(i), scores.get(i)));
        }

        return entries;
    }

    private static boolean isNumeric(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
