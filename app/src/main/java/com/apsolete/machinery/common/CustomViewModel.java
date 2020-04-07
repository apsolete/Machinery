package com.apsolete.machinery.common;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class CustomViewModel extends AndroidViewModel
{
    public CustomViewModel(@NonNull Application application)
    {
        super(application);
    }

    public abstract void start();
}
