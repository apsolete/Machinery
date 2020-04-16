package com.apsolete.machinery.calculation.belting.pbelt;

import com.apsolete.machinery.calculation.CalculationViewModel;

import android.app.Application;
import androidx.annotation.NonNull;

public class PbeltViewModel extends CalculationViewModel
{
	public PbeltViewModel(@NonNull Application application)
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
