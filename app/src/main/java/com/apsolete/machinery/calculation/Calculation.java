package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.CustomSettingsFragment;

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
        CustomSettingsFragment getSettings();
        boolean close();
    }
}
