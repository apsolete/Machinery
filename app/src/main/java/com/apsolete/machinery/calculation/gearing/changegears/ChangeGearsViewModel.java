package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.lifecycle.MutableLiveData;

import com.apsolete.machinery.common.CustomViewModel;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.Numbers;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ChangeGearsViewModel extends CustomViewModel
{
    private int mOneSetGearsCount = 2;

    private MutableLiveData<Boolean> mOneSet = new MutableLiveData<Boolean>()
    {
        @Override
        public void setValue(Boolean value)
        {
            super.setValue(value);
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

//                for (int set = G.Z2; set <= G.Z6; set++)
//                {
//                    Integer[] values = mGearSets.get(set - 1).getGears().getValue();
//                    mGearSets.get(set).setEnabled((values != null && values.length > 0));
//                }
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
            switch (value)
            {
                case G.RATIOS_BY_GEARS:
                    //_view.showRatio(false);
                    mLeadscrewPitchEnabled.setValue(false);
                    mThreadPitchEnabled.setValue(false);
                    //_view.showFormattedRatio(false);
                    break;
                case G.THREAD_BY_GEARS:
                    //_view.showRatio(false);
                    mLeadscrewPitchEnabled.setValue(true);
                    mThreadPitchEnabled.setValue(false);
                    //_view.showFormattedRatio(false);
                    break;
                case G.GEARS_BY_RATIO:
                    //_view.showRatio(true);
                    //_view.showRatioAsFration(_ratioAsFraction);
                    mLeadscrewPitchEnabled.setValue(false);
                    mThreadPitchEnabled.setValue(false);
                    //_view.showFormattedRatio(true);
                    //recalculateRatio();
                    break;
                case G.GEARS_BY_THREAD:
                    //_view.showRatio(false);
                    mLeadscrewPitchEnabled.setValue(true);
                    mThreadPitchEnabled.setValue(true);
                    //_view.showFormattedRatio(true);
                    //recalculateRatio();
                    break;
            }
        }
    };

    private MutableLiveData<Double> mRatio = new MutableLiveData<>();
    private MutableLiveData<Double> mRatioNumerator = new MutableLiveData<>();
    private MutableLiveData<Double> mRatioDenominator = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRatioAsFraction = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRatioEnabled = new MutableLiveData<>();

    private MutableLiveData<ThreadPitchUnit> mThreadPitchUnit = new MutableLiveData<>();
    private MutableLiveData<ThreadPitchUnit> mLeadscrewPitchUnit = new MutableLiveData<>();
    private MutableLiveData<Double> mLeadscrewPitch = new MutableLiveData<>();
    private MutableLiveData<Double> mThreadPitch = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLeadscrewPitchEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> mThreadPitchEnabled = new MutableLiveData<>();
    private NumberFormat mRatioFormat;
    private MutableLiveData<Double> mCalculatedRatio = new MutableLiveData<>();
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
        mDiffLockedZ2Z3.setValue(true);
        mDiffLockedZ4Z5.setValue(true);
        mDiffGearingZ1Z2.setValue(true);
        mDiffGearingZ3Z4.setValue(true);
        mDiffGearingZ5Z6.setValue(true);

        mRatio.setValue(1.25);
        mRatioNumerator.setValue(34.0);
        mRatioDenominator.setValue(56.0);
        mThreadPitchUnit.setValue(ThreadPitchUnit.mm);
        mLeadscrewPitchUnit.setValue(ThreadPitchUnit.mm);
        mLeadscrewPitch.setValue(4.0);
        mThreadPitch.setValue(0.75);

        mCalculatedRatio.setValue(0.0);
        mFirstResultNumber.setValue(1);
        mLastResultNumber.setValue(1);

        mGearSets.get(G.Z0).setGears(new Integer[]{20, 21, 22, 23, 24});
        mGearSets.get(G.Z1).setGears(new Integer[]{30, 31, 32, 33, 34}).setSwitched(true).setEnabled(true).setEditable(true);
        mGearSets.get(G.Z2).setGears(new Integer[]{40, 41, 42, 43, 44}).setSwitched(true).setEnabled(true).setEditable(true);
        mGearSets.get(G.Z3).setGears(new Integer[]{50, 51, 52, 53, 54});
        mGearSets.get(G.Z4).setGears(new Integer[]{});
        mGearSets.get(G.Z5).setGears(new Integer[]{});
        mGearSets.get(G.Z6).setGears(new Integer[]{});

        mOneSet.setValue(true);
        mCalculationMode.setValue(1);
        mRatioAsFraction.setValue(true);

        mCalculator = new ChangeGears();
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
        mGearSets.get(set).setGears(values);
        if (set > G.Z1 && set < G.Z6)
        {
            mGearSets.get(set+1).setEnabled(values.length > 0);
        }
    }

    public void setGearSetChecked(int set, boolean checked)
    {
        if (set < G.Z1 || set > G.Z6)
            return;
        mGearSets.get(set).setSwitched(checked);
        if (checked)
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

    public MutableLiveData<Boolean> getRatioEnabled()
    {
        return mRatioEnabled;
    }
}
