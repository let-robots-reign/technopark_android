package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EgeSubjectsViewModel extends AndroidViewModel {
    private DbRepository repository;

    private MutableLiveData<ArrayList<EGESubject>> data = new MutableLiveData<>();

    public EgeSubjectsViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public LiveData<ArrayList<EGESubject>> getData() {
        return data;
    }

    public LiveData<List<ExamPoints>> getExamPoints() {
        return repository.getAllPoints();
    }

    public void replaceAllPoints(final List<EGESubject> egeSubjectList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ExamPoints> points = new ArrayList<>();
                for (int i = 0; i < egeSubjectList.size(); i++) {
                    EGESubject subject = egeSubjectList.get(i);
                    points.add(new ExamPoints(subject.getName(), subject.getScore(), subject.getId()));
                }
                repository.replaceAllPoints(points);
                //if(!updateDiscipline)return;
            }
        }).start();
    }

    public void applyEgeScore(List<ExamPoints> exams) {
        //TODO maybe need another thread
        ArrayList<EGESubject> list = data.getValue();
        if (list == null) return;
        for (ExamPoints exam : exams) {
            for (EGESubject ege : list) {
                /*if (ege.getName().equals(exam.getExamName())) {
                    ege.setPassed(true);
                    ege.setScore(exam.getExamScore());
                    break;
                }*/
                if (ege.getId() == exam.getSubjectId()) {
                    ege.setPassed(true);
                    ege.setScore(exam.getExamScore());
                    break;
                }
            }
        }
    }

    public void loadData() {
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
            EGESubject subject = new EGESubject(s, drawables[i], i);
            res.add(subject);
            i++;
        }
        data.setValue(res);
    }


    public void updateDisciplines() {

    }
}
