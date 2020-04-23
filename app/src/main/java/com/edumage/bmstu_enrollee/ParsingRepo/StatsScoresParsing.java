package com.edumage.bmstu_enrollee.ParsingRepo;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

        Element specialities = doc.select("table").first();
        Elements rows = specialities.select("tr");
        Elements yearsRange = rows.get(1).select("th > div");
        int startYear = Integer.parseInt(yearsRange.first().text().split(" ")[0]);
        int lastYear = Integer.parseInt(yearsRange.last().text().split(" ")[0]);
        // будем считать мин. проходной балл среди кафедр
        // сначала мин. баллы равны 310
        Integer[] minScores = new Integer[startYear - lastYear + 1];
        Arrays.fill(minScores, 310);

        Element row;
        Elements cols;
        for (int i = 3; i < rows.size(); ++i) {
            // проходим по каждому ряду таблицы, кроме заголовка
            row = rows.get(i);
            cols = row.select("td");
            // если нашли нужное направление
            if (cols.first().text().equals(programName)) {
                // число кафедр на направлении
                int numOfRows = 1;
                if (cols.first().hasAttr("rowspan")) {
                    numOfRows = Integer.parseInt(cols.first().attr("rowspan"));
                }

                // проходимся по каждой кафедре
                for (int r = i; r < i + numOfRows; ++r) {
                    row = rows.get(r);
                    cols = row.select("td");
                    // на каждой кафедре смотрим проходные баллы
                    int scoreIndex = 0;
                    for (int j = (r == i) ? 6 : 3; j < cols.size(); j += 2) {
                        String scoreValue = cols.get(j).text();
                        if (isNumeric(scoreValue)) {
                            int score = Integer.parseInt(scoreValue);
                            if (score < minScores[scoreIndex]) {
                                minScores[scoreIndex] = score;
                            }
                        }
                        ++scoreIndex;
                    }
                }
                break;
            }
        }

        // x-values need to be sorted
        List<Integer> years = new ArrayList<>();
        for (int y = lastYear; y <= startYear; ++y) years.add(y);
        Collections.reverse(Arrays.asList(minScores));

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < years.size(); ++i) {
            entries.add(new Entry(years.get(i), minScores[i]));
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
