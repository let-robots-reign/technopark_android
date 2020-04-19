package com.edumage.bmstu_enrollee.DbRepo;

import android.app.Application;

import com.edumage.bmstu_enrollee.DataBase;
import com.edumage.bmstu_enrollee.DbDaos.ChosenProgramDao;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

import java.util.List;

public class DbRepository {
    private ExamPointsDao pointsDao;
    private ChosenProgramDao chosenProgramDao;

    public DbRepository(Application application) {
        DataBase dataBase = DataBase.getInstance(application);
        pointsDao = dataBase.examPointsDao();
        chosenProgramDao = dataBase.chosenProgramDao();
    }

    // Table exam_points
    public List<ExamPoints> getAllPoints() {
        return pointsDao.getAllPoints();
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

    public void deleteAllPoints() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pointsDao.deleteAllPoints();
            }
        });
        thread.start();
    }
    //

    // Table chosen_programs
    public List<ChosenProgram> getAllChosenPrograms() {
        return chosenProgramDao.getAllChosenPrograms();
    }

    public void insertAllPrograms(final List<ChosenProgram> allNewPrograms) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                chosenProgramDao.insertAllPrograms(allNewPrograms);
            }
        });
        thread.start();
    }

    public void deleteAllChosenPrograms() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                chosenProgramDao.deleteAllChosenPrograms();
            }
        });
        thread.start();
    }
    //
}
