package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.Observers;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.Numbers;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ChangeGearsViewModel extends ViewModel
{
    private MutableLiveData<Boolean> mOneSet = new MutableLiveData<>();
    private GearKitsViewModel mGearKits = new GearKitsViewModel();
    private MutableLiveData<Integer> mOneSetGearsCount = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffLockedZ2Z3 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffLockedZ4Z5 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffGearingZ1Z2 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffGearingZ3Z4 = new MutableLiveData<>();
    private MutableLiveData<Boolean> mDiffGearingZ5Z6 = new MutableLiveData<>();

    private MutableLiveData<Integer> mCalculationMode = new MutableLiveData<>();

    private MutableLiveData<Double> mRatio = new MutableLiveData<>();
    private MutableLiveData<Double> mRatioNumerator = new MutableLiveData<>();
    private MutableLiveData<Double> mRatioDenominator = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRatioAsFraction = new MutableLiveData<>();
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
        mOneSet.setValue(true);
        mOneSet.observeForever(new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean isOneSet)
            {
                if (isOneSet)
                {
                    mGearKits.get(G.Z0).setEditable(true).setEnabled(true);
                    mGearKits.get(G.Z1).setEditable(false).setEnabled(false);
                    mGearKits.get(G.Z2).setEditable(false).setEnabled(false);
                    mGearKits.get(G.Z3).setEditable(false).setEnabled(true);
                    mGearKits.get(G.Z4).setEditable(false);
                    mGearKits.get(G.Z5).setEditable(false);
                    mGearKits.get(G.Z6).setEditable(false);

                    for (int kit = G.Z4; kit <= G.Z6; kit++)
                    {
                        GearKitsViewModel.Kit gk_n = mGearKits.get(kit);
                        GearKitsViewModel.Kit gk_p = mGearKits.get(kit-1);
                        gk_n.setEnabled(gk_p.isChecked().getValue());
                    }
                }
                else
                {
                    mGearKits.get(G.Z0).setEditable(false).setEnabled(false);
                    mGearKits.get(G.Z1).setEditable(true).setEnabled(true);
                    mGearKits.get(G.Z2).setEditable(true);
                    mGearKits.get(G.Z3).setEditable(true);
                    mGearKits.get(G.Z4).setEditable(true);
                    mGearKits.get(G.Z5).setEditable(true);
                    mGearKits.get(G.Z6).setEditable(true);

                    for (int set = G.Z2; set <= G.Z6; set++)
                    {
                        Integer[] values = mGearKits.get(set - 1).getGears().getValue();
                        mGearKits.get(set).setEnabled((values != null && values.length > 0));
                    }
                }
            }
        });
        mOneSetGearsCount.setValue(2);
        mDiffLockedZ2Z3.setValue(true);
        mDiffLockedZ4Z5.setValue(true);
        mDiffGearingZ1Z2.setValue(true);
        mDiffGearingZ3Z4.setValue(true);
        mDiffGearingZ5Z6.setValue(true);

        mCalculationMode.setValue(1);
        mCalculationMode.observeForever(new Observer<Integer>()
        {
            @Override
            public void onChanged(Integer calcType)
            {
                switch (calcType)
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
        });

        mRatio.setValue(1.25);
        mRatioNumerator.setValue(34.0);
        mRatioDenominator.setValue(56.0);
        mRatioAsFraction.setValue(true);
        mThreadPitchUnit.setValue(ThreadPitchUnit.mm);
        mLeadscrewPitchUnit.setValue(ThreadPitchUnit.mm);
        mLeadscrewPitch.setValue(4.0);
        mThreadPitch.setValue(0.75);

        mCalculatedRatio.setValue(0.0);
        mFirstResultNumber.setValue(1);
        mLastResultNumber.setValue(1);

        mGearKits.get(G.Z0).setGears(new Integer[]{20, 21, 22, 23, 24});
        mGearKits.get(G.Z1).setGears(new Integer[]{30, 31, 32, 33, 34}).setChecked(true).setEnabled(true).setEditable(true);
        mGearKits.get(G.Z2).setGears(new Integer[]{40, 41, 42, 43, 44}).setChecked(true).setEnabled(true).setEditable(true);
        mGearKits.get(G.Z3).setGears(new Integer[]{50, 51, 52, 53, 54});
        mGearKits.get(G.Z4).setGears(new Integer[]{});
        mGearKits.get(G.Z5).setGears(new Integer[]{});
        mGearKits.get(G.Z6).setGears(new Integer[]{});

        //mGearKits.get(G.Z1).setChecked(true);
        //mGearKits.get(G.Z2).setChecked(true);

        mCalculator = new ChangeGears();
    }

    public GearKitsViewModel.Kit getGearKit(int kit)
    {
        return mGearKits.get(kit);
    }

    public void setGearKit(int kit, String valuesStr)
    {
        Integer[] values = Numbers.getNumbers(valuesStr);
        setGearKit(kit, values);
    }

    public void setGearKit(int kit, Integer[] values)
    {
        mGearKits.putGears(kit, values);
        if (kit > G.Z1 && kit < G.Z6)
        {
            mGearKits.get(kit+1).setEnabled(values.length > 0);
        }
    }

    public void setGearKitChecked(int kit, boolean checked)
    {
        if (kit < G.Z1 || kit > G.Z6)
            return;
        mGearKits.get(kit).setChecked(checked);
        if (checked)
        {
            mOneSetGearsCount.setValue((kit % 2) == 0 ? kit : kit - 1);
            if (kit < G.Z6)
                setGearKitEnabled(kit + 1, true);
        }
        else
        {
            int s = kit - 1;
            mOneSetGearsCount.setValue((s % 2) == 0 ? s : s - 1);
            for (kit++; kit <= G.Z6; kit++)
            {
                GearKitsViewModel.Kit gk = mGearKits.get(kit);
                gk.setChecked(false);
                gk.setEnabled(false);
            }
        }
    }

    public void setGearKitEditable(int kit, boolean editable)
    {
        mGearKits.get(kit).setEditable(editable);
    }

    public void setGearKitEnabled(int kit, boolean enabled)
    {
        mGearKits.get(kit).setEnabled(enabled);
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
}