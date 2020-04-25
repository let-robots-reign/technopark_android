package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;

import java.util.List;

public class LASecondViewModel extends AndroidViewModel {
    private DbRepository repository;

    public LASecondViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public List<ExamPoints> getAllPoints() {
        return repository.getAllPoints();
    }

    public void replaceAllPoints(List<ExamPoints> newPoints) {
        repository.replaceAllPoints(newPoints);
    }
}
