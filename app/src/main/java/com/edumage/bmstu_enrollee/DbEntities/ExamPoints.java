package com.edumage.bmstu_enrollee.DbEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exam_points")
public class ExamPoints {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "exam_name")
    private String examName;

    @ColumnInfo(name = "exam_score")
    private int examScore;

    @ColumnInfo(name="subject_id")
    private int subjectId;

    public ExamPoints(String examName, int examScore, int subjectId) {
        this.examName = examName;
        this.examScore = examScore;
        this.subjectId=subjectId;
    }

    public String getExamName() {
        return examName;
    }

    public int getExamScore() {
        return examScore;
    }


    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
