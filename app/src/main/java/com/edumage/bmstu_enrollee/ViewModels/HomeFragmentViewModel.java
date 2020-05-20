package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.ParsingRepo.CurrentFilesParsing;
import com.edumage.bmstu_enrollee.ParsingRepo.CurrentScoresParsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.edumage.bmstu_enrollee.ConnectionCheck.hasInternetAccess;
import static com.edumage.bmstu_enrollee.ConnectionCheck.isNetworkConnected;

public class HomeFragmentViewModel extends AndroidViewModel {
    private DbRepository repository;
    private final MutableLiveData<List<String>> scoresLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> filesLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<Integer>> userscoresLiveData = new MutableLiveData<>();
    private List<ChosenProgram> programsNames;
    private final Handler handler = new Handler(Looper.getMainLooper());





    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public void init(List<ChosenProgram> programs) {
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
            List<ExamPoints> exams = getExamPoints();
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



    public UserInfo getUserInfo() {
        return repository.getUserInfo();
    }

    public List<ExamPoints> getExamPoints() {
        return repository.getAllPoints();
    }

    public List<ChosenProgram> getChosenPrograms() {
        return repository.getAllChosenPrograms();
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