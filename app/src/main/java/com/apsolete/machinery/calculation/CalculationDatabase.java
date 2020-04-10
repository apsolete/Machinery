package com.apsolete.machinery.calculation;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.apsolete.machinery.calculation.gearing.changegears.ChgDao;
import com.apsolete.machinery.calculation.gearing.changegears.ChgEntity;
import com.apsolete.machinery.calculation.gearing.changegears.ChgResult;

@Database(version = 1,
        entities =
                {
                        ChgEntity.class,
                        ChgResult.class
                })
public abstract class CalculationDatabase extends RoomDatabase
{
    public abstract ChgDao changeGearsDao();
}
