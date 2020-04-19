package com.edumage.bmstu_enrollee.DbEntities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chosen_programs")
public class ChosenProgram {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "program_name")
    private String programName;

    @ColumnInfo(name = "program_current_score")
    private int programCurScore;

    public ChosenProgram(String programName, int programCurScore) {
        this.programName = programName;
        this.programCurScore = programCurScore;
    }

    public long getId() {
        return id;
    }

    public String getProgramName() {
        return programName;
    }

    public int getProgramCurScore() {
        return programCurScore;
    }
}
