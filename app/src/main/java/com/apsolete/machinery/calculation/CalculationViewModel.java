package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.CustomViewModel;

public abstract class CalculationViewModel extends CustomViewModel
{
    public abstract void save();
    public abstract void clear();
    public abstract void calculate();
    public abstract boolean close();
}
