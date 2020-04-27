package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;

public class LAFirstViewModel extends AndroidViewModel {
    private DbRepository repository;

    public LAFirstViewModel(@NonNull Application application) {
        super(application);
        repository = new DbRepository(application);
    }

    public void insertUserInfo(UserInfo info) {
        repository.insertUserInfo(info);
    }

    public void deleteAllInfo() {
        repository.deleteAllInfo();
    }
}
