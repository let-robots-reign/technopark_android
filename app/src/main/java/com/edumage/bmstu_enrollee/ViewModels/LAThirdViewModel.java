package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.Discipline;
import com.edumage.bmstu_enrollee.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LAThirdViewModel extends AndroidViewModel {
    private DbRepository repository;

    public final MutableLiveData<ArrayList<Discipline>> data =  new MutableLiveData<>();

    public LAThirdViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public void replaceAllPrograms(List<Discipline> data) {
        List<ChosenProgram> chosenPrograms =  new ArrayList<>();
        for (Discipline d : data) {
            if (d.getStatus()) {
                chosenPrograms.add(new ChosenProgram(d.getFullName(), 0));
            }
        }
        repository.replaceAllPrograms(chosenPrograms);
    }


    //применяет к текущим данным значение из базы данных

    public void applyChosenProgram(){

        //TODO maybe need another thread
        List<ChosenProgram> programs = repository.getAllChosenPrograms();
        ArrayList<Discipline> list= data.getValue();
        if (list==null)return;
        for (ChosenProgram program: programs){
            for(Discipline d: list){
                if (d.getFullName().equals(program.getProgramName())){
                    d.setStatus(true);
                    break;
                }
            }
        }

    }

    public void loadData(){
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
            list.add(new Discipline(value, name.toString(), number, form));
        }
        data.setValue(list);
    }
}
