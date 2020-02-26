package com.apsolete.machinery.common;

import android.view.View;

import androidx.lifecycle.Observer;

public abstract class CustomObserver<V extends View, T> implements Observer<T>
{
    protected V mView;

    public void setView(V view)
    {
        mView = view;
    }
}
