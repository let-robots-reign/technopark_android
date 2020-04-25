package com.edumage.bmstu_enrollee.DbDaos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

import java.util.List;

@Dao
public abstract class ExamPointsDao {

    @Query("SELECT * FROM exam_points")
    public abstract List<ExamPoints> getAllPoints();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllPoints(List<ExamPoints> allPoints);

    @Transaction
    public void replaceAllPoints(List<ExamPoints> newPoints) {
        deleteAllPoints();
        insertAllPoints(newPoints);
    }

    @Query("DELETE FROM exam_points")
    public abstract void deleteAllPoints();
}
