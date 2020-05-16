package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Looper;
import android.os.Handler;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class DisciplinesViewModel extends AndroidViewModel {
    private DbRepository repository;

    public final MutableLiveData<ArrayList<Discipline>> data = new MutableLiveData<>();

    public DisciplinesViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public void replaceAllPrograms(List<Discipline> data) {
        List<ChosenProgram> chosenPrograms = new ArrayList<>();
        for (Discipline d : data) {
            if (d.getStatus()) {
                chosenPrograms.add(new ChosenProgram(d.getFullName(), 0));
            }
        }
        repository.replaceAllPrograms(chosenPrograms);
    }

    //применяет к текущим данным значение из базы данных
    public void applyChosenProgram() {
        //TODO maybe need another thread
        List<ChosenProgram> programs = repository.getAllChosenPrograms();
        ArrayList<Discipline> list = data.getValue();
        if (list == null) return;
        for (ChosenProgram program : programs) {
            for (Discipline d : list) {
                if (d.getFullName().equals(program.getProgramName())) {
                    d.setStatus(true);
                    break;
                }
            }
        }
    }


    public void applyChosenSubjects(){

        final Handler handler =new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ExamPoints> exams = repository.getAllPoints();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Integer> id= new ArrayList<>();
                        for (ExamPoints exam:exams){
                            id.add(exam.getSubjectId());
                        }
                        ArrayList<Discipline> list = data.getValue();
                        if (list==null) return;
                        for (int j=0; j<list.size(); j++){
                            Discipline d=list.get(j);
                            int[] arr=d.getSubjects();
                            for (int i=0; i<arr.length; i++){
                                int c=arr[i];
                                if(!id.contains(c)){
                                    list.remove(d);
                                    j--;
                                    break;
                                    //TODO check iterator
                                }
                            }
                        }
                        data.setValue(list);
                    }
                });
            }
        }).start();

    }



    public void loadData() {
        String[] array = getApplication().getResources().getStringArray(R.array.disciplines);
        ArrayList<Discipline> list = new ArrayList<>();
        for (String value : array) {
            String[] s = value.split(" ");
            String number = s[0];
            StringBuilder name = new StringBuilder();
            for (int k = 1; k < s.length - 1; k++) {
                name.append(s[k]).append(" ");
            }
            String form = s[s.length - 1];
            form = form.substring(1, form.length() - 1);
            Discipline d= new Discipline(value, name.toString(), number, form);
            d.setSubjects(subjectsIdByCode(number));
            list.add(d);
        }
        data.setValue(list);
    }


    private int[] subjectsIdByCode(String code){
        /*EGESubject.Subject[] res = new EGESubject.Subject[Discipline.NUMBER_OF_PASSING_EXAMS];
        switch (code){
            case "01.03.02":
            case "02.03.01":
            case "09.03.01":
            case "09.03.02":
            case "09.03.03":
            case "09.03.04":
                res[0]= EGESubject.Subject.INFORMATICS;
                res[1]= EGESubject.Subject.MATH;
                res[2]= EGESubject.Subject.RUSSIAN;
                break;
            case "01.03.04":
                res[0]= EGESubject.Subject.MATH;
                res[1]= EGESubject.Subject.PHYSICS;
                res[2]= EGESubject.Subject.RUSSIAN;
                break;
            case "35.03.01":
            case "35.03.10":
                res[0]= EGESubject.Subject.BIOLOGY;
                res[1]= EGESubject.Subject.MATH;
                res[2]= EGESubject.Subject.RUSSIAN;
                break;
            case "38.03.01":
            case "38.03.02":
            case "38.03.05":
            case "39.03.01":
            case "44.03.04":
                res[0]= EGESubject.Subject.MATH;
                res[1]= EGESubject.Subject.SOCIAL;
                res[2]= EGESubject.Subject.RUSSIAN;
                break;
            case "45.03.02":
                res[0]= EGESubject.Subject.ENGLISH;
                res[1]= EGESubject.Subject.RUSSIAN;
                res[2]= EGESubject.Subject.SOCIAL;
                break;
            case "54.03.01":
                //TODO for design need to add LITERATURE, now is useless
                res[0]= EGESubject.Subject.CUSTOM;
                res[1]= EGESubject.Subject.CUSTOM;
                res[2]= EGESubject.Subject.CUSTOM;
                break;
            default:
                res[0]= EGESubject.Subject.PHYSICS;
                res[1]=EGESubject.Subject.MATH;
                res[2]= EGESubject.Subject.RUSSIAN;
                break;
        }*/


        int[] res = new int[Discipline.NUMBER_OF_PASSING_EXAMS];
        switch (code){
            case "01.03.02":
            case "02.03.01":
            case "09.03.01":
            case "09.03.02":
            case "09.03.03":
            case "09.03.04":
                res[0]= EGESubject.INFORMATICS_ID;
                res[1]= EGESubject.MATH_ID;
                res[2]= EGESubject.RUSSIAN_ID;
                break;
            case "01.03.04":
                res[0]= EGESubject.MATH_ID;
                res[1]= EGESubject.PHYSICS_ID;
                res[2]= EGESubject.RUSSIAN_ID;
                break;
            case "35.03.01":
            case "35.03.10":
                res[0]= EGESubject.BIOLOGY_ID;
                res[1]= EGESubject.MATH_ID;
                res[2]= EGESubject.RUSSIAN_ID;
                break;
            case "38.03.01":
            case "38.03.02":
            case "38.03.05":
            case "39.03.01":
            case "44.03.04":
                res[0]= EGESubject.MATH_ID;
                res[1]= EGESubject.SOCIAL_ID;
                res[2]= EGESubject.RUSSIAN_ID;
                break;
            case "45.03.02":
                res[0]= EGESubject.ENGLISH_ID;
                res[1]= EGESubject.RUSSIAN_ID;
                res[2]= EGESubject.SOCIAL_ID;
                break;
            case "54.03.01":
                //TODO for design need to add LITERATURE, now is useless
                res[0]= EGESubject.CUSTOM_ID;
                res[1]= EGESubject.CUSTOM_ID;
                res[2]= EGESubject.CUSTOM_ID;
                break;
            default:
                res[0]= EGESubject.PHYSICS_ID;
                res[1]=EGESubject.MATH_ID;
                res[2]= EGESubject.RUSSIAN_ID;
                break;
        }
        return res;
    }






}
