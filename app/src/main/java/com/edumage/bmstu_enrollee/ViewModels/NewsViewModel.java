package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.edumage.bmstu_enrollee.ParsingRepo.NewsParsing;

import java.io.IOException;

public class NewsViewModel extends AndroidViewModel {
    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public void parseNewsList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsParsing newsParsing = NewsParsing.getInstance();
                try {
                    newsParsing.parseNewsList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void parseNewsItem() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsParsing newsParsing = NewsParsing.getInstance();
                try {
                    newsParsing.parseNewsList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
