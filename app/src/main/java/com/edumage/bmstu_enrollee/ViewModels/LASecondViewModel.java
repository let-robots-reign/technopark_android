package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

public class LASecondViewModel extends AndroidViewModel {
    private DbRepository repository;


    public final MutableLiveData<ArrayList<EGESubject>> data= new MutableLiveData<>();

    public LASecondViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }


    public void replaceAllPoints(List<EGESubject> egeSubjectList) {
        List<ExamPoints> points = new ArrayList<>();
        for (EGESubject subject : egeSubjectList) {
                points.add(new ExamPoints(subject.getName(), subject.getScore()));
        }
        repository.replaceAllPoints(points);
    }

    public void applyEgeScore(){

        //TODO maybe need another thread
        List<ExamPoints> exams = repository.getAllPoints();
        ArrayList<EGESubject> list= data.getValue();
        if (list==null)return;
        for (ExamPoints exam: exams){
            for(EGESubject ege: list){
                if (ege.getName().equals(exam.getExamName())){
                    ege.setPassed(true);
                    ege.setScore(exam.getExamScore());
                    break;
                }
            }
        }

    }

    public void loadData(){
        ArrayList<EGESubject> res = new ArrayList<>();
        String[] str = getApplication().getResources().getStringArray(R.array.subjects);

        int[] drawables = new int[str.length];
        drawables[0] = R.drawable.russian;
        drawables[1] = R.drawable.math;
        drawables[2] = R.drawable.informatics;
        drawables[3] = R.drawable.physics;
        drawables[4] = R.drawable.chemistry;
        drawables[5] = R.drawable.biology;
        drawables[6] = R.drawable.social;
        drawables[7] = R.drawable.english;
        int i = 0;
        for (String s : str) {
            res.add(new EGESubject(s, drawables[i]));
            i++;
        }
        data.setValue(res);
    }
}
