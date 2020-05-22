package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.EGESubject;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DisciplinesViewModel extends AndroidViewModel {
    private DbRepository repository;

    private MutableLiveData<ArrayList<Discipline>> data = new MutableLiveData<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DisciplinesViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public LiveData<ArrayList<Discipline>> getData() {
        return data;
    }

    public void replaceAllPrograms(final List<Discipline> data) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<ChosenProgram> chosenPrograms = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    Discipline d = data.get(i);
                    if (d.getStatus()) {
                        chosenPrograms.add(new ChosenProgram(d.getFullName(), 0));
                    }
                }
                repository.replaceAllPrograms(chosenPrograms);
            }
        };

        executorService.execute(runnable);
    }

    //применяет к текущим данным значение из базы данных
    public void applyChosenProgram() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ChosenProgram> programs = repository.getAllChosenPrograms();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Discipline> list = data.getValue();
                        Log.d("TH_TEST", "Apply program NULL");
                        if (list == null) return;
                        for (int i = 0; i < programs.size(); i++) {
                            ChosenProgram program = programs.get(i);
                            boolean found = false;
                            for (Discipline d : list) {
                                if (d.getFullName().equals(program.getProgramName())) {
                                    d.setStatus(true);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                programs.remove(i);
                                i--;
                            }
                        }
                        Log.d("TH_TEST", "Apply program");
                        data.setValue(list);

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                repository.replaceAllPrograms(programs);
                            }
                        });
                    }
                });
            }
        };
        executorService.execute(runnable);
    }

    public void applySubjectThenProgram() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    public void applyChosenSubjects() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ExamPoints> exams = repository.getAllPoints();
                final ArrayList<Integer> id = new ArrayList<>();
                for (ExamPoints exam : exams) {
                    id.add(exam.getSubjectId());
                }
                Log.d("TH_TEST", "Apply Subject NULL");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Discipline> list = data.getValue();
                        if (list == null) return;
                        for (int j = 0; j < list.size(); j++) {
                            Discipline d = list.get(j);
                            int[] arr = d.getSubjects();
                            for (int c : arr) {
                                if (!id.contains(c)) {
                                    list.remove(j);
                                    j--;
                                    break;
                                }
                            }
                        }
                        Log.d("TH_TEST", "Apply Subject");
                        data.setValue(list);
                    }
                });
            }
        };

        executorService.execute(runnable);
    }

    public void loadData() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
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
                    Discipline d = new Discipline(value, name.toString(), number, form);
                    d.setSubjects(subjectsIdByCode(number));
                    list.add(d);
                }
                Log.d("TH_TEST", "Load data");
                data.postValue(list);
            }
        };

        executorService.execute(runnable);
    }

    static int[] subjectsIdByCode(String code) {
        int[] res = new int[Discipline.NUMBER_OF_PASSING_EXAMS];
        switch (code) {
            case "01.03.02":
            case "02.03.01":
            case "09.03.01":
            case "09.03.02":
            case "09.03.03":
            case "09.03.04":
                res[0] = EGESubject.INFORMATICS_ID;
                res[1] = EGESubject.MATH_ID;
                res[2] = EGESubject.RUSSIAN_ID;
                break;
            case "01.03.04":
                res[0] = EGESubject.MATH_ID;
                res[1] = EGESubject.PHYSICS_ID;
                res[2] = EGESubject.RUSSIAN_ID;
                break;
            case "35.03.01":
            case "35.03.10":
                res[0] = EGESubject.BIOLOGY_ID;
                res[1] = EGESubject.MATH_ID;
                res[2] = EGESubject.RUSSIAN_ID;
                break;
            case "38.03.01":
            case "38.03.02":
            case "38.03.05":
            case "39.03.01":
            case "44.03.04":
                res[0] = EGESubject.MATH_ID;
                res[1] = EGESubject.SOCIAL_ID;
                res[2] = EGESubject.RUSSIAN_ID;
                break;
            case "45.03.02":
                res[0] = EGESubject.ENGLISH_ID;
                res[1] = EGESubject.RUSSIAN_ID;
                res[2] = EGESubject.SOCIAL_ID;
                break;
            case "54.03.01":
                //TODO for design need to add LITERATURE, now is useless
                res[0] = EGESubject.CUSTOM_ID;
                res[1] = EGESubject.CUSTOM_ID;
                res[2] = EGESubject.CUSTOM_ID;
                break;
            default:
                res[0] = EGESubject.PHYSICS_ID;
                res[1] = EGESubject.MATH_ID;
                res[2] = EGESubject.RUSSIAN_ID;
                break;
        }
        return res;
    }
}
