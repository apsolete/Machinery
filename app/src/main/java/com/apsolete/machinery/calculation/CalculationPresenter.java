package com.apsolete.machinery.calculation;
import java.text.*;
import java.util.*;
import java.math.*;

public abstract class CalculationPresenter implements Calculation.Contract.Presenter
{
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
    
    public static NumberFormat getNumberFormat(String pattern)
    {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat _numFormat = new DecimalFormat(pattern, formatSymbols);
        _numFormat.setRoundingMode(RoundingMode.CEILING);
        return _numFormat;
    }
}
