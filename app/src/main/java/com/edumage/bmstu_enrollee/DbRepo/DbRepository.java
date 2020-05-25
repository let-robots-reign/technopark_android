package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;

import com.edumage.bmstu_enrollee.DbDaos.ChosenProgramDao;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbDaos.UserInfoDao;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    public UserInfo getUserInfo() throws InterruptedException {
        final UserInfo[] userInfo = new UserInfo[1];
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                userInfo[0] = userDao.getUserInfo();
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await(2, TimeUnit.SECONDS);
        return userInfo[0];
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
    public List<ExamPoints> getAllPoints() throws InterruptedException {
        final List<ExamPoints> points = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                points.addAll(pointsDao.getAllPoints());
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await(2, TimeUnit.SECONDS);
        return points;
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
    public List<ChosenProgram> getAllChosenPrograms() throws InterruptedException {
        final List<ChosenProgram> programs = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                programs.addAll(chosenProgramDao.getAllChosenPrograms());
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await(2, TimeUnit.SECONDS);
        return programs;
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
