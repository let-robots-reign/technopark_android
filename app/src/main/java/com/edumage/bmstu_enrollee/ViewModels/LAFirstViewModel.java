package com.edumage.bmstu_enrollee.ViewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.edumage.bmstu_enrollee.DbRepo.DbRepository;
import com.edumage.bmstu_enrollee.R;

public class LAFirstViewModel extends AndroidViewModel {
    private DbRepository repository;

    public final MutableLiveData<String> name= new MutableLiveData<>();
    public final MutableLiveData<String> date = new MutableLiveData<>();

    public final int NO_NAME_WARNING=-1;
    public final int NO_DATE_WARNING=-2;
    public final int NO_WARNINGS=1;

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


    public void loadData(){
       /* AsyncTask<Void,Void,UserInfo> asyncTask = new AsyncTask<Void, Void, UserInfo>() {

            @Override
            protected UserInfo doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(UserInfo userInfo) {
                super.onPostExecute(userInfo);

            }
        };*/

       new Thread(new Runnable() {
           @Override
           public void run() {
            UserInfo userInfo = repository.getUserInfo();
            name.postValue(userInfo.getUserName());
            date.postValue(userInfo.getUserBirthday());
           }
       }).start();
    }

    public void insertUserInfo(){
        final UserInfo userInfo = new UserInfo(name.getValue(), date.getValue());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //repository.deleteAllInfo();
                repository.insertUserInfo(userInfo);

            }
        }).start();
    }

    // if returning value>0 is ok
    // int returning value<0 is warning
    public int validateData(){
        if (name.getValue().length() == 0) {
            return NO_NAME_WARNING;
        }
        if (date.getValue().length() == 0) {
            return NO_DATE_WARNING;
        }
        return NO_WARNINGS;
    }




}
