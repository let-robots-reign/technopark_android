package com.edumage.bmstu_enrollee.ParsingRepo;

import android.util.Log;

import com.edumage.bmstu_enrollee.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsParsing {
    private final String BASE_URL = "https://bmstu.ru";
    private static NewsParsing instance;

    public static NewsParsing getInstance() {
        if (instance == null) {
            instance = new NewsParsing();
        }
        return instance;
    }

    public List<NewsItem> parseNewsList() throws IOException {
        final String URL = "https://bmstu.ru/mstu/info/bauman-news/";
        List<NewsItem> news = new ArrayList<>();
        Document doc = Jsoup.connect(URL).get();

        Elements items = doc.select("div.col-md-3");
        Log.d("PARSING", "parseNewsList: " + items.size());
        Element link;
        String title, linkURL, imageURL;
        for (Element item : items) {
            title = item.select("a").last().text();
            link = item.select("a").first();
            linkURL = BASE_URL + link.attr("href");
            if (link.select("img").size() != 0) {
                imageURL = BASE_URL + link.select("img").attr("src");
            } else {
                Log.d("PARSING", "parseNewsList: null image");
                imageURL = null;
            }
            news.add(new NewsItem(imageURL, title));
        }
        return news;
    }

    public void parseNewsItem(final String URL) throws IOException {
        Document doc = Jsoup.connect(URL).get();
        StringBuilder content = new StringBuilder();

        for (Element text : doc.select("div.b-newsdetail-text").select("p,span")) {
            content.append(text.text());
        }
        Elements links = doc.select("div.b-newsdetail-text > div.j-marg2").select("a");
        for (Element link : links) {
            content.append(link.text()).append(":\n");
            content.append(BASE_URL).append(link.attr("href")).append("\n");
        }
    }
}
