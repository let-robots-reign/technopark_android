package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;


import com.edumage.bmstu_enrollee.DbDaos.ChosenProgramDao;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbDaos.UserInfoDao;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;

import java.util.List;

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
    public UserInfo getUserInfo() {
        return userDao.getUserInfo();
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
    public List<ExamPoints> getAllPoints() {
        return pointsDao.getAllPoints();
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
    public List<ChosenProgram> getAllChosenPrograms() {
        return chosenProgramDao.getAllChosenPrograms();
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
