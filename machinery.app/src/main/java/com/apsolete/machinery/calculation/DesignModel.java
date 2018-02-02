package com.apsolete.machinery.calculation;

import android.os.*;

public abstract class DesignModel<T> implements Parcelable
{
    public final Parcelable.Creator<T> CREATOR = new Parcelable.Creator<T>()
    {
        @Override
        public T createFromParcel(Parcel parcel)
        {
            return null;
        }

        @Override
        public T[] newArray(int size)
        {
            java.util.ArrayList<T> list = new java.util.ArrayList<T>(size);
            return (T[]) list.toArray();
        }
    };
}
