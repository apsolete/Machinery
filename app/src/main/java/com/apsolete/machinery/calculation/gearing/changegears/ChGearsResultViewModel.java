package com.apsolete.machinery.calculation.gearing.changegears;

import java.text.NumberFormat;

public class ChGearsResultViewModel
{
    private ChGearsResult _result;
    private NumberFormat _numberFormat;

    public ChGearsResultViewModel(ChGearsResult result, NumberFormat format)
    {
        _result = result;
        _numberFormat = format;
    }

    String number()
    {
        return _result.number.toString();
    }
    String z1()
    {
        return _result.z1.toString();
    }
    String z2()
    {
        return _result.z2.toString();
    }
    String z3()
    {
        return _result.z3 > 0 ? _result.z3.toString() : null;
    }
    String z4()
    {
        return _result.z4 > 0 ? _result.z4.toString() : null;
    }
    String z5()
    {
        return _result.z5 > 0 ? _result.z5.toString() : null;
    }
    String z6()
    {
        return _result.z6 > 0 ? _result.z6.toString() : null;
    }
    String ratio()
    {
        return _numberFormat.format(_result.ratio);
    }
    String threadPitch()
    {
        return _result.threadPitch > 0 ? _numberFormat.format(_result.threadPitch) : null;
    }
}
