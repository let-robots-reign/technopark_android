package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;

import com.edumage.bmstu_enrollee.DbDaos.ChosenProgramDao;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbDaos.UserInfoDao;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DbRepository {
    private UserInfoDao userDao;
    private ExamPointsDao pointsDao;
    private ChosenProgramDao chosenProgramDao;

    public DbRepository(Application application) {
        DataBase dataBase = DataBase.getInstance(application);
        userDao = dataBase.userInfoDao();
        pointsDao = dataBase.examPointsDao();
        chosenProgramDao = dataBase.chosenProgramDao();
    }

    // Table user_info
    public LiveData<UserInfo> getUserInfo() {
        final MutableLiveData<UserInfo> liveData = new MutableLiveData<>(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                liveData.postValue(userDao.getUserInfo());
            }
        }).start();
        return liveData;
    }

    public void insertUserInfo(final UserInfo info) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.insertUserInfo(info);
            }
        });
        thread.start();
    }

    public void replaceUserInfo(final UserInfo info) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.replaceUserInfo(info);
            }
        });
        thread.start();
    }

    public void deleteAllInfo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.deleteAllInfo();
            }
        });
        thread.start();
    }
    //

    // Table exam_points
    public LiveData<List<ExamPoints>> getAllPoints() {
        final MutableLiveData<List<ExamPoints>> liveData = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                liveData.postValue(pointsDao.getAllPoints());
            }
        }).start();
        return liveData;
    }

    public void replaceAllPoints(final List<ExamPoints> newPoints) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.replaceAllPoints(newPoints);
            }
        });
        thread.start();
    }
    //

    // Table chosen_programs
    public LiveData<List<ChosenProgram>> getAllChosenPrograms() {
        final MutableLiveData<List<ChosenProgram>> liveData = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                liveData.postValue(chosenProgramDao.getAllChosenPrograms());
            }
        }).start();
        return liveData;
    }

    public void replaceAllPrograms(final List<ChosenProgram> newPrograms) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                chosenProgramDao.replaceAllPrograms(newPrograms);
            }
        });
        thread.start();
    }
    //
}
