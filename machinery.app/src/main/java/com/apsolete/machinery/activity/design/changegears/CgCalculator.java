package com.apsolete.machinery.activity.design.changegears;

import android.os.*;
import java.util.*;

public class CgCalculator extends AsyncTask<int[], Integer, Void>
{
    public interface OnResultListener
    {
        void onResult(double ratio, int[] gears);
        void onCompleted();
    }
    
    private boolean _diffTeethGearing;
    private boolean _diffTeethDoubleGear;
    private double _ratio;
    private double _accuracy;
    private OnResultListener _resultListener;
    
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
                //_pb.setMax(z1Gears.length * z2Gears.length);
                //clear();
                //int p = 1;
                calculateBy(gs1, gs2);
            }
            else if (gs3.length > 0 && gs4.length > 0)
            {
                if (gs5 == null || gs6 == null)
                {
                    //clear();
                    calculateBy(gs1, gs2, gs3, gs4);
                }
                else if (gs5.length > 0 && gs6.length > 0)
                {
                    //clear();
                    calculateBy(gs1, gs2, gs3, gs4, gs5, gs6);
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values)
    {
        // TODO: Implement this method
        super.onProgressUpdate(values);
    }
    
    private void calculateBy(int[] gs1, int[] gs2)
    {
        // calculate by z1, z2
        for (int z1: gs1)
        {
            for (int z2: gs2)
            {
                if (_diffTeethGearing && z1 == z2)
                    continue;

                //_pb.setProgress(p++);
                double ratio = (double)z1 / (double)z2;
                if (checkRatio(ratio))
                {
                    if (!setResult(ratio, z1, z2, 0, 0, 0, 0))
                        break;
                }
            }
        }
    }
    
    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4)
    {
        // calculate by z1, z2, z3, z4
        for (int z1: gs1)
        {
            for (int z2: gs2)
            {
                if (_diffTeethGearing && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    if (_diffTeethDoubleGear && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        if (_diffTeethGearing && z3 == z4)
                            continue;

                        double ratio = (double)(z1 * z3) / (double)(z2 * z4);
                        if (checkRatio(ratio))
                        {
                            if (!setResult(ratio, z1, z2, z3, z4, 0, 0))
                                break;
                        }
                    }
                }
            }
        }
    }

    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4, int[] gs5, int[] gs6)
    {
        // calculate by z1, z2, z3, z4, z6
        for (int z1: gs1)
        {
            for (int z2: gs2)
            {
                if (_diffTeethGearing && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    if (_diffTeethDoubleGear && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        if (_diffTeethGearing && z1 == z2)
                            continue;

                        for (int z5: gs5)
                        {
                            if (_diffTeethDoubleGear && z4 == z5)
                                continue;

                            for (int z6: gs6)
                            {
                                if (_diffTeethGearing && z1 == z2)
                                    continue;

                                double ratio = (double)(z1 * z3 * z5) / (double)(z2 * z4 * z6);
                                if (checkRatio(ratio))
                                {
                                    if (!setResult(ratio, z1, z2, z3, z4, z5, z6))
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean setResult(double ratio, int z1, int z2, int z3, int z4, int z5, int z6)
    {
        if (_resultListener == null)
            return false;
            
        _resultListener.onResult(ratio, new int[]{z1, z2, z3, z4, z5, z6});
        return true;
    }
    
    private boolean checkRatio(double ratio)
    {
        if (_ratio == 0)
            return true;
        return (Math.abs(ratio - _ratio) <= _accuracy) ? true : false;
    }
}
