package com.edumage.bmstu_enrollee.ParsingRepo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public List<Integer> parseBudgetFundedScores(String programName) throws IOException {
        Document doc = Jsoup.connect(URL).get();
        List<Integer> scores = new ArrayList<>();

        Elements specialities = doc.select("table.table > tbody > tr");

        scores = Arrays.asList(220, 250, 270);
        return scores;
    }
}
