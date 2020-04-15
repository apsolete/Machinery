package com.apsolete.machinery.calculation.gearing.gearwheel;

import com.apsolete.machinery.calculation.CalculationModel;
import com.apsolete.machinery.calculation.CalculationViewModel;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GearWheelViewModel extends CalculationViewModel
{
    public GearWheelViewModel(@NonNull Application application)
    {
        super(application);
    }

    @Override
    public boolean validate()
    {
        return false;
    }

    @Override
    public void save()
    {

    }

    @Override
    public void clear()
    {

    }

    @Override
    public void calculate()
    {

    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public void start()
    {

    }
}
