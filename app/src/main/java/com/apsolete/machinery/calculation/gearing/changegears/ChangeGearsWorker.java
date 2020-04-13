package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.apsolete.machinery.calculation.*;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.Numbers;

import java.util.List;

public class ChangeGearsWorker extends CalculationWorker
{
    public interface Result
    {
        String id();
        String z1();
        String z2();
        String z3();
        String z4();
        String z5();
        String z6();
        String ratio();
        String threadPitch();
    }

    private OnResultListener<ChangeGearsResult> _resultListener;

    private ChGearsRepository mRepository;
    private ChGearsEntity mEntity;

    private double _ratio = 0;
    private double _accuracy;
    private boolean _diffLockedZ2Z3 = false;
    private boolean _diffLockedZ4Z5 = false;
    private boolean _diffGearingZ1Z2 = false;
    private boolean _diffGearingZ3Z4 = false;
    private boolean _diffGearingZ5Z6 = false;
    private boolean _isOneSet = false;
    private int _gearsCount = 2;

    private int _calculatedRatios = 0;

    public ChangeGearsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
        mRepository = new ChGearsRepository(context);
    }

    public void setAccuracy(double accuracy)
    {
        _accuracy = accuracy;
    }

    public void setDiffLockedZ2Z3(boolean equal)
    {
        _diffLockedZ2Z3 = equal;
    }

    public void setDiffLockedZ4Z5(boolean equal)
    {
        _diffLockedZ4Z5 = equal;
    }

    public void setDiffGearingZ1Z2(boolean equal)
    {
        _diffGearingZ1Z2 = equal;
    }

    public void setDiffGearingZ3Z4(boolean equal)
    {
        _diffGearingZ3Z4 = equal;
    }

    public void setDiffGearingZ5Z6(boolean equal)
    {
        _diffGearingZ5Z6 = equal;
    }

    @Override
    protected void calculate()
    {
        final Data inputData = getInputData();
        long id = inputData.getLong(G.ChangeGearsId, 0);
        mEntity = mRepository.getChangeGears(id);
        if (mEntity == null)
            return;

        _accuracy = mEntity.accuracy;
        _diffLockedZ2Z3 = mEntity.diffLocked23;
        _diffLockedZ4Z5 = mEntity.diffLocked45;
        _diffGearingZ1Z2 = mEntity.diffGearing12;
        _diffGearingZ3Z4 = mEntity.diffGearing34;
        _diffGearingZ5Z6 = mEntity.diffGearing56;
        _ratio = mEntity.ratio;
        _isOneSet = mEntity.oneSet;

        try
        {
            if (_isOneSet)
            {
                _gearsCount = mEntity.count;
                int[] gs0 = Numbers.getIntNumbers(mEntity.set0);
                calculateByOneSet(_gearsCount, gs0);
                return;
            }

            int[] gs1 = Numbers.getIntNumbers(mEntity.set1);
            int[] gs2 = Numbers.getIntNumbers(mEntity.set2);
            int[] gs3 = Numbers.getIntNumbers(mEntity.set3);
            int[] gs4 = Numbers.getIntNumbers(mEntity.set4);
            int[] gs5 = Numbers.getIntNumbers(mEntity.set5);
            int[] gs6 = Numbers.getIntNumbers(mEntity.set6);

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
            if (_resultListener != null)
                _resultListener.onCompleted(_calculatedRatios);
        }
    }

    private void calculateBy(int[] gs1, int[] gs2)
    {
        // calculate by z1, z2
        int totalResults = gs1.length * gs2.length;
        resetProgress(totalResults);
        for (int z1: gs1)
        {
            publishProgress();
            for (int z2: gs2)
            {
                publishProgress();
                if (_diffGearingZ1Z2 && z1 == z2)
                    continue;

                calculateRatio(z1, z2);
            }
        }
    }

    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4)
    {
        // calculate by z1, z2, z3, z4
        int totalResults = gs1.length * gs2.length * gs3.length * gs4.length;
        resetProgress(totalResults);
        for (int z1: gs1)
        {
            publishProgress();
            for (int z2: gs2)
            {
                publishProgress();
                if (_diffGearingZ1Z2 && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    publishProgress();
                    if (_diffLockedZ2Z3 && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        publishProgress();
                        if (_diffGearingZ3Z4 && z3 == z4)
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
        resetProgress(totalResults);
        for (int z1: gs1)
        {
            publishProgress();
            for (int z2: gs2)
            {
                publishProgress();
                if (_diffGearingZ1Z2 && z1 == z2)
                    continue;

                for (int z3: gs3)
                {
                    publishProgress();
                    if (_diffLockedZ2Z3 && z2 == z3)
                        continue;

                    for (int z4: gs4)
                    {
                        publishProgress();
                        if (_diffGearingZ3Z4 && z3 == z4)
                            continue;

                        for (int z5: gs5)
                        {
                            publishProgress();
                            if (_diffLockedZ4Z5 && z4 == z5)
                                continue;

                            for (int z6: gs6)
                            {
                                publishProgress();
                                if (_diffGearingZ5Z6 && z5 == z6)
                                    continue;

                                calculateRatio(z1, z2, z3, z4, z5, z6);
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateByOneSet(int count, int[] set)
    {
        //int count = _gearsCount;
        if (count == 3) count = 2;
        if (count == 5) count = 4;

        //int[] set = _gearSets.getZ0();
        List<List<Integer>> combinations = Numbers.combinations(set.length, count);
        List<List<Integer>> permutations = Numbers.permutations(count);
        int totalResults = combinations.size() * permutations.size();
        resetProgress(totalResults);
        for (List<Integer> comb: combinations)
        {
            publishProgress();
            for (List<Integer> perm: permutations)
            {
                publishProgress();
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
        return Math.abs(ratio - _ratio) <= _accuracy;
    }
}
