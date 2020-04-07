package com.apsolete.machinery.calculation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;

import com.apsolete.machinery.common.CustomViewModel;

public abstract class CalculationViewModel extends CustomViewModel implements Calculation.ViewModel
{
    public CalculationViewModel(@NonNull Application application)
    {
        super(application);
    }

    public abstract void save();
    public abstract void clear();
    public abstract void calculate();
    public abstract boolean close();
}
