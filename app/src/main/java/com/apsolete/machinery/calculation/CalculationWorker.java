package com.apsolete.machinery.calculation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apsolete.machinery.common.OnProgressListener;

public abstract class CalculationWorker extends Worker
{
    private int _total = 1;
    private int _progress = 0;
    //private OnProgressListener _progressListener;

    public CalculationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        try
        {
            calculate();
        }
        catch (Exception e)
        {
            return Result.failure();
        }
        return Result.success();
    }

    protected abstract void calculate();

//    protected void setOnProgressListener(OnProgressListener listener)
//    {
//        if (listener == null)
//            throw new NullPointerException();
//
//        _progressListener = listener;
//    }

    protected void publishProgress()
    {
        int percent = (100 * _progress) / _total;
        setProgressAsync(new Data.Builder().putInt("PROGRESS", percent).build());
        //_progressListener.onProgress((100 * _progress) / _total);
        _progress++;
    }

    protected void resetProgress(int total)
    {
        _total = total;
        _progress = 0;
    }

}
