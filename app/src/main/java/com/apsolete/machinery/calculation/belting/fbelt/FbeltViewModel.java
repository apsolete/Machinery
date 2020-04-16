package com.apsolete.machinery.calculation.belting.fbelt;

import android.app.Application;

import androidx.annotation.NonNull;

import com.apsolete.machinery.calculation.CalculationViewModel;

public class FbeltViewModel extends CalculationViewModel
{
	public FbeltViewModel(@NonNull Application application)
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
