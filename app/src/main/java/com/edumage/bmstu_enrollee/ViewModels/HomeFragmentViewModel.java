package com.edumage.bmstu_enrollee.ViewModels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edumage.bmstu_enrollee.ParsingRepo.CurrentScoresParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private final MutableLiveData<List<String>> parsingLiveData = new MutableLiveData<>();
    private List<String> programsNames;

    public void init(List<String> names) {
        programsNames = names;
        loadScores();
    }

    public LiveData<List<String>> getParsingScores() {
        return parsingLiveData;
    }

    private void loadScores() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> results = new ArrayList<>();
                String score = "Ошибка";
                for (String name : programsNames) {
                    try {
                        score = CurrentScoresParsing.getInstance().parseScore(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    results.add(score);
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                parsingLiveData.setValue(strings);
            }
        }.execute();
    }

}