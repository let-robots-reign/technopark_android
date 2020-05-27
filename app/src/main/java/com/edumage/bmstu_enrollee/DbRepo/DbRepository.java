package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;

import com.edumage.bmstu_enrollee.DbDaos.ChosenProgramDao;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbDaos.UserInfoDao;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DbRepository {
    private UserInfoDao userDao;
    private ExamPointsDao pointsDao;
    private ChosenProgramDao chosenProgramDao;


    private static DbRepository repository;

    private ExecutorService executorService=Executors.newSingleThreadExecutor();

    public static void init(Application application){
        repository = new DbRepository(application);
    }

    public static DbRepository getInstance(){
        return repository;
    }

    public DbRepository(Application application) {
        DataBase dataBase = DataBase.getInstance(application);
        userDao = dataBase.userInfoDao();
        pointsDao = dataBase.examPointsDao();
        chosenProgramDao = dataBase.chosenProgramDao();
    }

    // Table user_info

    public LiveData<UserInfo> getUserInfo() {
        MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userInfo.postValue(userDao.getUserInfo());
            }
        });
        return userInfo;
    }

    public void insertUserInfo(final UserInfo info) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insertUserInfo(info);
        });
    }



    public void replaceUserInfo(final UserInfo info) {
        Runnable runable = new Runnable() {
            @Override
            public void run() {
                userDao.replaceUserInfo(info);
            }
        };
        executorService.execute(runable);
    }

    public void deleteAllInfo() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.deleteAllInfo();
            }
        });
    }

    public MutableLiveData<List<ExamPoints>> getAllPoints() {
        MutableLiveData<List<ExamPoints>> points = new MutableLiveData<>();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                points.postValue(pointsDao.getAllPoints());
            }
        });
        return points;
    }

    public void replaceAllPoints(final List<ExamPoints> newPoints) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pointsDao.replaceAllPoints(newPoints);
            }
        });
    }

    public MutableLiveData<List<ChosenProgram>> getAllChosenPrograms() {
        MutableLiveData<List<ChosenProgram>> programs = new MutableLiveData<>();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                programs.postValue(chosenProgramDao.getAllChosenPrograms());
            }
        });
        return programs;
    }

    public void replaceAllPrograms(final List<ChosenProgram> newPrograms) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                chosenProgramDao.replaceAllPrograms(newPrograms);
            }
        });
    }
}
