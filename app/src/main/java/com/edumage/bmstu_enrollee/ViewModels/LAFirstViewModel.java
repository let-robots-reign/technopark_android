package com.edumage.bmstu_enrollee.ViewModels;

import android.app.Application;
import android.util.Log;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class LAFirstViewModel extends AndroidViewModel {
    private DbRepository repository;

    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> date = new MutableLiveData<>();

    public final int NO_NAME_WARNING = -1;
    public final int NO_DATE_WARNING = -2;
    public final int NO_WARNINGS = 1;

    public LAFirstViewModel(@NonNull Application application) {
        super(application);
        repository = DbRepository.getInstance();
    }

    public LiveData<UserInfo> getUserInfo() {
        return repository.getUserInfo();
    }

    public void insertUserInfo(UserInfo info) {
        repository.insertUserInfo(info);
    }

    public void replaceUserInfo(UserInfo info) {
        repository.replaceUserInfo(info);
    }

    public void deleteAllInfo() {
        repository.deleteAllInfo();
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getDate() {
        return date;
    }

    public void setName(String n) {
        name.setValue(n);
    }

    public void setDate(String d) {
        date.setValue(d);
    }

    // if returning value > 0 is ok
    // int returning value < 0 is warning
    public int validateData() {
        if (name.getValue() != null && name.getValue().length() == 0) {
            return NO_NAME_WARNING;
        }
        if (date.getValue() != null && date.getValue().length() == 0) {
            return NO_DATE_WARNING;
        }
        return NO_WARNINGS;
    }
}
