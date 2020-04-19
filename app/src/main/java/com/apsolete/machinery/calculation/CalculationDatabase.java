package com.apsolete.machinery.calculation;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.apsolete.machinery.calculation.gearing.changegears.db.ChGearsDao;
import com.apsolete.machinery.calculation.gearing.changegears.db.ChGearsEntity;
import com.apsolete.machinery.calculation.gearing.changegears.db.ChGearsResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    private static final ExecutorService executor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CalculationDatabase getInstance(final Context context)
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

    public static void execute(Runnable command)
    {
        try
        {
            executor.execute(command);
            executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
