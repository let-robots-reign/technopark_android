package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.ParsingRepo.StatsScoresParsing;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatsFragmentViewModel extends AndroidViewModel {
    private DbRepository repository;
    private final MutableLiveData<List<Entry>> budgetFundedScores = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> industryFundedScores = new MutableLiveData<>();
    private final MutableLiveData<Boolean> finishedParsing = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public StatsFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public void init(String programName) {
        budgetFundedScores.setValue(new ArrayList<Entry>());
        industryFundedScores.setValue(new ArrayList<Integer>());
        loadBudgetFundedScores(programName);
    }

    public List<ChosenProgram> getAllChosenPrograms() {
        return repository.getAllChosenPrograms();
    }

    public LiveData<List<Entry>> getBudgetFundedScores() {
        return budgetFundedScores;
    }

    public LiveData<List<Integer>> getIndustryFundedScores() {
        return industryFundedScores;
    }

    public MutableLiveData<Boolean> getFinishedParsing() {
        return finishedParsing;
    }

    public void loadBudgetFundedScores(final String programName) {
        finishedParsing.setValue(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Entry> scores = new ArrayList<>();
                StatsScoresParsing instance = StatsScoresParsing.getInstance();

                try {
                    scores.addAll(instance.parseBudgetFundedScores(programName));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        budgetFundedScores.setValue(scores);
                        finishedParsing.setValue(true);
                    }
                });
            }
        });
        thread.start();
    }
}
