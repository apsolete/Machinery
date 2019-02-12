package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.utils.Numbers;

import android.os.*;

import java.util.List;

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
    private GearKits _gearKits = new GearKits();

    private int _calculatedRatios = 0;

    public ChangeGears()
    {
    }

    public ChangeGears(Parcel in)
    {
        _ratio = in.readDouble();
        _accuracy = in.readDouble();
        _gearsCount = in.readInt();
        _diffTeethGearing = in.readByte() == 1;
        _diffTeethDoubleGear = in.readByte() == 1;
        _isOneSet = in.readByte() == 1;
        _gearKits = in.readParcelable(GearKits.class.getClassLoader());
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
        out.writeDouble(_ratio);
        out.writeDouble(_accuracy);
        out.writeInt(_gearsCount);
        out.writeByte(_diffTeethGearing? (byte)1 : (byte)0);
        out.writeByte(_diffTeethDoubleGear? (byte)1 : (byte)0);
        out.writeByte(_isOneSet? (byte)1 : (byte)0);
        out.writeParcelable(_gearKits, flags);
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

//    public double getRatio()
//    {
//        return _ratio;
//    }

    public void setRatio(double ratio)
    {
        _ratio = ratio;
    }

//    public double getAccuracy()
//    {
//        return _accuracy;
//    }

    public void setAccuracy(double accuracy)
    {
        _accuracy = accuracy;
    }

//    public boolean isDiffTeethGearing()
//    {
//        return _diffTeethGearing;
//    }

    public void setDiffTeethGearing(boolean diffTeethGearing)
    {
        _diffTeethGearing = diffTeethGearing;
    }

//    public boolean isDiffTeethDoubleGear()
//    {
//        return _diffTeethDoubleGear;
//    }

    public void setDiffTeethDoubleGear(boolean diffTeethDoubleGear)
    {
        _diffTeethDoubleGear = diffTeethDoubleGear;
    }

    public void setGearKit(int gears, int[] kit)
    {
        _isOneSet = true;
        _gearsCount = gears;
        _gearKits.putZ0(kit);
    }

    public void setGearKit(int[] gs1, int[] gs2, int[] gs3, int[] gs4, int[] gs5, int[] gs6)
    {
        _isOneSet = false;
        _gearKits.putZ1(gs1);
        _gearKits.putZ2(gs2);
        _gearKits.putZ3(gs3);
        _gearKits.putZ4(gs4);
        _gearKits.putZ5(gs5);
        _gearKits.putZ6(gs6);
    }

//    public void setGearsCount(int count)
//    {
//        _gearsCount = count;
//    }

//    public int getGearsCount()
//    {
//        return _gearsCount;
//    }

//    public int[] getGears(int z)
//    {
//        return _gearKits.get(z);
//    }

//    public void setGears(int z, int[] gears)
//    {
//        _gearKits.put(z, gears);
//    }

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

            int[] gs1 = _gearKits.getZ1();
            int[] gs2 = _gearKits.getZ2();
            int[] gs3 = _gearKits.getZ3();
            int[] gs4 = _gearKits.getZ4();
            int[] gs5 = _gearKits.getZ5();
            int[] gs6 = _gearKits.getZ6();

            if (gs1 == null || gs2 == null)
                return;

            if (gs1.length > 0 && gs2.length > 0)
            {
                if (gs3 == null || gs3.length == 0 || gs4 == null || gs4.length == 0)
                {
                    calculateBy(gs1, gs2);
                }
                else //if (gs3.length > 0 && gs4.length > 0)
                {
                    if (gs5 == null || gs5.length == 0 || gs6 == null || gs6.length == 0)
                    {
                        calculateBy(gs1, gs2, gs3, gs4);
                    }
                    else //if (gs5.length > 0 && gs6.length > 0)
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

        int[] set = _gearKits.getZ0();
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
        return Math.abs(ratio - _ratio) <= _accuracy;
    }
}
