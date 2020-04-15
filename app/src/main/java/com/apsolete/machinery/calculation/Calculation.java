package com.apsolete.machinery.calculation;

import java.util.UUID;

public interface Calculation
{
    interface Model
    {

    }

    interface ViewModel
    {
        boolean validate();
        void save();
        void clear();
        void calculate();
        boolean close();
    }

    interface View
    {

    }
}
