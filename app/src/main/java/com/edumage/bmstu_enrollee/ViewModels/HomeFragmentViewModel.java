package com.edumage.bmstu_enrollee.ViewModels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edumage.bmstu_enrollee.ParsingRepo.CurrentFilesParsing;
import com.edumage.bmstu_enrollee.ParsingRepo.CurrentScoresParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private final MutableLiveData<List<String>> scoresLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> filesLiveData = new MutableLiveData<>();
    private List<String> programsNames;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public void init(List<String> names) {
        programsNames = names;
        loadScores();
        loadFiles();
    }

    public LiveData<List<String>> getParsingScores() {
        return scoresLiveData;
    }

    public LiveData<List<String>> getParsingFiles() {
        return filesLiveData;
    }

    private void loadScores() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> scores = new ArrayList<>();
                String score = "Ошибка";
                for (String name : programsNames) {
                    try {
                        score = CurrentScoresParsing.getInstance().parseScore(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scores.add(score);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scoresLiveData.setValue(scores);
                        }
                    });

                }
            }
        });
        thread.start();
    }

    private void loadFiles() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> fileUrls = new ArrayList<>();
                String fileUrl = null;
                for (String name : programsNames) {
                    try {
                        fileUrl = CurrentFilesParsing.getInstance().parseFile(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileUrls.add(fileUrl);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            filesLiveData.setValue(fileUrls);
                        }
                    });
                }
            }
        });
        thread.start();
    }
}