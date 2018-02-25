package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.Calculation;
import com.apsolete.machinery.calculation.CalculationView;

public final class ChangeGearsVieww extends CalculationView implements ChangeGearsContract.View
{
    public ChangeGearsVieww()
    {
        super(Calculation.CHANGEGEARS, 1, 1);
    }

    @Override
    public void setPresenter(ChangeGearsContract.Presenter presenter)
    {
        
    }
}
