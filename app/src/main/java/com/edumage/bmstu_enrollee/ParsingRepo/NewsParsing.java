package com.edumage.bmstu_enrollee.ParsingRepo;

import com.edumage.bmstu_enrollee.FeedType;
import com.edumage.bmstu_enrollee.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsParsing {
    private final String BASE_URL = "https://bmstu.ru";
    private static NewsParsing instance;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static NewsParsing getInstance() {
        if (instance == null) {
            instance = new NewsParsing();
        }
        return instance;
    }

    public void pushTask(Runnable runnable){
     executorService.execute(runnable);
    }

    public static void init(){
        instance= new NewsParsing();
    }

    public List<NewsItem> parseNewsList(FeedType type) throws IOException {
        List<NewsItem> news = new ArrayList<>();

        final String URL;
        if (type == FeedType.NEWS) {
            URL = "https://bmstu.ru/mstu/info/bauman-news/";
        } else if (type == FeedType.EVENTS) {
            URL = "https://www.bmstu.ru/mstu/info/upcoming-events/";
        } else {
            return news;
        }

        Document doc = Jsoup.connect(URL).get();
        Elements items = doc.select("div.col-md-3");
        Element link;
        String title, linkURL, imageURL;
        for (Element item : items) {
            title = item.select("a").last().text();
            link = item.select("a").first();
            linkURL = BASE_URL + link.attr("href");
            if (item.select("img").size() != 0) {
                imageURL = BASE_URL + item.select("img.img-responsive").attr("src");
            } else {
                imageURL = null;
            }
            news.add(new NewsItem(imageURL, title, linkURL));
        }
        return news;
    }

    public String parseNewsContent(final String URL) throws IOException {
        Document doc = Jsoup.connect(URL).get();
        StringBuilder content = new StringBuilder();

        Elements texts = doc.select("div.b-newsdetail-text").select("p,span");
        String result;
        for (Element text : texts) {
            result = text.text().replaceAll("\u00a0", "").trim();
            if (content.indexOf(result) == -1) {
                content.append(result).append("\n\n");
            }
            Element link_under_span = text.select("a").first();
            if (link_under_span != null) {
                String linkHref = link_under_span.attr("href")
                        .replace("http:", "https:");
                if (linkHref.charAt(0) == '/') {
                    linkHref = BASE_URL + linkHref;
                }

                if (content.indexOf(linkHref) == -1) {
                    content.append(linkHref).append("\n\n");
                }
            }
        }
        Elements links = doc.select("div.b-newsdetail-text > div.j-marg2").select("a");
        for (Element link : links) {
            String resultText = link.text().replaceAll("\u00a0", "").trim();
            String resultLink = link.attr("href")
                    .replace("http:", "https:");

            if (content.indexOf(resultText) == -1 && content.indexOf(resultLink) == -1) {
                content.append(resultText).append(":\n");
                content.append(BASE_URL).append(resultLink).append("\n");
            }
        }

        return content.toString();
    }
}
