package com.apsolete.machinery.calculation;

public interface Calculation
{
    interface Model
    {

    }

    interface ViewModel
    {
        void save();
        void clear();
        void calculate();
        boolean close();
    }

    interface View
    {

    }
}
