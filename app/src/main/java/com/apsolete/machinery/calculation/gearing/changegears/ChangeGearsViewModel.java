package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;
import com.apsolete.machinery.utils.Numbers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
        mOneSetGearsCount.setValue(2);
        mDiffLockedZ2Z3.setValue(true);
        mDiffLockedZ4Z5.setValue(true);
        mDiffGearingZ1Z2.setValue(true);
        mDiffGearingZ3Z4.setValue(true);
        mDiffGearingZ5Z6.setValue(true);

        //mCalculationMode.setValue(1);

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
        mGearKits.get(G.Z1).setGears(new Integer[]{30, 31, 32, 33, 34});
        mGearKits.get(G.Z2).setGears(new Integer[]{40, 41, 42, 43, 44});
        mGearKits.get(G.Z3).setGears(new Integer[]{50, 51, 52, 53, 54});
        mGearKits.get(G.Z4).setGears(new Integer[]{});
        mGearKits.get(G.Z5).setGears(new Integer[]{});
        mGearKits.get(G.Z6).setGears(new Integer[]{});

        mGearKits.get(G.Z1).setChecked(true);
        mGearKits.get(G.Z2).setChecked(true);

        mCalculator = new ChangeGears();
    }
    public GearKitsViewModel.Kit getGearKit(int kit)
    {
        return mGearKits.get(kit);
    }

    public void setGearKit(int kit, String valuesStr)
    {
        Integer[] values = Numbers.getNumbers(valuesStr);
        mGearKits.putGears(kit, values);
        if (kit > G.Z1 && kit < G.Z6)
        {
            mGearKits.get(kit+1).setEnabled(valuesStr != null && !valuesStr.isEmpty());
        }
    }

    public void setGearKitChecked(int kit, boolean checked)
    {
        if (kit < G.Z1 || kit > G.Z6)
            return;
        mGearKits.get(kit).setChecked(checked);
        if (checked)
        {
            //_oneSetGearsCount = (kit % 2) == 0 ? kit : kit - 1;
            if (kit < G.Z6)
                setGearKitEnabled(kit + 1, true);
        }
        else
        {
            int s = kit - 1;
            //_oneSetGearsCount = (s % 2) == 0 ? s : s - 1;
            for (kit++; kit <= G.Z6; kit++)
            {
                GearKitsViewModel.Kit gk = mGearKits.get(s);
                //_gearKits.setChecked(kit, false);
                gk.setChecked(false);
                gk.setEnabled(false);
                //setGearKitChecked(kit, false);
                //setGearKitEnabled(kit, false);
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

    public LiveData<Boolean> getOneSet()
    {
        return mOneSet;
    }
}
