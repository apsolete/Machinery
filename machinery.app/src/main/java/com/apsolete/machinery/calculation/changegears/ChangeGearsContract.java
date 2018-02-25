package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.Calculation;

public interface ChangeGearsContract
{
    public interface Presenter extends Calculation.Contract.Presenter
    {

    }

    public interface View extends Calculation.Contract.View<Presenter>
    {

    }
}