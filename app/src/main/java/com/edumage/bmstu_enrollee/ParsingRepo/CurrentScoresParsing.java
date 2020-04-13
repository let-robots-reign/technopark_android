package com.edumage.bmstu_enrollee.ParsingRepo;

import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrentScoresParsing {

    private static CurrentScoresParsing instance;
    private List<String> scores = new ArrayList<>();

    public static CurrentScoresParsing getInstance() {
        if (instance == null) {
            instance = new CurrentScoresParsing();
        }
        return instance;
    }

    public String parseScore(String programName) throws IOException {
        String URL = "http://priem.bmstu.ru/ru/points";
        Document doc = Jsoup.connect(URL).get();
        String score = "Ошибка";

        Elements specialities = doc.select("div.speciality-container");
        Elements specialityInfo;
        String specialityScore;
        String specialityTitle;

        for (Element speciality : specialities) {
            // all the info about speciality (title and score)
            specialityInfo = speciality.select("div.speciality-header > table.pretty-table > tbody > tr > td");
            specialityTitle = specialityInfo.select("h3").text();
            specialityScore = specialityInfo.last().select("b").text();

            // if title equals any of the programs chosen by user
            if (specialityTitle.equals(programName)) {
                score = specialityScore;
                break;
            }
        }
        return score;
    }
}