package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.*;
import android.os.*;

public final class ChangeGears extends CalculationModel
{
    public ChangeGears()
    {
    }

    public ChangeGears(Parcel parcel)
    {
    }

    @Override
    public int describeContents()
    {
        // TODO: Implement this method
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO: Implement this method
    }

    public static final Parcelable.Creator<ChangeGears> CREATOR = new Parcelable.Creator<ChangeGears>()
    {
        @Override
        public ChangeGears createFromParcel(Parcel parcel)
        {
            return new ChangeGears(parcel);
        }

        @Override
        public ChangeGears[] newArray(int size)
        {
            return new ChangeGears[size];
        }
    };
}
