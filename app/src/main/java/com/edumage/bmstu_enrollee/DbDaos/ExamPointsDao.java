package com.edumage.bmstu_enrollee.DbDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.edumage.bmstu_enrollee.DbEntities.ExamPoints;

import java.util.List;

public interface ExamPointsDao {

    @Query("SELECT * FROM exam_points")
    LiveData<List<ExamPoints>> getAllPoints();

    @Query("SELECT exam_score FROM exam_points WHERE exam_name = :examName")
    int getScore(String examName);

    @Insert
    void insertPoints(ExamPoints examPoints);

    @Update
    void updatePoints(ExamPoints examPoints);

    @Delete
    void deletePoints(ExamPoints examPoints);
}
