package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.apsolete.machinery.calculation.CalculationWorker;
import com.apsolete.machinery.calculation.gearing.changegears.db.ChGearsEntity;
import com.apsolete.machinery.calculation.gearing.changegears.db.ChGearsRepository;
import com.apsolete.machinery.calculation.gearing.changegears.db.ChGearsResult;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.Numbers;

import java.util.List;

public class ChangeGearsWorker extends CalculationWorker
{
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
        }
    }

    private void calculateBy(int[] gs1, int[] gs2)
    {
        // calculate by z1, z2
        int totalResults = gs1.length * gs2.length;
        resetProgress(totalResults);
        for (int a: gs1)
        {
            publishProgress();
            for (int b: gs2)
            {
                publishProgress();
                if (_diffGearingZ1Z2 && a == b)
                    continue;

                calculateRatio(a, b);
            }
        }
    }

    private void calculateBy(int[] gs1, int[] gs2, int[] gs3, int[] gs4)
    {
        // calculate by z1, z2, z3, z4
        int totalResults = gs1.length * gs2.length * gs3.length * gs4.length;
        resetProgress(totalResults);
        for (int a: gs1)
        {
            publishProgress();
            for (int b: gs2)
            {
                publishProgress();
                if (_diffGearingZ1Z2 && a == b)
                    continue;

                for (int c: gs3)
                {
                    publishProgress();
                    if (_diffLockedZ2Z3 && b == c)
                        continue;

                    for (int d: gs4)
                    {
                        publishProgress();
                        if (_diffGearingZ3Z4 && c == d)
                            continue;

                        calculateRatio(a, b, c, d);
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
        for (int a: gs1)
        {
            publishProgress();
            for (int b: gs2)
            {
                publishProgress();
                if (_diffGearingZ1Z2 && a == b)
                    continue;

                for (int c: gs3)
                {
                    publishProgress();
                    if (_diffLockedZ2Z3 && b == c)
                        continue;

                    for (int d: gs4)
                    {
                        publishProgress();
                        if (_diffGearingZ3Z4 && c == d)
                            continue;

                        for (int e: gs5)
                        {
                            publishProgress();
                            if (_diffLockedZ4Z5 && d == e)
                                continue;

                            for (int f: gs6)
                            {
                                publishProgress();
                                if (_diffGearingZ5Z6 && e == f)
                                    continue;

                                calculateRatio(a, b, c, d, e, f);
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateByOneSet(int count, int[] set)
    {
        if (count == 3) count = 2;
        if (count == 5) count = 4;

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
                {
                    int a = set[comb.get(perm.get(0))];
                    int b = set[comb.get(perm.get(1))];
                    calculateRatio(a, b);
                }
                else if (count == 4)
                {
                    int a = set[comb.get(perm.get(0))];
                    int b = set[comb.get(perm.get(1))];
                    int c = set[comb.get(perm.get(2))];
                    int d = set[comb.get(perm.get(3))];
                    calculateRatio(a, b, c, d);
                }
                else if (count == 6)
                {
                    int a = set[comb.get(perm.get(0))];
                    int b = set[comb.get(perm.get(1))];
                    int c = set[comb.get(perm.get(2))];
                    int d = set[comb.get(perm.get(3))];
                    int e = set[comb.get(perm.get(4))];
                    int f = set[comb.get(perm.get(5))];
                    calculateRatio(a, b, c, d, e, f);
                }
            }
        }
    }

    private boolean calculateRatio(int a, int b)
    {
        double ratio = (double)(a) / (double)(b);
        if (checkRatio(ratio))
        {
            _calculatedRatios++;
            publishResult(_calculatedRatios, ratio, a, b);
            return true;
        }
        return false;
    }

    private boolean calculateRatio(int a, int b, int c, int d)
    {
        if (!checkCoupling(15, a, b, c, d))
            return false;

        double ratio = (double)(a * c) / (double)(b * d);
        if (checkRatio(ratio))
        {
            _calculatedRatios++;
            publishResult(_calculatedRatios, ratio, a, b, c, d);
            return true;
        }
        return false;
    }

    private boolean calculateRatio(int a, int b, int c, int d, int e, int f)
    {
        if (!checkCoupling(15, a, b, c, d, e, f))
            return false;

        double ratio = (double)(a * c * e) / (double)(b * d * f);
        if (checkRatio(ratio))
        {
            _calculatedRatios++;
            publishResult(_calculatedRatios, ratio, a, b, c, d, e, f);
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

    private void publishResult(int number, double ratio, int...z)
    {
        ChGearsResult result = new ChGearsResult();
        result.chg_id = mEntity.id;
        result.number = number;
        result.ratio = ratio;
        if (mEntity.mode == G.CHG_THREAD_BY_GEARS)
            result.threadPitch = ratio * mEntity.leadScrewPitch;
        else
            result.threadPitch = 0.0;
        result.z1 = z[0];
        result.z2 = z[1];
        result.z3 = z.length > 2 ? z[2] : 0;
        result.z4 = z.length > 3 ? z[3] : 0;
        result.z5 = z.length > 4 ? z[4] : 0;
        result.z6 = z.length > 5 ? z[5] : 0;
        mRepository.insert(result);
    }

    private boolean checkCoupling(int dm, int a, int b, int c, int d)
    {
        return ((a + b) >= (c + dm)) && ((c + d) >= (b + dm));
    }

    private boolean checkCoupling(int dm, int a, int b, int c, int d, int e, int f)
    {
        return ((a + b) >= (c + dm)) && ((c + d) >= (b + dm)) && ((c + d) >= (e + dm)) && ((e + f) >= (d + dm));
    }

}
