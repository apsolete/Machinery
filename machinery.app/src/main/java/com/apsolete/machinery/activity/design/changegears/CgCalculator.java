package com.apsolete.machinery.activity.design.changegears;

import android.os.*;

public class CgCalculator extends AsyncTask<int[], Integer, Void>
{
    public interface OnResultListener
    {
        void onResult(double ratio, int[] gears);
        void onProgress(int percent);
        void onCompleted();
    }
    
    private boolean _diffTeethGearing;
    private boolean _diffTeethDoubleGear;
    private double _ratio;
    private double _accuracy;
    private OnResultListener _resultListener;
    private boolean _oneSet = false;
    
    public CgCalculator(double ratio, double accuracy, boolean dtg, boolean dtdg, OnResultListener resultListener)
    {
        _ratio = ratio;
        _accuracy = accuracy;
        _diffTeethGearing = dtg;
        _diffTeethDoubleGear = dtdg;
        _resultListener = resultListener;
    }
    
    @Override
    protected Void doInBackground(int[]... params)
    {
        if (_oneSet)
        {
            int[] set = params[0];
            int gears = params[1][0];
            calculateByOneSet(set, gears);
            return null;
        }

        int[] gs1 = params[0];
        int[] gs2 = params[1];
        int[] gs3 = params[2];
        int[] gs4 = params[3];
        int[] gs5 = params[4];
        int[] gs6 = params[5];
        
        if (gs1 == null || gs2 == null)
            return null;
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
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values)
    {
        super.onProgressUpdate(values);
        _resultListener.onProgress(values[0]);
    }

    public void calculate(int[] gs1, int[] gs2, int[] gs3, int[] gs4, int[] gs5, int[] gs6)
    {
        this.execute(gs1, gs2, gs3, gs4, gs5, gs6);
    }

    public void calculate(int[] set, int[] gears)
    {
        _oneSet = true;
        this.execute(set, gears);
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

                calculateRatio(z1, z2, 1, 1, 1, 1);
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

                        calculateRatio(z1, z2, z3, z4, 1, 1);
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

    private void calculateByOneSet(int[] set, int gears)
    {
        int count = gears;
        if (count == 3) count = 2;
        if (count == 5) count = 4;

        for (int z1: set)
        {
            for (int z2 : set)
            {
                if (z1 == z2)
                    continue;

                if (count == 2)
                    calculateRatio(z1, z2, 1, 1, 1, 1);
                else
                {
                    for (int z3 : set)
                    {
                        for (int z4 : set)
                        {
                            if (z1 == z2 || z1 == z3 || z1 == z4 || z2 == z3 || z2 == z4 || z3 == z4)
                                continue;

                            if (count == 4)
                                calculateRatio(z1, z2, z3, z4, 1, 1);
                            else
                            {
                                for (int z5 : set)
                                {
                                    for (int z6 : set)
                                    {
                                        if (z1 == z2 || z1 == z3 || z1 == z4 || z1 == z5 || z1 == z6 ||
                                                z2 == z3 || z2 == z4 || z2 == z5 || z2 == z6 ||
                                                z3 == z4 || z3 == z5 || z3 == z6 ||
                                                z4 == z5 || z4 == z6 ||
                                                z5 == z6)
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

    private void calculateRatio(int z1, int z2, int z3, int z4, int z5, int z6)
    {
        double ratio = (double)(z1 * z3 * z5) / (double)(z2 * z4 * z6);
        if (checkRatio(ratio))
        {
            if (_resultListener != null)
            {
                //publishProgress((100 * i++) / count);
                _resultListener.onResult(ratio, new int[]{z1, z2, z3>1?z3:0, z4>1?z4:0, z5>1?z5:0, z6>1?z6:0});
            }
        }
    }
    
    private boolean checkRatio(double ratio)
    {
        if (_ratio == 0)
            return true;
        return (Math.abs(ratio - _ratio) <= _accuracy) ? true : false;
    }
}
