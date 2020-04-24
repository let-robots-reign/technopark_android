package com.edumage.bmstu_enrollee.DbDaos;

import com.edumage.bmstu_enrollee.DbEntities.ChosenProgram;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class ChosenProgramDao {

    @Query("SELECT * FROM chosen_programs")
    public abstract List<ChosenProgram> getAllChosenPrograms();

    @Query("SELECT program_current_score FROM chosen_programs WHERE program_name = :programName")
    public abstract int getCurrentPassingScore(String programName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllPrograms(List<ChosenProgram> allPrograms);

    @Transaction
    public void replaceAllPrograms(List<ChosenProgram> newPrograms) {
        deleteAllChosenPrograms();
        insertAllPrograms(newPrograms);
    }

    @Query("DELETE FROM chosen_programs")
    public abstract void deleteAllChosenPrograms();
}
