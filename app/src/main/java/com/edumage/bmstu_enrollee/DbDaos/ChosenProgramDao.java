package com.edumage.bmstu_enrollee.DbDaos;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ChosenProgramDao {

    @Query("SELECT * FROM chosen_programs")
    List<ChosenProgram> getAllChosenPrograms();

    @Query("SELECT program_current_score FROM chosen_programs WHERE program_name = :programName")
    int getCurrentPassingScore(String programName);

    @Insert
    void insertProgram(ChosenProgram newProgram);

    @Insert
    void insertAllPrograms(List<ChosenProgram> allNewPrograms);

    @Delete
    void deleteProgram(ChosenProgram program);

    @Query("DELETE FROM chosen_programs")
    void deleteAllChosenPrograms();
}
