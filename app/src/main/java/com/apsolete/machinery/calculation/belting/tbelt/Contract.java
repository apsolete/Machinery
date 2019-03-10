package com.apsolete.machinery.calculation.belting.tbelt;

import com.apsolete.machinery.calculation.CalculationContract;

interface Contract
{
    interface Presenter extends CalculationContract.Presenter
    {

    }

    interface View extends CalculationContract.View<Presenter>
    {

    }
}
