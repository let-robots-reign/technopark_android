package com.edumage.bmstu_enrollee.ParsingRepo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrentFilesParsing {

    private static CurrentFilesParsing instance;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public static void init(){
        instance= new CurrentFilesParsing();
    }

    public void pushTask(Runnable runnable){
        executorService.execute(runnable);
    }

    public static CurrentFilesParsing getInstance() {
        if (instance == null) {
            instance = new CurrentFilesParsing();
        }
        return instance;
    }

    public String parseFile(String programName) throws IOException {
        String url = "http://priem.bmstu.ru/lists.html";
        Document doc = Jsoup.connect(url).get();
        String specialityTitle;
        String fileUrl = null;

        Elements specialities = doc.select("div.speciality-header");
        for (Element speciality : specialities) {
            specialityTitle = speciality.select(
                    "table.pretty-table > tbody > tr > td > h3").text();
            fileUrl = speciality.select("table.pretty-table > tbody > tr > td")
                    .get(1).select("a").attr("abs:href");
            if (specialityTitle.equals(programName)) {
                return fileUrl;
            }
        }
        return fileUrl;
    }
}