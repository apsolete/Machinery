package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.calculation.CalculationViewModel;
import com.apsolete.machinery.common.CustomViewModel;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.ArrayUtils;
import com.apsolete.machinery.utils.Fraction;
import com.apsolete.machinery.utils.Numbers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ChangeGearsViewModel extends CalculationViewModel
{
    private int mOneSetGearsCount = 2;
    private boolean mStarted = false;

    /*settings*/
    private int _ratioPrecision = 2;
    private NumberFormat mRatioFormat;
    private double _calculatedRatio = 0.0;

    private MutableLiveData<Boolean> mOneSet = new MutableLiveData<Boolean>()
    {
        @Override
        public void setValue(Boolean value)
        {
            super.setValue(value);
            switchOneSet(value);
        }
    };

    private GearSetsViewModel mGearSets = new GearSetsViewModel();
    private MutableLiveData<Boolean> mDiffLockedZ2Z3 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffLockedZ4Z5 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffGearingZ1Z2 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffGearingZ3Z4 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffGearingZ5Z6 = new MutableLiveData<>();

    private MutableLiveData<Integer> mCalculationMode = new MutableLiveData<Integer>()
    {
        @Override
        public void setValue(Integer value)
        {
            super.setValue(value);
            switchCalculationMode(value);
        }
    };

    private MutableLiveData<Double> mRatio = new MutableLiveData<>();
    private MutableLiveData<Integer> mRatioNumerator = new MutableLiveData<Integer>()
    {
        @Override
        public void setValue(Integer value)
        {
            super.setValue(value);
            recalculateRatio();
        }
    };
    private MutableLiveData<Integer> mRatioDenominator = new MutableLiveData<Integer>()
    {
        @Override
        public void setValue(Integer value)
        {
            super.setValue(value);
            recalculateRatio();
        }
    };
    private MutableLiveData<Boolean> mRatioAsFraction = new MutableLiveData<Boolean>()
    {
        @Override
        public void setValue(Boolean value)
        {
            super.setValue(value);
            recalculateRatio();
        }
    };
    private MutableLiveData<Boolean> mRatioEnabled = new MutableLiveData<>();
    private MutableLiveData<String> mRatioCalculated = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRatioCalculatedEnabled = new MutableLiveData<>();

    private MutableLiveData<ThreadPitchUnit> mThreadPitchUnit = new MutableLiveData<>();
    private MutableLiveData<ThreadPitchUnit> mLeadscrewPitchUnit = new MutableLiveData<>();
    private MutableLiveData<Double> mLeadscrewPitch = new MutableLiveData<>();
    private MutableLiveData<Double> mThreadPitch = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLeadscrewPitchEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> mThreadPitchEnabled = new MutableLiveData<>();

    private MutableLiveData<Integer> mFirstResultNumber = new MutableLiveData<Integer>();
    private LiveData<String> mFirstResultNumberStr = Transformations.map(mFirstResultNumber, Object::toString);
    private MutableLiveData<Integer> mLastResultNumber = new MutableLiveData<Integer>();
    private LiveData<String> mLastResultNumberStr = Transformations.map(mLastResultNumber, Object::toString);
    private MutableLiveData<Integer> mProgress = new MutableLiveData<>();
    private MutableLiveData<String> mMessage = new MutableLiveData<>();
    private ArrayList<Contract.Result> mResults = new ArrayList<>();
    private ChangeGears mCalculator;

    /*settings*/
    private int mRatioPrecision = 2;

    private OnResultListener<Result> _resultListener = new OnResultListener<Result>()
    {
        @Override
        public void onResult(Result result)
        {
            if (mCalculationMode.getValue() == G.THREAD_BY_GEARS)
                result.setLeadscrewPitch(mLeadscrewPitch.getValue());
            result.setFormat(mRatioFormat);
            mResults.add(result);
        }

        @Override
        public void onProgress(int percent)
        {
            mProgress.postValue(percent);
        }

        @Override
        public void onCompleted(int count)
        {
            mProgress.postValue(0);
            int shown = getNextResults();
            mMessage.postValue("Calculated " + count + " ratios. Shown " + shown + " results.");
        }
    };

    public ChangeGearsViewModel()
    {
    }

    @Override
    public void start()
    {
        mStarted = false;

        mDiffLockedZ2Z3.setValue(true);
        mDiffLockedZ4Z5.setValue(true);
        mDiffGearingZ1Z2.setValue(true);
        mDiffGearingZ3Z4.setValue(true);
        mDiffGearingZ5Z6.setValue(true);

        mRatio.setValue(1.25);
        mRatioNumerator.setValue(34);
        mRatioDenominator.setValue(56);
        //mRatioEnabled.setValue(true);

        mThreadPitchUnit.setValue(ThreadPitchUnit.mm);
        mThreadPitch.setValue(0.75);
        mLeadscrewPitchUnit.setValue(ThreadPitchUnit.mm);
        mLeadscrewPitch.setValue(4.0);

        mFirstResultNumber.setValue(1);
        mLastResultNumber.setValue(1);

        mGearSets.get(G.Z0).setGears("20-24");
        mGearSets.get(G.Z1).setGears("30-34").setSwitched(true).setEnabled(true).setEditable(true);
        mGearSets.get(G.Z2).setGears("40-44").setSwitched(true).setEnabled(true).setEditable(true);
        mGearSets.get(G.Z3).setGears("50-54");
        mGearSets.get(G.Z4).setGears("");
        mGearSets.get(G.Z5).setGears("");
        mGearSets.get(G.Z6).setGears("");

        mOneSet.setValue(true);
        mCalculationMode.setValue(1);
        mRatioAsFraction.setValue(true);
        setRatioPrecision(4);

        mCalculator = new ChangeGears();
        mCalculator.setResultListener(_resultListener);
        mStarted = true;
    }

    public GearSetsViewModel.GSet gearSet(int set)
    {
        return mGearSets.get(set);
    }

    public void setGearSet(int set, String valuesStr)
    {
        Integer[] values = Numbers.getNumbers(valuesStr);
        setGearSet(set, values);
    }

    public void setGearSet(int set, Integer[] values)
    {
        String gearsStr = Numbers.getString(values);
        mGearSets.get(set).setGears(gearsStr);
        if (set > G.Z1 && set < G.Z6)
        {
            mGearSets.get(set+1).setEnabled(values.length > 0);
        }
    }

    public void setGearSetSwitched(int set, boolean switched)
    {
        if (set < G.Z1 || set > G.Z6)
            return;
        mGearSets.get(set).setSwitched(switched);
        if (switched)
        {
            mOneSetGearsCount = (set % 2) == 0 ? set : set - 1;
            if (set < G.Z6)
                setGearSetEnabled(set + 1, true);
        }
        else
        {
            int s = set - 1;
            mOneSetGearsCount = (s % 2) == 0 ? s : s - 1;
            for (set++; set <= G.Z6; set++)
            {
                GearSetsViewModel.GSet gk = mGearSets.get(set);
                gk.setSwitched(false);
                gk.setEnabled(false);
            }
        }
    }

    public void setGearSetEditable(int set, boolean editable)
    {
        mGearSets.get(set).setEditable(editable);
    }

    public void setGearSetEnabled(int set, boolean enabled)
    {
        mGearSets.get(set).setEnabled(enabled);
    }

    private void switchOneSet(boolean value)
    {
        if (!mStarted)
            return;
        if (value)
        {
            mGearSets.get(G.Z0).setEditable(true).setEnabled(true).setSwitched(true);
            mGearSets.get(G.Z1).setEditable(false).setEnabled(false).setSwitched(true);
            mGearSets.get(G.Z2).setEditable(false).setEnabled(false).setSwitched(true);
            mGearSets.get(G.Z3).setEditable(false).setEnabled(true);
            mGearSets.get(G.Z4).setEditable(false);
            mGearSets.get(G.Z5).setEditable(false);
            mGearSets.get(G.Z6).setEditable(false);

            for (int set = G.Z4; set <= G.Z6; set++)
            {
                GearSetsViewModel.GSet gk_n = mGearSets.get(set);
                GearSetsViewModel.GSet gk_p = mGearSets.get(set-1);
                gk_n.setEnabled(gk_p.isSwitched().getValue());
            }
        }
        else
        {
            mGearSets.get(G.Z0).setEditable(false).setEnabled(false).setSwitched(false);
            mGearSets.get(G.Z1).setEditable(true).setEnabled(false).setSwitched(true);
            mGearSets.get(G.Z2).setEditable(true).setEnabled(false).setSwitched(true);
            mGearSets.get(G.Z3).setEditable(true);
            mGearSets.get(G.Z4).setEditable(true);
            mGearSets.get(G.Z5).setEditable(true);
            mGearSets.get(G.Z6).setEditable(true);

            boolean prevNotEmpty = !mGearSets.get(G.Z2).isEmpty();
            for (int set = G.Z3; set <= G.Z6; set++)
            {
                boolean notEmpty = !mGearSets.get(set).isEmpty();
                mGearSets.get(set).setEnabled(prevNotEmpty).setSwitched(notEmpty);
                prevNotEmpty = notEmpty;
            }
        }
    }
    private void switchCalculationMode(int value)
    {
        if (!mStarted)
            return;
        switch (value)
        {
            case G.RATIOS_BY_GEARS:
                mLeadscrewPitchEnabled.setValue(false);
                mThreadPitchEnabled.setValue(false);
                mRatioEnabled.setValue(false);
                mRatioCalculatedEnabled.setValue(false);
                break;
            case G.THREAD_BY_GEARS:
                mLeadscrewPitchEnabled.setValue(true);
                mThreadPitchEnabled.setValue(false);
                mRatioEnabled.setValue(false);
                mRatioCalculatedEnabled.setValue(false);
                break;
            case G.GEARS_BY_RATIO:
                mLeadscrewPitchEnabled.setValue(false);
                mThreadPitchEnabled.setValue(false);
                mRatioEnabled.setValue(true);
                mRatioCalculatedEnabled.setValue(true);
                recalculateRatio();
                break;
            case G.GEARS_BY_THREAD:
                mLeadscrewPitchEnabled.setValue(true);
                mThreadPitchEnabled.setValue(true);
                mRatioEnabled.setValue(false);
                mRatioCalculatedEnabled.setValue(true);
                recalculateRatio();
                break;
        }
    }

    public MutableLiveData<Boolean> getOneSet()
    {
        return mOneSet;
    }

    public MutableLiveData<Integer> getCalculationMode()
    {
        return mCalculationMode;
    }

    public MutableLiveData<ThreadPitchUnit> getThreadPitchUnit()
    {
        return mThreadPitchUnit;
    }

    public MutableLiveData<ThreadPitchUnit> getLeadscrewPitchUnit()
    {
        return mLeadscrewPitchUnit;
    }

    public MutableLiveData<Double> getLeadscrewPitch()
    {
        return mLeadscrewPitch;
    }

    public MutableLiveData<Double> getThreadPitch()
    {
        return mThreadPitch;
    }

    public MutableLiveData<Boolean> getLeadscrewPitchEnabled()
    {
        return mLeadscrewPitchEnabled;
    }

    public MutableLiveData<Boolean> getThreadPitchEnabled()
    {
        return mThreadPitchEnabled;
    }

    public MutableLiveData<Double> getRatio()
    {
        return mRatio;
    }

    public MutableLiveData<Integer> getRatioNumerator()
    {
        return mRatioNumerator;
    }

    public MutableLiveData<Integer> getRatioDenominator()
    {
        return mRatioDenominator;
    }

    public MutableLiveData<Boolean> getRatioEnabled()
    {
        return mRatioEnabled;
    }

    public MutableLiveData<Boolean> getRatioAsFraction()
    {
        return mRatioAsFraction;
    }

    public MutableLiveData<String> getRatioCalculated()
    {
        return mRatioCalculated;
    }

    public MutableLiveData<Boolean> getRatioCalculatedEnabled()
    {
        return mRatioCalculatedEnabled;
    }

    public LiveData<String> getFirstResultNumberStr()
    {
        return mFirstResultNumberStr;
    }

    public LiveData<String> getLastResultNumberStr()
    {
        return mLastResultNumberStr;
    }

    public int getNextResults()
    {
        int fi = mLastResultNumber.getValue() > 1 ? mLastResultNumber.getValue() + 1 : 1;
        if (fi > mResults.size())
            return 0;
        int li = fi + 99;
        if (li > mResults.size())
            li = mResults.size();
        mFirstResultNumber.postValue(fi);
        mLastResultNumber.postValue(li);
        List<Contract.Result> next = mResults.subList(fi-1, li);
        //_view.showResults(next);
        return next.size();
    }

    public int getPrevResults()
    {
        int fi = mFirstResultNumber.getValue() - 100;
        if (fi < 0)
            return 0;
        int ti = fi + 99;
        if (ti > mResults.size())
            ti = mResults.size();
        mFirstResultNumber.postValue(fi);
        mLastResultNumber.postValue(ti);
        List<Contract.Result> prev = mResults.subList(fi-1, ti);
        //_view.showResults(prev);
        return prev.size();
    }

    private void recalculateRatio()
    {
        if (!mStarted)
            return;

        String ratioInfo = "R = <Undefined>";

        double _calculatedRatio = 0.0;
        int _calculationMode = mCalculationMode.getValue();
        double _threadPitch = mThreadPitch.getValue();
        double _leadscrewPitch = mLeadscrewPitch.getValue();
        ThreadPitchUnit _threadPitchUnit = mThreadPitchUnit.getValue();
        ThreadPitchUnit _leadscrewPitchUnit = mLeadscrewPitchUnit.getValue();
        boolean _ratioAsFraction = mRatioAsFraction.getValue();
        double _ratioNumerator = mRatioNumerator.getValue();
        double _ratioDenominator = mRatioDenominator.getValue();

        if (_calculationMode == G.GEARS_BY_THREAD)
        {
            if (_threadPitch == 0.0)
            {
                _calculatedRatio = 0.0;
            }
            else if (_leadscrewPitch == 0.0)
            {
                _calculatedRatio = _threadPitchUnit.toMm(_threadPitch);
                ratioInfo = "R = " + mRatioFormat.format(_calculatedRatio) + " (" + _threadPitchUnit + ")";
            }
            else
            {
                Fraction tpf = _threadPitchUnit.toMmFraction(_threadPitch);
                Fraction spf = _leadscrewPitchUnit.toMmFraction(_leadscrewPitch);
                Fraction fract = tpf.divide(spf);
                _calculatedRatio = fract.toDouble();
                ratioInfo = "R = " + _threadPitch + " (" + _threadPitchUnit + ") / " +
                        _leadscrewPitch + " (" + _leadscrewPitchUnit + ") = " + fract.toString() +
                        " = " + mRatioFormat.format(_calculatedRatio);
            }
        }
        else if (_calculationMode == G.GEARS_BY_RATIO)
        {
            if (_ratioAsFraction)
            {
                if (_ratioNumerator == 0.0)
                {
                    _calculatedRatio = 0.0;
                }
                else if (_ratioDenominator == 0.0)
                {
                    _calculatedRatio = _ratioNumerator;
                    ratioInfo = "R = " + mRatioFormat.format(_calculatedRatio);
                }
                else
                {
                    Fraction fract = new Fraction(_ratioNumerator, _ratioDenominator);
                    _calculatedRatio = fract.toDouble();
                    ratioInfo = "R = " + _ratioNumerator + " / " + _ratioDenominator + " = " +
                            fract.toString() + " = " + mRatioFormat.format(_calculatedRatio);
                }
            }
            else
            {
                _calculatedRatio = mRatio.getValue();
                ratioInfo = "R = " + mRatioFormat.format(_calculatedRatio);
            }
        }

        mRatioCalculated.setValue(ratioInfo);
    }

    public void setRatioPrecision(int precision)
    {
        _ratioPrecision = precision;
        StringBuilder pattern = new StringBuilder("#0.0");
        for (int i = 0; i < precision-1; i++)
            pattern.append("#");
        mRatioFormat = CalculationFragment.getNumberFormat(pattern.toString());
        recalculateRatio();
    }

    @Override
    public void save()
    {

    }

    @Override
    public void clear()
    {

    }

    @Override
    public void calculate()
    {
        clear();

        mCalculator.setAccuracy(Math.pow(10, -_ratioPrecision));
        mCalculator.setDiffLockedZ2Z3(mDiffLockedZ2Z3.getValue());
        mCalculator.setDiffLockedZ4Z5(mDiffLockedZ4Z5.getValue());
        mCalculator.setDiffGearingZ1Z2(mDiffGearingZ1Z2.getValue());
        mCalculator.setDiffGearingZ3Z4(mDiffGearingZ3Z4.getValue());
        mCalculator.setDiffGearingZ5Z6(mDiffGearingZ5Z6.getValue());

        double r = mCalculationMode.getValue() == G.GEARS_BY_RATIO || mCalculationMode.getValue() == G.GEARS_BY_THREAD
                ? _calculatedRatio : 0.0;
        mCalculator.setRatio(r);

        if (mOneSet.getValue())
        {
            int wheelsCount = mGearSets.getWheelsCount();
            int[] set = ArrayUtils.toArrayInt(mGearSets.get(G.Z0).getGears());
            mCalculator.setGearKit(wheelsCount, set);
        }
        else
        {
            int[] zs1 = ArrayUtils.toArrayInt(mGearSets.get(G.Z1).getGears());
            int[] zs2 = ArrayUtils.toArrayInt(mGearSets.get(G.Z2).getGears());
            int[] zs3 = ArrayUtils.toArrayInt(mGearSets.get(G.Z3).getGears());
            int[] zs4 = ArrayUtils.toArrayInt(mGearSets.get(G.Z4).getGears());
            int[] zs5 = ArrayUtils.toArrayInt(mGearSets.get(G.Z5).getGears());
            int[] zs6 = ArrayUtils.toArrayInt(mGearSets.get(G.Z6).getGears());
            int total = zs1.length > 0 ? zs1.length : 1;
            total *= zs2.length > 0 ? zs2.length : 1;
            total *= zs3.length > 0 ? zs3.length : 1;
            total *= zs4.length > 0 ? zs4.length : 1;
            total *= zs5.length > 0 ? zs5.length : 1;
            total *= zs6.length > 0 ? zs6.length : 1;
            if (total > 20000)
            {
                mMessage.setValue("Too much gears!");
                return;
            }
            mCalculator.setGearKit(zs1, zs2, zs3, zs4, zs5, zs6);
        }
        mCalculator.calculate();
    }

    @Override
    public boolean close()
    {
        return false;
    }
}
