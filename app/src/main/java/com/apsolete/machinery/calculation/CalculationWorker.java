package com.apsolete.machinery.calculation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public abstract class CalculationWorker extends Worker
{
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
}
