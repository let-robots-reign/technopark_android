package com.edumage.bmstu_enrollee.ViewModels;

import android.os.AsyncTask;

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

    // TODO: replace AsyncTask inside following methods

    private void loadScores() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> scores = new ArrayList<>();
                String score = "Ошибка";
                for (String name : programsNames) {
                    try {
                        score = CurrentScoresParsing.getInstance().parseScore(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scores.add(score);
                }
                return scores;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                scoresLiveData.setValue(strings);
            }
        }.execute();
    }

    private void loadFiles() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> fileUrls = new ArrayList<>();
                String fileUrl = null;
                for (String name : programsNames) {
                    try {
                        fileUrl = CurrentFilesParsing.getInstance().parseFile(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileUrls.add(fileUrl);
                }
                return fileUrls;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                filesLiveData.setValue(strings);
            }
        }.execute();
    }

}