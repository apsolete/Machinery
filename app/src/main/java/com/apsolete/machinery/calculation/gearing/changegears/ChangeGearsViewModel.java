package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.lifecycle.MutableLiveData;

import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.CustomViewModel;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.Fraction;
import com.apsolete.machinery.utils.Numbers;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ChangeGearsViewModel extends CustomViewModel
{
    private int mOneSetGearsCount = 2;
    private boolean mStarted = false;

    /*settings*/
    private int _ratioPrecision = 2;
    private NumberFormat _ratioFormat;

    private MutableLiveData<Boolean> mOneSet = new MutableLiveData<Boolean>()
    {
        @Override
        public void setValue(Boolean value)
        {
            super.setValue(value);
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
    };

    private MutableLiveData<Double> mRatio = new MutableLiveData<>();
    private MutableLiveData<Integer> mRatioNumerator = new MutableLiveData<Integer>()
    {
        @Override
        public void setValue(Integer value)
        {
            super.setValue(value);
            if (!mStarted)
                return;
            recalculateRatio();
        }
    };
    private MutableLiveData<Integer> mRatioDenominator = new MutableLiveData<Integer>()
    {
        @Override
        public void setValue(Integer value)
        {
            super.setValue(value);
            if (!mStarted)
                return;
            recalculateRatio();
        }
    };
    private MutableLiveData<Boolean> mRatioAsFraction = new MutableLiveData<Boolean>()
    {
        @Override
        public void setValue(Boolean value)
        {
            super.setValue(value);
            if (!mStarted)
                return;
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
    private NumberFormat mRatioFormat;
    //private MutableLiveData<Double> mCalculatedRatio = new MutableLiveData<>();
    private MutableLiveData<Integer> mFirstResultNumber = new MutableLiveData<>();
    private MutableLiveData<Integer> mLastResultNumber = new MutableLiveData<>();
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
            //_view.showProgress(percent);
        }

        @Override
        public void onCompleted(int count)
        {
            //_view.showProgress(0);
            //int shown = getNextResults();
            //_view.showMessage("Calculated " + count + " ratios. Shown " + shown + " results.");
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



    private void recalculateRatio()
    {
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
                ratioInfo = "R = " + _ratioFormat.format(_calculatedRatio) + " (" + _threadPitchUnit + ")";
            }
            else
            {
                Fraction tpf = _threadPitchUnit.toMmFraction(_threadPitch);
                Fraction spf = _leadscrewPitchUnit.toMmFraction(_leadscrewPitch);
                Fraction fract = tpf.divide(spf);
                _calculatedRatio = fract.toDouble();
                ratioInfo = "R = " + _threadPitch + " (" + _threadPitchUnit + ") / " +
                        _leadscrewPitch + " (" + _leadscrewPitchUnit + ") = " + fract.toString() +
                        " = " + _ratioFormat.format(_calculatedRatio);
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
                    ratioInfo = "R = " + _ratioFormat.format(_calculatedRatio);
                }
                else
                {
                    Fraction fract = new Fraction(_ratioNumerator, _ratioDenominator);
                    _calculatedRatio = fract.toDouble();
                    ratioInfo = "R = " + _ratioNumerator + " / " + _ratioDenominator + " = " +
                            fract.toString() + " = " + _ratioFormat.format(_calculatedRatio);
                }
            }
            else
            {
                _calculatedRatio = mRatio.getValue();
                ratioInfo = "R = " + _ratioFormat.format(_calculatedRatio);
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
        _ratioFormat = CalculationFragment.getNumberFormat(pattern.toString());
        recalculateRatio();
    }

}
