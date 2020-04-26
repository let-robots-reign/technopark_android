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

import static com.edumage.bmstu_enrollee.ConnectionCheck.hasInternetAccess;
import static com.edumage.bmstu_enrollee.ConnectionCheck.isNetworkConnected;

public class NewsViewModel extends AndroidViewModel {
    private MutableLiveData<List<NewsItem>> newsList = new MutableLiveData<>();
    private MutableLiveData<String> newsContent = new MutableLiveData<>();
    private MutableLiveData<Boolean> hasConnection = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsList.setValue(new ArrayList<NewsItem>());
        newsContent.setValue(null);
        hasConnection.setValue(true);
    }

    public MutableLiveData<Boolean> getHasConnection() {
        return hasConnection;
    }

    public LiveData<List<NewsItem>> getNewsList() {
        return newsList;
    }

    public MutableLiveData<String> getNewsContent() {
        return newsContent;
    }

    private boolean getConnectionStatus() {
        return isNetworkConnected(getApplication()) && hasInternetAccess();
    }

    public void parseNewsList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean conn = getConnectionStatus();
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
                        hasConnection.setValue(conn);
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
                final boolean conn = getConnectionStatus();
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
                        hasConnection.setValue(conn);
                        newsContent.setValue(finalContent);
                    }
                });
            }
        });
        thread.start();
    }
}
