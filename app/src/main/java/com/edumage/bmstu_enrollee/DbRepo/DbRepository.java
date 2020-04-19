package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;

import com.edumage.bmstu_enrollee.DataBase;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

import java.util.List;

public class DbRepository {
    private ExamPointsDao pointsDao;

    public DbRepository(Application application) {
        DataBase dataBase = DataBase.getInstance(application);
        pointsDao = dataBase.examPointsDao();
    }

    public void insertPoints(final ExamPoints examPoints) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.insertPoints(examPoints);
            }
        });
        thread.start();
    };

    public void insertAllPoints(final List<ExamPoints> allExamPoints) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.insertAllPoints(allExamPoints);
            }
        });
        thread.start();
    };

    public void deletePoints(final ExamPoints examPoints) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.deletePoints(examPoints);
            }
        });
        thread.start();
    }

    public List<ExamPoints> getAllPoints() {
        return pointsDao.getAllPoints();
    }
}
