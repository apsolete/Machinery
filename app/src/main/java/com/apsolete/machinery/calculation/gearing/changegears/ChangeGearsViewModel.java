package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.OnResultListener;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ChangeGearsViewModel extends ViewModel
{
    private MutableLiveData<Boolean> mOneSet = new MutableLiveData<>();
    private GearKits mGearKits = new GearKits();
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

        mGearKits.putZ0(new int[]{20, 21, 22, 23, 24});
        mGearKits.putZ1(new int[]{30, 31, 32, 33, 34});
        mGearKits.putZ2(new int[]{40, 41, 42, 43, 44});
        mGearKits.putZ3(new int[]{50, 51, 52, 53, 54});
        mGearKits.putZ4(new int[]{});
        mGearKits.putZ5(new int[]{});
        mGearKits.putZ6(new int[]{});

        mGearKits.setChecked(G.Z1, true);
        mGearKits.setChecked(G.Z2, true);

        mCalculator = new ChangeGears();
    }
}
