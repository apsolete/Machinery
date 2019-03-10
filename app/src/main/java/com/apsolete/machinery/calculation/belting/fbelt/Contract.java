package com.apsolete.machinery.calculation.belting.fbelt;

import com.apsolete.machinery.calculation.CalculationContract;

interface Contract
{
    interface Presenter extends CalculationContract.Presenter
    {

    }
<<<<<<< HEAD
    interface View extends Calculation.Contract.View<Presenter>
=======

    interface View extends CalculationContract.View<Presenter>
>>>>>>> feb4bb9... CalculationContract refactoring
    {

    }
}
