package com.apsolete.machinery.calculation.changegears;

import android.os.*;

import com.apsolete.machinery.utils.ArrayUtils;

import java.util.*;

public class ChangeGearsCalculator
{
    private class AsyncCalc extends AsyncTask<ChangeGearsCalculator, Integer, Void>
    {
        @Override
        protected Void doInBackground(ChangeGearsCalculator... params)
        {
            params[0].calculateInternal();
            return null;
        }
    }
    
    public static final int Z0 = 0;
    public static final int Z1 = 1;
    public static final int Z2 = 2;
    public static final int Z3 = 3;
    public static final int Z4 = 4;
    public static final int Z5 = 5;
    public static final int Z6 = 6;
    
    public interface OnResultListener
    {
        void onResult(ChangeGearsResult result);
        void onProgress(int percent);
        void onCompleted(int count);
    }

    private OnResultListener _resultListener;

    private double _ratio = 0;
    private double _accuracy;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;
    private boolean _isOneSet = false;
    private int _gearsCount = 2;
    private Map<Integer, int[]> _gearsSets = new HashMap<>();

    private int _calculatedRatios = 0;

    ChangeGearsCalculator()
    {
        _gearsSets.put(Z0, null);
        _gearsSets.put(Z1, null);
        _gearsSets.put(Z2, null);
        _gearsSets.put(Z3, null);
        _gearsSets.put(Z4, null);
        _gearsSets.put(Z5, null);
        _gearsSets.put(Z6, null);
    }

//    ChangeGearsCalculator(double ratio, double accuracy, boolean dtg, boolean dtdg, OnResultListener resultListener)
//    {
//        _ratio = ratio;
//        _accuracy = accuracy;
//        _diffTeethGearing = dtg;
//        _diffTeethDoubleGear = dtdg;
//        _resultListener = resultListener;
//    }

    public double getRatio()
    {
        return _ratio;
    }

    public void setRatio(double ratio)
    {
        _ratio = ratio;
    }

    public double getAccuracy()
    {
        return _accuracy;
    }

    public void setAccuracy(double accuracy)
    {
        _accuracy = accuracy;
    }

    public boolean isDiffTeethGearing()
    {
        return _diffTeethGearing;
    }

    public void setDiffTeethGearing(boolean diffTeethGearing)
    {
        _diffTeethGearing = diffTeethGearing;
    }

    public boolean isDiffTeethDoubleGear()
    {
        return _diffTeethDoubleGear;
    }

    public void setDiffTeethDoubleGear(boolean diffTeethDoubleGear)
    {
        _diffTeethDoubleGear = diffTeethDoubleGear;
    }
    
    public void setGearsSet(int gears, int[] set)
    {
        _isOneSet = true;
        _gearsCount = gears;
        _gearsSets.put(Z0, set);
    }
    
    public void setGearsSet(int[] gs1, int[] gs2, int[] gs3, int[] gs4, int[] gs5, int[] gs6)
    {
        _isOneSet = false;
        _gearsSets.put(Z1, gs1);
        _gearsSets.put(Z2, gs2);
        _gearsSets.put(Z3, gs3);
        _gearsSets.put(Z4, gs4);
        _gearsSets.put(Z5, gs5);
        _gearsSets.put(Z6, gs6);
    }
    
    public void setGearsCount(int count)
    {
        _gearsCount = count;
    }

    public int getGearsCount()
    {
        return _gearsCount;
    }

    public int[] getGears(int z)
    {
        return _gearsSets.get(z);
    }

    public void setGears(int z, int[] gears)
    {
        _gearsSets.put(z, gears);
    }

    public void setResultListener(OnResultListener resultListener)
    {
        _resultListener = resultListener;
    }

    private void calculateInternal()
    {
        _calculatedRatios = 0;
        
        try
        {
            if (_isOneSet)
            {
                calculateByOneSet();
                return;
            }

            int[] gs1 = _gearsSets.get(Z1);
            int[] gs2 = _gearsSets.get(Z2);
            int[] gs3 = _gearsSets.get(Z3);
            int[] gs4 = _gearsSets.get(Z4);
            int[] gs5 = _gearsSets.get(Z5);
            int[] gs6 = _gearsSets.get(Z6);

            if (gs1 == null || gs2 == null)
                return;
            else if (gs1.length > 0 && gs2.length > 0)
            {
                if (gs3 == null || gs4 == null)
                {
                    calculateBy(gs1, gs2);
                }
                else if (gs3.length > 0 && gs4.length > 0)
                {
                    if (gs5 == null || gs6 == null)
                    {
                        calculateBy(gs1, gs2, gs3, gs4);
                    }
                    else if (gs5.length > 0 && gs6.length > 0)
                    {
                        calculateBy(gs1, gs2, gs3, gs4, gs5, gs6);
                    }
                }
            }
        }
        finally
        {
            _resultListener.onCompleted(_calculatedRatios);
        }
        return;
    }

    private void publishProgress(int values)
    {
        _resultListener.onProgress(values);
    }

