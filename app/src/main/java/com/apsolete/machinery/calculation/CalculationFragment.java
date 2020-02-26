package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.CustomFragment;

import androidx.lifecycle.ViewModel;

public abstract class CalculationFragment<VM extends ViewModel> extends CustomFragment<VM>
{
    private int _type;

    public CalculationFragment(int type, int layout, int title)
    {
        super(layout, title);
        _type = type;
    }

    public int type()
    {
        return _type;
    }
}
