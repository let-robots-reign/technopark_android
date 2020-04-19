package com.edumage.bmstu_enrollee.DbDaos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

import java.util.List;

@Dao
public interface ExamPointsDao {

    @Query("SELECT * FROM exam_points")
    List<ExamPoints> getAllPoints();

    @Query("SELECT exam_score FROM exam_points WHERE exam_name = :examName")
    int getScore(String examName);

    @Insert
    void insertPoints(ExamPoints examPoints);

    @Insert
    void insertAllPoints(List<ExamPoints> allPoints);

    @Update
    void updatePoints(ExamPoints examPoints);

    @Delete
    void deletePoints(ExamPoints examPoints);

    @Query("DELETE FROM exam_points")
    void deleteAllPoints();
}
