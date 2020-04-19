package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;

import java.util.List;

public class StatsFragmentViewModel extends AndroidViewModel {
    private DbRepository repository;

    public StatsFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public List<ChosenProgram> getAllChosenPrograms() {
        return repository.getAllChosenPrograms();
    }
}
