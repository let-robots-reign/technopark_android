package com.edumage.bmstu_enrollee;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.edumage.bmstu_enrollee.DbDaos.ChosenProgramDao;
import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbDaos.UserInfoDao;
import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;
import com.edumage.bmstu_enrollee.DbEntities.UserInfo;

@Database(entities = {UserInfo.class, ExamPoints.class, ChosenProgram.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static DataBase instance;

    public abstract UserInfoDao userInfoDao();
    public abstract ExamPointsDao examPointsDao();
    public abstract ChosenProgramDao chosenProgramDao();

    public static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, "bmstu_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
