package com.edumage.bmstu_enrollee.ParsingRepo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrentScoresParsing {

    private final String URL = "http://priem.bmstu.ru/ru/points";
    private static CurrentScoresParsing instance;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void pushTask(Runnable runnable){
        executorService.execute(runnable);
    }

    public static void init(){
        instance= new CurrentScoresParsing();
    }

    public static CurrentScoresParsing getInstance() {
        if (instance == null) {
            instance = new CurrentScoresParsing();
        }
        return instance;
    }




    public String getLastUpdate() throws IOException {
        Document doc = Jsoup.connect(URL).get();
        return doc.select("div.float-right > b").text();
    }

    public String parseScore(String programName) throws IOException {
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
            specialityScore = specialityInfo.get(specialityInfo.size() - 2).select("b").text();

            // if title equals the program chosen by user
            if (specialityTitle.equals(programName)) {
                score = specialityScore;
                break;
            }
        }
        return score;
    }
}