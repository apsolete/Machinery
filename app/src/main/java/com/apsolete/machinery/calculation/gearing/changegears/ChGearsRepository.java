package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.apsolete.machinery.calculation.CalculationDatabase;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ChGearsRepository
{
    private ChGearsDao mDao;
    private LiveData<List<ChGearsEntity>> mAllChGears;

    public ChGearsRepository(Context context)
    {
        CalculationDatabase db = CalculationDatabase.getDatabase(context);
        mDao = db.changeGearsDao();
        mAllChGears = mDao.getAllChangeGears();
    }

    public LiveData<List<ChGearsEntity>> getAllChGears()
    {
        return mAllChGears;
    }

    public long insert(ChGearsEntity entity)
    {
        AtomicLong id = new AtomicLong();
        CalculationDatabase.writeExecutor.execute(()->
        {
            try
            {
                id.set(mDao.insertChangeGears(entity));
            }
            catch (Exception e)
            {
                id.set(0);
            }
        });
        return id.get();
    }

    public long upsert(ChGearsEntity entity)
    {
        long id = insert(entity);
        if (id == 0)
        {
            id = entity.id;
            CalculationDatabase.writeExecutor.execute(()->
            {
                mDao.updateChangeGears(entity);
            });
        }
        return id;
    }

    public ChGearsEntity getChangeGears(long id)
    {
        AtomicReference<ChGearsEntity> changeGears = new AtomicReference<>(null);
        try
        {
            CalculationDatabase.writeExecutor.execute(()->
            {
                changeGears.set(mDao.getChangeGearsById(id));
            });
            return changeGears.get();
        }
        catch (Exception e)
        {
            return changeGears.get();
        }
    }
}
