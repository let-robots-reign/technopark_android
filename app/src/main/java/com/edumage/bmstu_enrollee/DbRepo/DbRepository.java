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
    public UserInfo getUserInfo() throws InterruptedException {
        final UserInfo[] userInfo = new UserInfo[1];
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                userInfo[0] = userDao.getUserInfo();
                countDownLatch.countDown();
            }
        }).start();
        //TODO add runnable to executor
        countDownLatch.await(2, TimeUnit.SECONDS);
        return userInfo[0];
    }

    public void insertUserInfo(final UserInfo info) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                userDao.insertUserInfo(info);
            }
        };
        executorService.execute(runnable);
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

    public List<ExamPoints> getAllPoints() throws InterruptedException {

        //TODO add runnbale to executor
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
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pointsDao.replaceAllPoints(newPoints);
            }
        });
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
        //TODO add to executor service
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
