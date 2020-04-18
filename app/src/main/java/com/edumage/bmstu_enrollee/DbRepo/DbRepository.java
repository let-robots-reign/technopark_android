package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.edumage.bmstu_enrollee.DataBase;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

import java.util.List;

public class DbRepository {
    private ExamPointsDao pointsDao;
    private LiveData<List<ExamPoints>> allPoints;

    public DbRepository(Application application) {
        DataBase dataBase = DataBase.getInstance(application);
        pointsDao = dataBase.examPointsDao();
        allPoints = pointsDao.getAllPoints();
    }

    public void insertPoints(final ExamPoints examPoints) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.insertPoints(examPoints);
            }
        });
        thread.start();
    }

    public void deletePoints(final ExamPoints examPoints) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.deletePoints(examPoints);
            }
        });
        thread.start();
    }

    public LiveData<List<ExamPoints>> getAllPoints() {
        return allPoints;
    }
}
