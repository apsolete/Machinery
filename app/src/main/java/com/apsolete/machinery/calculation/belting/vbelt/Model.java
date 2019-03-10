package com.apsolete.machinery.calculation.belting.vbelt;

import com.apsolete.machinery.calculation.CalculationModel;
import android.os.Parcel;
import android.os.Parcelable;

public class Model extends CalculationModel
{
	public Model()
	{

	}

	public Model(Parcel in)
	{

	}

	@Override
	protected void onCalculate()
	{
		// TODO: Implement this method
	}

	@Override
	protected void onLoad()
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

	public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>()
    {
        @Override
        public Model createFromParcel(Parcel parcel)
        {
            return new Model(parcel);
        }

        @Override
        public Model[] newArray(int size)
        {
            return new Model[size];
        }
    };
}
