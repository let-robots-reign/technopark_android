package com.edumage.bmstu_enrollee.ParsingRepo;

import com.edumage.bmstu_enrollee.chartingData.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatsScoresParsing {

    private final Integer MAX_SCORE = 311;
    private static StatsScoresParsing instance;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public void pushTask(Runnable runnable){
        executorService.execute(runnable);
    }

    public static void init(){
        instance= new StatsScoresParsing();
    }

    public static StatsScoresParsing getInstance() {
        if (instance == null) {
            instance = new StatsScoresParsing();
        }
        return instance;
    }

    public List<Entry> parseBudgetFundedScores(String programName) throws IOException {
        return parseScores(programName, 6, 3);
    }

    public List<Entry> parseIndustryFundedScores(String programName) throws IOException {
        return parseScores(programName, 7, 4);
    }

    private List<Entry> parseScores(String programName, int firstRowStart, int notFirstRowStart)
            throws IOException {
        String URL = "https://bmstu.ru/abitur/general/passing_scores/";
        Document doc = Jsoup.connect(URL).get();

        Element specialities = doc.select("table").first();
        Elements rows = specialities.select("tr");
        Elements yearsRange = rows.get(1).select("th > div");
        int startYear = Integer.parseInt(yearsRange.first().text().split(" ")[0]);
        int lastYear = Integer.parseInt(yearsRange.last().text().split(" ")[0]);
        // будем считать мин. проходной балл среди кафедр
        // сначала мин. баллы равны MAX_SCORE
        Integer[] minScores = new Integer[startYear - lastYear + 1];
        Arrays.fill(minScores, MAX_SCORE);

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
                    for (int j = (r == i) ? firstRowStart : notFirstRowStart; j < cols.size(); j += 2) {
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
        for (int y = lastYear; y <= startYear; ++y) {
            if (!minScores[startYear - y].equals(MAX_SCORE)) {
                years.add(y);
            }
        }

        List<Integer> scores = reverseAndFilterScores(minScores);

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

    private List<Integer> reverseAndFilterScores(Integer[] array) {
        List<Integer> res = new ArrayList<>();
        for (int i = array.length - 1; i >= 0; --i) {
            if (!array[i].equals(MAX_SCORE)) {
                res.add(array[i]);
            }
        }
        return res;
    }
}
