package com.apsolete.machinery.calculation.belting.pbelt;

import com.apsolete.machinery.calculation.Calculation;

interface Contract
{
    interface Presenter extends Calculation.Contract.Presenter
    {

    }

    interface View extends Calculation.Contract.View<Presenter>
    {

    }
}
