package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChgDao
{
    @Query("SELECT * FROM changegears")
    LiveData<List<ChgEntity>> getAllChangeGears();
    @Insert
    long insertChangeGears(ChgEntity entity);
    @Update
    int updateChangeGears(ChgEntity entity);
    @Delete
    int deleteChangeGears(ChgEntity entity);

    @Query("SELECT * FROM chgresult")
    LiveData<List<ChgResult>> getAllChgResults();
    @Insert
    long insertChgResult(ChgResult result);
    @Update
    int updateChgResult(ChgResult result);
    @Delete
    int deleteChgResult(ChgResult result);

    @Query("SELECT * FROM chgresult WHERE chg_id = :chgId")
    LiveData<List<ChgResult>> getResultsByChgId(long chgId);
}
