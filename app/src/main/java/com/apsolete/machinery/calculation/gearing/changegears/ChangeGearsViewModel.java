package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.lifecycle.ViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ChangeGearsViewModel extends ViewModel
{
    private boolean _oneSet;
    private GearKits _gearKits = new GearKits();
    private int _oneSetGearsCount = 2;
    private boolean _diffLockedZ2Z3 = true;
    private boolean _diffLockedZ4Z5 = true;
    private boolean _diffGearingZ1Z2 = true;
    private boolean _diffGearingZ3Z4 = true;
    private boolean _diffGearingZ5Z6 = true;

    private int _calculationMode;

    private double _ratio = 1.25;
    private double _ratioNumerator = 34;
    private double _ratioDenominator = 56;
    private boolean _ratioAsFraction = true;
    private ThreadPitchUnit _threadPitchUnit = ThreadPitchUnit.mm;
    private ThreadPitchUnit _leadscrewPitchUnit = ThreadPitchUnit.mm;
    private double _leadscrewPitch = 4;
    private double _threadPitch = 0.75;
    private NumberFormat _ratioFormat;
    private double _calculatedRatio;
    private int _firstResultNumber = 1;
    private int _lastResultNumber = 1;
    private ArrayList<Contract.Result> _results = new ArrayList<>();
    private ChangeGears _calculator;
}
