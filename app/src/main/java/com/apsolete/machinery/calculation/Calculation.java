package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;

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
        SettingsBase getSettings();
        boolean close();
    }
}
