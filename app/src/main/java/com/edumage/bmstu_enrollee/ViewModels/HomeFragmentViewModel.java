package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.ParsingRepo.CurrentFilesParsing;
import com.edumage.bmstu_enrollee.ParsingRepo.CurrentScoresParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.edumage.bmstu_enrollee.ConnectionCheck.hasInternetAccess;
import static com.edumage.bmstu_enrollee.ConnectionCheck.isNetworkConnected;

public class HomeFragmentViewModel extends AndroidViewModel {
    private DbRepository repository;
    private MutableLiveData<List<ExamPoints>> examPoints = new MutableLiveData<>();
    private MutableLiveData<List<ChosenProgram>> programs = new MutableLiveData<>();
    private MutableLiveData<List<String>> scoresLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> filesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> userscoresLiveData = new MutableLiveData<>();
    private MediatorLiveData<Pair<List<ExamPoints>, List<ChosenProgram>>> mainData = new MediatorLiveData<>();
    private List<ChosenProgram> programsNames;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);

        examPoints.setValue(new ArrayList<>());
        programs.setValue(new ArrayList<>());
        getExamPoints();
        getChosenPrograms();

        mainData.addSource(examPoints, new Observer<List<ExamPoints>>() {
            @Override
            public void onChanged(List<ExamPoints> examPoints) {
                if (examPoints != null)
                    mainData.setValue(new Pair<>(examPoints, programs.getValue()));
            }
        });
        mainData.addSource(programs, new Observer<List<ChosenProgram>>() {
            @Override
            public void onChanged(List<ChosenProgram> chosenPrograms) {
                if (chosenPrograms != null)
                    mainData.setValue(new Pair<>(examPoints.getValue(), chosenPrograms));
            }
        });
    }

    public void init(List<ChosenProgram> programs, List<ExamPoints> exams) {
        programsNames = programs;
        scoresLiveData.setValue(new ArrayList<String>());
        filesLiveData.setValue(new ArrayList<String>());

        ArrayList<Integer> list =  new ArrayList<>();

        for(ChosenProgram program:programs){
            int add=0;
            String[] s = program.getProgramName().split(" ");
            String code = s[0];
            ArrayList<Integer> subjects=new ArrayList<>();
            int[] arr =DisciplinesViewModel.subjectsIdByCode(code);
            for(Integer e:arr){
                subjects.add(e);
            }

           // int[] subjects =;
            for (ExamPoints exam: exams){

                if(subjects.contains(exam.getSubjectId())){
                    add+=exam.getExamScore();
                }
            }
            list.add(add);
        }

        userscoresLiveData.setValue(list);

        loadScores();
        loadFiles();
    }

    public LiveData<Pair<List<ExamPoints>, List<ChosenProgram>>> getMainData() {
        return mainData;
    }

    public LiveData<UserInfo> getUserInfo() {
        return repository.getUserInfo();
    }

    private void getExamPoints() {
        examPoints = repository.getAllPoints();
    }

    private void getChosenPrograms() {
        programs = repository.getAllChosenPrograms();
    }

    public MutableLiveData<List<Integer>> getUserscoresLiveData() {
        return userscoresLiveData;
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
                CurrentScoresParsing instance = CurrentScoresParsing.getInstance();
                // firstly, add info about last update
                try {
                    scores.add(instance.getLastUpdate());
                } catch (IOException e) {
                    scores.add("неизвестно");
                }
                String score;
                // retrieving info about passing scores
                for (ChosenProgram program : programsNames) {
                    if (!isNetworkConnected(getApplication())) {
                        score = "Нет сети";
                    } else if (!hasInternetAccess()) {
                        score = "Нет интернета";
                    } else {
                        try {
                            score = instance.parseScore(program.getProgramName());
                        } catch (IOException e) {
                            score = "Ошибка";
                            e.printStackTrace();
                        }
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
                CurrentFilesParsing instance = CurrentFilesParsing.getInstance();
                String fileUrl = null;
                for (ChosenProgram program: programsNames) {
                    try {
                        fileUrl = instance.parseFile(program.getProgramName());
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