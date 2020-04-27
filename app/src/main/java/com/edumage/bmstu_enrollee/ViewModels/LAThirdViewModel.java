package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LAThirdViewModel extends AndroidViewModel {
    private DbRepository repository;

    public LAThirdViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public void replaceAllPrograms(List<ChosenProgram> newPrograms) {
        repository.replaceAllPrograms(newPrograms);
    }
}
