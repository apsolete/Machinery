package com.apsolete.machinery.calculation;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.UUID;

import com.apsolete.machinery.common.CustomViewModel;
import com.apsolete.machinery.common.Event;

public abstract class CalculationViewModel extends CustomViewModel implements Calculation.ViewModel
{
    protected MutableLiveData<Event<Calculation.Notify>> mNotificationEvent = new MutableLiveData<>();
    protected MutableLiveData<Event<UUID>> mCalculationEvent = new MutableLiveData<>(null);

    public CalculationViewModel(@NonNull Application application)
    {
        super(application);
    }

    public abstract void load();
    public abstract void save();
    public abstract boolean validate();
    public abstract void calculate();
    public abstract void clear();
    public abstract boolean close();

    public LiveData<Event<Calculation.Notify>> getNotificationEvent()
    {
        return mNotificationEvent;
    }

    public LiveData<Event<UUID>> getCalculationEvent()
    {
        return mCalculationEvent;
    }

}
