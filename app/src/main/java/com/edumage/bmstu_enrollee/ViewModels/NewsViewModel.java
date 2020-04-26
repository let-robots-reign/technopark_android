package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.edumage.bmstu_enrollee.NewsItem;
import com.edumage.bmstu_enrollee.ParsingRepo.NewsParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private MutableLiveData<List<NewsItem>> newsList = new MutableLiveData<>();
    private MutableLiveData<String> newsContent = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsList.setValue(new ArrayList<NewsItem>());
        newsContent.setValue(null);
    }

    public LiveData<List<NewsItem>> getNewsList() {
        return newsList;
    }

    public MutableLiveData<String> getNewsContent() {
        return newsContent;
    }

    public void parseNewsList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<NewsItem> news = new ArrayList<>();
                NewsParsing newsParsing = NewsParsing.getInstance();
                try {
                    news.addAll(newsParsing.parseNewsList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsList.setValue(news);
                    }
                });
            }
        });
        thread.start();
    }

    public void parseNewsContent(final String URL) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String content = "Error: couldn't get news content";;
                NewsParsing newsParsing = NewsParsing.getInstance();
                try {
                    content = newsParsing.parseNewsContent(URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String finalContent = content;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsContent.setValue(finalContent);
                    }
                });
            }
        });
        thread.start();
    }
}
