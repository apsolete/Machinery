package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.utils.Numbers;

import android.os.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChangeGears extends CalculationModel
{
    private class AsyncCalc extends AsyncTask<Void, Integer, Void>
    {
        ChangeGears _cg;

        public AsyncCalc(ChangeGears cg)
        {
            _cg = cg;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            _cg.calculateInternal();
            return null;
        }
    }
    
    private OnResultListener<CgResult> _resultListener;
    private ProgressPublisher _progress;

    private double _ratio = 0;
    private double _accuracy;
    private boolean _diffTeethGearing = true;
    private boolean _diffTeethDoubleGear = true;
    private boolean _isOneSet = false;
    private int _gearsCount = 2;
    private Map<Integer, int[]> _gearsSets = new HashMap<>();

    private int _calculatedRatios = 0;

    public ChangeGears()
    {
        _gearsSets.put(G.Z0, null);
        _gearsSets.put(G.Z1, null);
        _gearsSets.put(G.Z2, null);
        _gearsSets.put(G.Z3, null);
        _gearsSets.put(G.Z4, null);
        _gearsSets.put(G.Z5, null);
        _gearsSets.put(G.Z6, null);
    }

    public ChangeGears(Parcel parcel)
    {
    }

    @Override
    public int describeContents()
    {
        // TODO: Implement this method
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO: Implement this method
    }

    public static final Parcelable.Creator<ChangeGears> CREATOR = new Parcelable.Creator<ChangeGears>()
    {
        @Override
        public ChangeGears createFromParcel(Parcel parcel)
        {
            return new ChangeGears(parcel);
        }

        @Override
        public ChangeGears[] newArray(int size)
        {
            return new ChangeGears[size];
        }
    };

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
        _gearsSets.put(G.Z0, set);
    }

    public void setGearsSet(int[] gs1, int[] gs2, int[] gs3, int[] gs4, int[] gs5, int[] gs6)
    {
        _isOneSet = false;
        _gearsSets.put(G.Z1, gs1);
        _gearsSets.put(G.Z2, gs2);
        _gearsSets.put(G.Z3, gs3);
        _gearsSets.put(G.Z4, gs4);
        _gearsSets.put(G.Z5, gs5);
        _gearsSets.put(G.Z6, gs6);
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

    public void setResultListener(OnResultListener<CgResult> resultListener)
    {
        _resultListener = resultListener;
        _progress = new ProgressPublisher(_resultListener);
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

            int[] gs1 = _gearsSets.get(G.Z1);
            int[] gs2 = _gearsSets.get(G.Z2);
            int[] gs3 = _gearsSets.get(G.Z3);
            int[] gs4 = _gearsSets.get(G.Z4);
            int[] gs5 = _gearsSets.get(G.Z5);
            int[] gs6 = _gearsSets.get(G.Z6);

            if (gs1 == null || gs2 == null)
                return;
            else if (gs1.length > 0 && gs2.length > 0)
            {
                if (gs3 == null || gs3.length == 0 || gs4 == null || gs4.length == 0)
                {
                    calculateBy(gs1, gs2);
                }
                else if (gs3.length > 0 && gs4.length > 0)
                {
                    if (gs5 == null || gs5.length == 0 || gs6 == null || gs6.length == 0)
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

    public void calculate()
    {
        ChangeGears.AsyncCalc asyncCalc = new ChangeGears.AsyncCalc(this);
        asyncCalc.execute();
    }

    private void calculateBy(int[] gs1, int[] gs2)
    {
        // calculate by z1, z2
        int totalResults = gs1.length * gs2.length;
        _progress.reset(totalResults);
        for (int z1: gs1)
        {
            _progress.publish();
            for (int z2: gs2)
            {
                _progress.publish();
                if (_diffTeethGearing && z1 == z2)
                    continue;

                calculateRatio(z1, z2);
            }
        }
    }

    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4)
    {
        // calculate by z1, z2, z3, z4
        int totalResults = gs1.length * gs2.length * gs3.length * gs4.length;
        _progress.reset(totalResults);
        for (int z1: gs1)
        {
            _progress.publish();
            for (int z2: gs2)
            {
                _progress.publish();
                if (_diffTeethGearing && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    _progress.publish();
                    if (_diffTeethDoubleGear && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        _progress.publish();
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
        int totalResults = gs1.length * gs2.length
                * gs3.length * gs4.length * gs5.length * gs6.length;
        _progress.reset(totalResults);
        for (int z1: gs1)
        {
            _progress.publish();
            for (int z2: gs2)
            {
                _progress.publish();
                if (_diffTeethGearing && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    _progress.publish();
                    if (_diffTeethDoubleGear && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        _progress.publish();
                        if (_diffTeethGearing && z3 == z4)
                            continue;

                        for (int z5: gs5)
                        {
                            _progress.publish();
                            if (_diffTeethDoubleGear && z4 == z5)
                                continue;

                            for (int z6: gs6)
                            {
                                _progress.publish();
                                if (_diffTeethGearing && z5 == z6)
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
        List<List<Integer>> combinations = Numbers.combinations(set.length, count);
        List<List<Integer>> permutations = Numbers.permutations(count);
        int totalResults = combinations.size() * permutations.size();
        _progress.reset(totalResults);
        for (List<Integer> comb: combinations)
        {
            _progress.publish();
            for (List<Integer> perm: permutations)
            {
                _progress.publish();
                if (count == 2)
                    calculateRatio(
                            set[comb.get(perm.get(0))],
                            set[comb.get(perm.get(1))]);
                else if (count == 4)
                    calculateRatio(
                            set[comb.get(perm.get(0))],
                            set[comb.get(perm.get(1))],
                            set[comb.get(perm.get(2))],
                            set[comb.get(perm.get(3))]);
                else if (count == 6)
                    calculateRatio(
                            set[comb.get(perm.get(0))],
                            set[comb.get(perm.get(1))],
                            set[comb.get(perm.get(2))],
                            set[comb.get(perm.get(3))],
                            set[comb.get(perm.get(4))],
                            set[comb.get(perm.get(5))]);
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
                _resultListener.onResult(new CgResult(_calculatedRatios, ratio, new int[]{z1, z2, 0, 0, 0, 0}));
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
                _resultListener.onResult(new CgResult(_calculatedRatios, ratio, new int[]{z1, z2, z3, z4, 0, 0}));
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
                _resultListener.onResult(new CgResult(_calculatedRatios, ratio, new int[]{z1, z2, z3, z4, z5, z6}));
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
