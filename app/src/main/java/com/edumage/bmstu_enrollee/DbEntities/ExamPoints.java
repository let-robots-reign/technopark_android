package com.edumage.bmstu_enrollee.DbEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exam_points")
public class ExamPoints {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "exam_name")
    public String examName;

    @ColumnInfo(name = "exam_score")
    public int examScore;

    public ExamPoints(String examName, int examScore) {
        this.examName = examName;
        this.examScore = examScore;
    }

    public String getExamName() {
        return examName;
    }

    public int getExamScore() {
        return examScore;
    }
}
