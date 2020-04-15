package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.apsolete.machinery.calculation.CalculationDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ChGearsRepository
{
    private ChGearsDao mDao;
    private LiveData<List<ChGearsEntity>> mAllChGears;

    public ChGearsRepository(Context context)
    {
        CalculationDatabase db = CalculationDatabase.getInstance(context);
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
        CalculationDatabase.execute(()->
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
        //CalculationDatabase.executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        return id.get();
    }

    public long upsert(ChGearsEntity entity)
    {
        long id = insert(entity);
        if (id == 0)
        {
            id = entity.id;
            CalculationDatabase.execute(()->
            {
                try
                {
                    mDao.updateChangeGears(entity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
        return id;
    }

    public ChGearsEntity getChangeGears(long id)
    {
        try
        {
            return mDao.getChangeGearsById(id);
        }
        catch (Exception e)
        {
            return null;
        }

//        AtomicReference<ChGearsEntity> changeGears = new AtomicReference<>(null);
//        try
//        {
//            CalculationDatabase.executor.execute(()->
//            {
//                changeGears.set(mDao.getChangeGearsById(id));
//            });
//            return changeGears.get();
//        }
//        catch (Exception e)
//        {
//            return changeGears.get();
//        }
    }

    public List<ChGearsResult> getChGearsResultsLive(long chgId)
    {
        AtomicReference<List<ChGearsResult>> results = new AtomicReference<>();
        CalculationDatabase.execute(()->
        {
            try
            {
                results.set(mDao.getAllResultsByChgId(chgId));
            }
            catch (Exception e)
            {
                results.set(new ArrayList<>());
            }
        });
        return results.get();
    }

    public List<ChGearsResult> getChGearsResults(long chgId, int from, int count)
    {
        AtomicReference<List<ChGearsResult>> results = new AtomicReference<>();
        CalculationDatabase.execute(()->
        {
            try
            {
                results.set(mDao.getPartResultsByChgId(chgId, from, count));
            }
            catch (Exception e)
            {
                results.set(new ArrayList<>());
            }
        });
        return results.get();
    }

    public long insert(ChGearsResult result)
    {
        AtomicLong id = new AtomicLong(0);
        CalculationDatabase.execute(()->
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
            CalculationDatabase.execute(()->
            {
                mDao.updateChgResult(result);
            });
        }
        return id;
    }

    public void deleteResultsById(long chgId)
    {
        CalculationDatabase.execute(()->
        {
            mDao.deleteChgResults(chgId);
        });
    }
}
