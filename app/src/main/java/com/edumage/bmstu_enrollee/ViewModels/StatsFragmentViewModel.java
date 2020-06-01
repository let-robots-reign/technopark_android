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
//import com.edumage.bmstu_enrollee.chartingData.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.edumage.bmstu_enrollee.ConnectionCheck.hasInternetAccess;
import static com.edumage.bmstu_enrollee.ConnectionCheck.isNetworkConnected;

public class StatsFragmentViewModel extends AndroidViewModel {
    private DbRepository repository;
    private final MutableLiveData<Boolean> hasConnection = new MutableLiveData<>();
    private final MutableLiveData<List<Entry>> budgetFundedScores = new MutableLiveData<>();
    private final MutableLiveData<List<Entry>> industryFundedScores = new MutableLiveData<>();
    private final MediatorLiveData<List<List<Entry>>> mainData= new MediatorLiveData<>();
    private final MutableLiveData<Boolean> finishedParsing = new MutableLiveData<>();
    public final MediatorLiveData<List<ChosenProgram>> chosenProgram = new MediatorLiveData<>();
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
                if(compareEntries(list.get(BUDGET_INDEX),entries)){
                    return;
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
                if(compareEntries(list.get(INDUSTRY_INDEX),entries)){
                    return;
                }
                list.set(INDUSTRY_INDEX,entries);
                mainData.setValue(list);
            }
        });
    }

    //true is equals
    private boolean compareEntries(List<Entry> list1, List<Entry> list2){
       //TODO compare list entry does not work
        if(list1==null || list2==null) return false;
        ArrayList<Entry> b= new ArrayList<>(list2);
        ArrayList<Entry> a = new ArrayList<>(list1);

        for (Entry e:a){
            int index=entryContains(e,b);
            if (index<0)return false;
            b.remove(index);
        }
        return b.isEmpty();
    }

    private int entryContains(Entry entry, ArrayList<Entry> list){
        for (int i=0; i<list.size(); i++){
            Entry e=list.get(i);
            if(e.getX()==entry.getX() && e.getY()==entry.getY())
            return i;
        }
        return -1;
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


    public LiveData<List<List<Entry>>> getMainData() {
        return mainData;
    }

    public void updateProgram() {
        chosenProgram.addSource(repository.getAllChosenPrograms(), new Observer<List<ChosenProgram>>() {
            @Override
            public void onChanged(List<ChosenProgram> chosenPrograms) {
                chosenProgram.setValue(chosenPrograms);
            }
        });
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
      StatsScoresParsing.getInstance().pushTask(new Runnable() {
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
    }

    public void loadIndustryFundedScores(final String programName) {
        finishedParsing.setValue(false);
      StatsScoresParsing.getInstance().pushTask(new Runnable() {
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
    }


    public void loadAll(final String programName){
        finishedParsing.setValue(false);
        StatsScoresParsing.getInstance().pushTask(new Runnable() {
            @Override
            public void run() {
                final boolean conn = getConnectionStatus();
                final List<List<Entry>> scores = new ArrayList<>(2);
                StatsScoresParsing instance = StatsScoresParsing.getInstance();
                scores.add(new ArrayList<Entry>());
                scores.add(new ArrayList<Entry>());

                try {
                    scores.get(INDUSTRY_INDEX).addAll(instance.parseIndustryFundedScores(programName));
                    scores.get(BUDGET_INDEX).addAll(instance.parseBudgetFundedScores(programName));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hasConnection.setValue(conn);
                        mainData.setValue(scores);
                        finishedParsing.setValue(true);
                    }
                });
            }
        });
    }
}
