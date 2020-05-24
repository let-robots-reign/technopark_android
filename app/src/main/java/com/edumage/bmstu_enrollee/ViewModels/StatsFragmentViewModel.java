package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.ParsingRepo.StatsScoresParsing;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.edumage.bmstu_enrollee.ConnectionCheck.hasInternetAccess;
import static com.edumage.bmstu_enrollee.ConnectionCheck.isNetworkConnected;

public class StatsFragmentViewModel extends AndroidViewModel {
    private DbRepository repository;
    private final MutableLiveData<Boolean> hasConnection = new MutableLiveData<>();
    private final MutableLiveData<List<Entry>> budgetFundedScores = new MutableLiveData<>();
    private final MutableLiveData<List<Entry>> industryFundedScores = new MutableLiveData<>();
    public final MediatorLiveData<List<List<Entry>>> mainData= new MediatorLiveData<>();
    private final MutableLiveData<Boolean> finishedParsing = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public static final int BUDGET_INDEX=0;
    public static final int INDUSTRY_INDEX=1;


    public StatsFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
        mainData.addSource(budgetFundedScores, new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                List<List<Entry>> list = mainData.getValue();
                if(list==null) {
                    list = new ArrayList<>();
                    list.add(new ArrayList<Entry>());
                    list.add(new ArrayList<Entry>());
                }
                list.set(BUDGET_INDEX,entries);
                mainData.setValue(list);
            }
        });

        mainData.addSource(industryFundedScores, new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                List<List<Entry>> list = mainData.getValue();
                if(list==null){
                    list = new ArrayList<>();
                    list.add(new ArrayList<Entry>());
                    list.add(new ArrayList<Entry>());
                }
                list.set(INDUSTRY_INDEX,entries);
                mainData.setValue(list);
            }
        });
    }

    public void init(String programName) {
        budgetFundedScores.setValue(new ArrayList<Entry>());
        industryFundedScores.setValue(new ArrayList<Entry>());
        hasConnection.setValue(true);
        loadBudgetFundedScores(programName);
    }

    public LiveData<Boolean> getHasConnection() {
        return hasConnection;
    }

    public List<ChosenProgram> getAllChosenPrograms() {
        return repository.getAllChosenPrograms();
    }

    public LiveData<List<Entry>> getBudgetFundedScores() {
        return budgetFundedScores;
    }

    public LiveData<List<Entry>> getIndustryFundedScores() {
        return industryFundedScores;
    }

    public MutableLiveData<Boolean> getFinishedParsing() {
        return finishedParsing;
    }

    public void clearBudgetFundedScores(){
        budgetFundedScores.setValue(new ArrayList<Entry>());
    }

    public void clearIndustryFundedScores(){
        industryFundedScores.setValue(new ArrayList<Entry>());
    }

    private boolean getConnectionStatus() {
        return isNetworkConnected(getApplication()) && hasInternetAccess();
    }

    public void loadBudgetFundedScores(final String programName) {
        finishedParsing.setValue(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean conn = getConnectionStatus();
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
                        hasConnection.setValue(conn);
                        budgetFundedScores.setValue(scores);
                        finishedParsing.setValue(true);
                    }
                });
            }
        });
        thread.start();
    }

    public void loadIndustryFundedScores(final String programName) {
        finishedParsing.setValue(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean conn = getConnectionStatus();
                final List<Entry> scores = new ArrayList<>();
                StatsScoresParsing instance = StatsScoresParsing.getInstance();

                try {
                    scores.addAll(instance.parseIndustryFundedScores(programName));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hasConnection.setValue(conn);
                        industryFundedScores.setValue(scores);
                        finishedParsing.setValue(true);
                    }
                });
            }
        });
        thread.start();
    }
}
