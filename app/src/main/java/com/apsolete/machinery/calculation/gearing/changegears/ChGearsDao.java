package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChGearsDao
{
    @Query("SELECT * FROM changegears")
    LiveData<List<ChGearsEntity>> getAllChangeGears();
    @Query("SELECT * FROM changegears WHERE Id = :id")
    ChGearsEntity getChangeGearsById(long id);
    @Insert
    long insertChangeGears(ChGearsEntity entity);
    @Update
    int updateChangeGears(ChGearsEntity entity);
    @Delete
    int deleteChangeGears(ChGearsEntity entity);

    @Query("SELECT * FROM changegearsresult")
    LiveData<List<ChGearsResult>> getAllChgResults();
    @Insert
    long insertChgResult(ChGearsResult result);
    @Update
    int updateChgResult(ChGearsResult result);
    @Delete
    int deleteChgResult(ChGearsResult result);

    @Query("DELETE FROM changegearsresult WHERE chg_id = :chgId")
    void deleteChgResults(long chgId);

    @Query("SELECT * FROM changegearsresult WHERE chg_id = :chgId")
    LiveData<List<ChGearsResult>> getResultsByChgId(long chgId);
}
