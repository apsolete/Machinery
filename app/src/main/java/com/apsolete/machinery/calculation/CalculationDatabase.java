package com.apsolete.machinery.calculation;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.apsolete.machinery.calculation.gearing.changegears.ChGearsDao;
import com.apsolete.machinery.calculation.gearing.changegears.ChGearsEntity;
import com.apsolete.machinery.calculation.gearing.changegears.ChGearsResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(version = 1,
        entities =
                {
                        ChGearsEntity.class,
                        ChGearsResult.class
                })
public abstract class CalculationDatabase extends RoomDatabase
{
    public abstract ChGearsDao changeGearsDao();

    private static volatile CalculationDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService executor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CalculationDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (CalculationDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CalculationDatabase.class, "calculation_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
