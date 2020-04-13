package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.apsolete.machinery.calculation.Calculation;
import com.apsolete.machinery.calculation.CalculationDatabase;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ChGearsRepository
{
    private ChGearsDao mDao;
    private LiveData<List<ChGearsEntity>> mAllChGears;
    //private LiveData<List<ChGearsEntity>> mAllChGears;

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
        AtomicLong id = new AtomicLong(0);
        CalculationDatabase.executor.execute(()->
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
            CalculationDatabase.executor.execute(()->
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
            CalculationDatabase.executor.execute(()->
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

    public LiveData<List<ChGearsResult>> getChGearsResults(long chgId)
    {
        return mDao.getResultsByChgId(chgId);
    }

    public long insert(ChGearsResult result)
    {
        AtomicLong id = new AtomicLong(0);
        CalculationDatabase.executor.execute(()->
        {
            try
            {
                id.set(mDao.insertChgResult(result));
            }
            catch (Exception e)
            {
                id.set(0);
            }
        });
        return id.get();
    }

    public long upsert(ChGearsResult result)
    {
        long id = insert(result);
        if (id == 0)
        {
            id = result.id;
            CalculationDatabase.executor.execute(()->
            {
                mDao.updateChgResult(result);
            });
        }
        return id;
    }

    public void deleteResultsById(long chgId)
    {
        CalculationDatabase.executor.execute(()->
        {
            mDao.deleteChgResults(chgId);
        });
    }
}