    public void calculate()
    {
        AsyncCalc ac = new AsyncCalc();
        ac.execute(this);
    }
    
    private void calculateBy(int[] gs1, int[] gs2)
    {
        // calculate by z1, z2
        int count = gs1.length * gs2.length;
        int i = 0;
        for (int z1: gs1)
        {
            publishProgress((100 * i++) / count);
            for (int z2: gs2)
            {
                publishProgress((100 * i++) / count);
                if (_diffTeethGearing && z1 == z2)
                    continue;

                calculateRatio(z1, z2);
            }
        }
    }
    
    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4)
    {
        // calculate by z1, z2, z3, z4
        int count = gs1.length * gs2.length * gs3.length * gs4.length;
        int i = 0;
        for (int z1: gs1)
        {
            publishProgress((100 * i++) / count);
            for (int z2: gs2)
            {
                publishProgress((100 * i++) / count);
                if (_diffTeethGearing && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    publishProgress((100 * i++) / count);
                    if (_diffTeethDoubleGear && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        publishProgress((100 * i++) / count);
                        if (_diffTeethGearing && z3 == z4)
                            continue;

                        calculateRatio(z1, z2, z3, z4);
                    }
                }
            }
        }
    }

    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4, int[] gs5, int[] gs6)
    {
        // calculate by z1, z2, z3, z4, z6
        int count = gs1.length * gs2.length * gs3.length * gs4.length * gs5.length * gs6.length;
        int i = 0;
        for (int z1: gs1)
        {
            publishProgress((100 * i++) / count);
            for (int z2: gs2)
            {
                publishProgress((100 * i++) / count);
                if (_diffTeethGearing && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    publishProgress((100 * i++) / count);
                    if (_diffTeethDoubleGear && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        publishProgress((100 * i++) / count);
                        if (_diffTeethGearing && z1 == z2)
                            continue;

                        for (int z5: gs5)
                        {
                            publishProgress((100 * i++) / count);
                            if (_diffTeethDoubleGear && z4 == z5)
                                continue;

                            for (int z6: gs6)
                            {
                                publishProgress((100 * i++) / count);
                                if (_diffTeethGearing && z1 == z2)
                                    continue;

                                calculateRatio(z1, z2, z3, z4, z5, z6);
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateByOneSet()
    {
        int count = _gearsCount;
        if (count == 3) count = 2;
        if (count == 5) count = 4;

        int[] set = _gearsSets.get(0);
        for (int z1: set)
        {
            for (int z2 : set)
            {
                if (z1 == z2)
                    continue;

                if (count == 2)
                    calculateRatio(z1, z2);
                else
                {
                    for (int z3 : set)
                    {
                        if (z1 == z3 || z2 == z3)
                            continue;
                        for (int z4 : set)
                        {
                            if (z1 == z4 || z2 == z4 || z3 == z4)
                                continue;

                            if (count == 4)
                                calculateRatio(z1, z2, z3, z4);
                            else
                            {
                                for (int z5 : set)
                                {
                                    if (z1 == z5 || z2 == z5 || z3 == z5 || z4 == z5) 
                                        continue;
                                    for (int z6 : set)
                                    {
                                        if (z1 == z6 || z2 == z6 || z3 == z6 || z4 == z6 || z5 == z6)
                                            continue;
                                        calculateRatio(z1, z2, z3, z4, z5, z6);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean calculateRatio(int z1, int z2)
    {
        double ratio = (double)(z1) / (double)(z2);
        if (checkRatio(ratio))
        {
            _calculatedRatios++;
            if (_resultListener != null)
            {
                //publishProgress((100 * i++) / count);
                _resultListener.onResult(new ChangeGearsResult(_calculatedRatios, ratio, new int[]{z1, z2, 0, 0, 0, 0}));
            }
            return true;
        }
        return false;
    }

    private boolean calculateRatio(int z1, int z2, int z3, int z4)
    {
        double ratio = (double)(z1 * z3) / (double)(z2 * z4);
        if (checkRatio(ratio))
        {
            _calculatedRatios++;
            if (_resultListener != null)
            {
                //publishProgress((100 * i++) / count);
                _resultListener.onResult(new ChangeGearsResult(_calculatedRatios, ratio, new int[]{z1, z2, z3, z4, 0, 0}));
            }
            return true;
        }
        return false;
    }

    private boolean calculateRatio(int z1, int z2, int z3, int z4, int z5, int z6)
    {
        double ratio = (double)(z1 * z3 * z5) / (double)(z2 * z4 * z6);
        if (checkRatio(ratio))
        {
            _calculatedRatios++;
            if (_resultListener != null)
            {
                //publishProgress((100 * i++) / count);
                _resultListener.onResult(new ChangeGearsResult(_calculatedRatios, ratio, new int[]{z1, z2, z3, z4, z5, z6}));
            }
            return true;
        }
        return false;
    }

    private boolean checkRatio(double ratio)
    {
        if (_ratio == 0)
            return true;
        return (Math.abs(ratio - _ratio) <= _accuracy) ? true : false;
    }
}
