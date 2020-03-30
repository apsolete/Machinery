package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.ContentBase;

import java.text.*;
import java.util.*;
import java.math.*;

@Deprecated
public abstract class CalculationPresenter implements CalculationContract.Presenter
{
    private CalculationContract.View _view;

    protected CalculationPresenter(CalculationContract.View view)
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
    public ContentBase getContent()
    {
        return _view.getContent();
    }

    @Override
    public CalculationContract.View getView()
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
