package com.apsolete.machinery.calculation.gearing.changegears;

import java.util.Arrays;
import java.text.*;

import com.apsolete.machinery.calculation.*;

public class CgResult implements ChangeGearsContract.Result
{
    private int _id;
    private double _ratio;
    private int[] _gears = new int[6];
    private boolean _thrPitch = false;
    private double _leadscrewPitch = 0.0;
    private NumberFormat _numFormat;

    public CgResult(int id, double ratio, int[] gears)
    {
        _id = id;
        _ratio = ratio;
        _gears = Arrays.copyOf(gears, 6);
        _numFormat = CalculationPresenter.getNumberFormat("#0.0#####");
    }

    public CgResult(int id, double ratio, int[] gears, double lsPitch, NumberFormat numformat)
    {
        _id = id;
        _ratio = ratio;
        _gears = Arrays.copyOf(gears, 6);
        setLeadscrewPitch(lsPitch);
        setFormat(numformat);
    }

    @Override
    public String id()
    {
        return Integer.toString(_id);
    }

    @Override
    public String z1()
    {
        return Integer.toString(_gears[0]);
    }

    @Override
    public String z2()
    {
        return Integer.toString(_gears[1]);
    }

    @Override
    public String z3()
    {
        return (_gears[2] > 0) ? Integer.toString(_gears[2]) : null;
    }

    @Override
    public String z4()
    {
        return (_gears[3] > 0) ? Integer.toString(_gears[3]) : null;
    }

    @Override
    public String z5()
    {
        return (_gears[4] > 0) ? Integer.toString(_gears[4]) : null;
    }

    @Override
    public String z6()
    {
        return (_gears[5] > 0) ? Integer.toString(_gears[5]) : null;
    }

    @Override
    public String ratio()
    {
        return _numFormat.format(_ratio);
    }

    @Override
    public String threadPitch()
    {
        return _thrPitch ? _numFormat.format(_ratio * _leadscrewPitch) : null;
    }
    
    public void setLeadscrewPitch(double lsPitch)
    {
        _leadscrewPitch = lsPitch;
        _thrPitch = _leadscrewPitch > 0;
    }
    
    public void setFormat(NumberFormat numformat)
    {
        _numFormat = numformat;
    }
}
