package com.apsolete.machinery.calculation;
import android.support.v4.app.Fragment;

import com.apsolete.machinery.common.mvp.BaseContract;

import java.text.*;
import java.util.*;
import java.math.*;

public abstract class CalculationPresenter implements Calculation.Contract.Presenter
{
    private Calculation.Contract.View _view;

    protected CalculationPresenter(Calculation.Contract.View view)
    {
        _view = view;
    }

//    @Override
//    public void start()
//    {
//    }
//
//    @Override
//    public void stop()
//    {
//
//    }
//
//    @Override
//    public void save()
//    {
//    }
//
//    @Override
//    public void clear()
//    {
//    }
//
//    @Override
//    public void calculate()
//    {
//    }
//
//
//    @Override
//    public boolean close()
//    {
//        return true;
//    }


    @Override
    public Fragment getFragmentView()
    {
        return _view.asFragment();
    }

    @Override
    public Calculation.Contract.View getView()
    {
        return _view;
    }

    public static NumberFormat getNumberFormat(String pattern)
    {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat _numFormat = new DecimalFormat(pattern, formatSymbols);
        _numFormat.setRoundingMode(RoundingMode.CEILING);
        return _numFormat;
    }
}
