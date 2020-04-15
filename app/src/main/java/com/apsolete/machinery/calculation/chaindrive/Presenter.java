package com.apsolete.machinery.calculation.chaindrive;

import com.apsolete.machinery.calculation.CalculationPresenter;

@Deprecated
public class Presenter extends CalculationPresenter implements Contract.Presenter
{
	private ChainDriveViewModel _model;
	
    public Presenter(Contract.View view)
    {
        super(view);
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
		_model.calculate();
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

    @Override
    public void stop()
    {

    }
}
