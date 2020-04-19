package com.edumage.bmstu_enrollee;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.edumage.bmstu_enrollee.DbDaos.ExamPointsDao;
import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

@Database(entities = {ExamPoints.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    private static DataBase instance;

    public abstract ExamPointsDao examPointsDao();

    public static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, "bmstu_db").allowMainThreadQueries().build();
        }
        return instance;
    }
}
