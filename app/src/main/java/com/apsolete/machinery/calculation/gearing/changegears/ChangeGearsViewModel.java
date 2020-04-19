package com.apsolete.machinery.calculation.gearing.changegears;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.apsolete.machinery.calculation.Calculation;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.calculation.CalculationViewModel;
import com.apsolete.machinery.common.Event;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.utils.Fraction;
import com.apsolete.machinery.utils.Numbers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ChangeGearsViewModel extends CalculationViewModel
{
    private ChGearsRepository mRepository;
    private long mChangeGearsId = 1;
    private ChGearsEntity mEntity;

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
    private MutableLiveData<Long> mRatioNumerator = new MutableLiveData<Long>()
    {
        @Override
        public void setValue(Long value)
        {
            super.setValue(value);
            recalculateRatio();
        }
    };
    private MutableLiveData<Long> mRatioDenominator = new MutableLiveData<Long>()
    {
        @Override
        public void setValue(Long value)
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

    private MutableLiveData<Integer> mFirstResultNumber = new MutableLiveData<>();
    private LiveData<String> mFirstResultNumberStr = Transformations.map(mFirstResultNumber, Object::toString);
    private MutableLiveData<Integer> mLastResultNumber = new MutableLiveData<>();
    private LiveData<String> mLastResultNumberStr = Transformations.map(mLastResultNumber, Object::toString);
    //private MutableLiveData<Integer> mProgress = new MutableLiveData<>();
    //private ArrayList<ChangeGearsWorker.Result> mResults = new ArrayList<>();
    //private LiveArrayList<Contract.Result> mResultsToShow = new LiveArrayList<>();
    private MutableLiveData<List<ChGearsResult>> mResultsToShow = new MutableLiveData<>();
    private LiveData<List<ChGearsResultViewModel>> mResultsVmToShow = Transformations.map(mResultsToShow, results ->
    {
        List<ChGearsResultViewModel> list = new ArrayList<>();
        for (ChGearsResult result : results)
            list.add(new ChGearsResultViewModel(result, mRatioFormat));
        return list;
    });
    private MutableLiveData<List<ChGearsResult>> mResults = new MutableLiveData<>();
    //private ChangeGearsModel mCalculator;

    /*settings*/
    private int mRatioPrecision = 2;

    public ChangeGearsViewModel(@NonNull Application application)
    {
        super(application);
        mRepository = new ChGearsRepository(application);
    }

    @Override
    public void load()
    {
        mEntity = mRepository.getChangeGears(mChangeGearsId);
        if (mEntity == null)
        {
            mEntity = new ChGearsEntity();
            mEntity.id = mChangeGearsId;

            mEntity.mode = G.CHG_RATIOS_BY_GEARS;

            mEntity.ratio = 1.25;
            mEntity.ratioAsFraction = true;
            mEntity.threadPitch = 0.5;
            mEntity.threadPitchUnit = ThreadPitchUnit.toInt(ThreadPitchUnit.mm);
            mEntity.leadScrewPitch = 4.0;
            mEntity.leadScrewPitchUnit = ThreadPitchUnit.toInt(ThreadPitchUnit.mm);

            mEntity.accuracy = 0.0001;
            mEntity.diffLocked23 = true;
            mEntity.diffLocked45 = true;
            mEntity.diffGearing12 = true;
            mEntity.diffGearing34 = true;
            mEntity.diffGearing56 = true;
            mEntity.oneSet = true;
            mEntity.count = 4;
            mEntity.set0 = "21-24";
            mEntity.set1 = "31-34";
            mEntity.set2 = "41-44";
            mEntity.set3 = "51-54";
            mEntity.set4 = "61-64";
            mEntity.set5 = "";
            mEntity.set6 = "";
        }

        mCalculationMode.setValue(mEntity.mode);

        mRatio.setValue(mEntity.ratio);
        mRatioAsFraction.setValue(mEntity.ratioAsFraction);
        Fraction fr = Fraction.fromDouble(mEntity.ratio);
        mRatioNumerator.setValue(fr.numerator());
        mRatioDenominator.setValue(fr.denominator());
        mRatioEnabled.setValue(mEntity.mode == G.CHG_GEARS_BY_RATIO);
        setRatioPrecision(4);

        mThreadPitch.setValue(mEntity.threadPitch);
        mThreadPitchUnit.setValue(ThreadPitchUnit.fromInt(mEntity.threadPitchUnit));
        mLeadscrewPitch.setValue(mEntity.leadScrewPitch);
        mLeadscrewPitchUnit.setValue(ThreadPitchUnit.fromInt(mEntity.leadScrewPitchUnit));

        mDiffLockedZ2Z3.setValue(mEntity.diffLocked23);
        mDiffLockedZ4Z5.setValue(mEntity.diffLocked45);
        mDiffGearingZ1Z2.setValue(mEntity.diffGearing12);
        mDiffGearingZ3Z4.setValue(mEntity.diffGearing34);
        mDiffGearingZ5Z6.setValue(mEntity.diffGearing56);

        mGearSets.set0().setGears(mEntity.set0).setEnabled(mEntity.oneSet).setEditable(mEntity.oneSet).setSwitched(mEntity.oneSet);
        mGearSets.set1().setGears(mEntity.set1).setSwitched(true).setEnabled(false).setEditable(true);
        mGearSets.set2().setGears(mEntity.set2).setSwitched(true).setEnabled(false).setEditable(true);
        mGearSets.set3().setGears(mEntity.set3).setSwitched(mEntity.count>2);
        mGearSets.set4().setGears(mEntity.set4).setSwitched(mEntity.count>3);
        mGearSets.set5().setGears(mEntity.set5).setSwitched(mEntity.count>4);
        mGearSets.set6().setGears(mEntity.set6).setSwitched(mEntity.count>5);

        mOneSet.setValue(mEntity.oneSet);

        mFirstResultNumber.setValue(1);
        mLastResultNumber.setValue(1);
    }

    @Override
    public void save()
    {
        mEntity.mode = mCalculationMode.getValue();
        double r = mEntity.mode == G.CHG_GEARS_BY_RATIO || mEntity.mode == G.CHG_GEARS_BY_THREAD
                ? _calculatedRatio : 0.0;

        mEntity.accuracy = Math.pow(10, -_ratioPrecision);
        mEntity.ratio = r;
        mEntity.diffLocked23 = mDiffLockedZ2Z3.getValue();
        mEntity.diffLocked45 = mDiffLockedZ4Z5.getValue();
        mEntity.diffGearing12 = mDiffGearingZ1Z2.getValue();
        mEntity.diffGearing34 = mDiffGearingZ3Z4.getValue();
        mEntity.diffGearing56 = mDiffGearingZ5Z6.getValue();
        mEntity.oneSet = mOneSet.getValue();
        mEntity.count = mGearSets.getWheelsCount();
        mEntity.set0 = mGearSets.set0().gearsStr();
        mEntity.set1 = mGearSets.set1().gearsStr();
        mEntity.set2 = mGearSets.set2().gearsStr();
        mEntity.set3 = mGearSets.set3().gearsStr();
        mEntity.set4 = mGearSets.set4().gearsStr();
        mEntity.set5 = mGearSets.set5().gearsStr();
        mEntity.set6 = mGearSets.set6().gearsStr();

        mRepository.upsert(mEntity);
    }

    @Override
    public boolean validate()
    {
        if (!mEntity.oneSet)
        {
            Integer[] zs1 = mGearSets.set1().gears();
            Integer[] zs2 = mGearSets.set2().gears();
            Integer[] zs3 = mGearSets.set3().gears();
            Integer[] zs4 = mGearSets.set4().gears();
            Integer[] zs5 = mGearSets.set5().gears();
            Integer[] zs6 = mGearSets.set6().gears();
            int total = zs1.length > 0 ? zs1.length : 1;
            total *= zs2.length > 0 ? zs2.length : 1;
            total *= zs3.length > 0 ? zs3.length : 1;
            total *= zs4.length > 0 ? zs4.length : 1;
            total *= zs5.length > 0 ? zs5.length : 1;
            total *= zs6.length > 0 ? zs6.length : 1;
            if (total > 20000)
            {
                mNotificationEvent.setValue(Calculation.notifyMessage("Too much gears in sets!"));
                mCalculationEvent.setValue(new Event<>(null));
                return false;
            }
        }
        else
        {
            Integer[] set = Numbers.getIntegerNumbers(mEntity.set0);
            if (mEntity.count > set.length)
            {
                mNotificationEvent.setValue(Calculation.notifyMessage("The number of gears in set is less than number of wheels!"));
                mCalculationEvent.setValue(new Event<>(null));
                return false;
            }
        }

        return true;
    }

    @Override
    public void calculate()
    {
        save();

        if (!validate())
            return;

        clear();

        Data.Builder db = new Data.Builder();
        db.putLong(G.ChangeGearsId, mChangeGearsId);

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ChangeGearsWorker.class)
                .setInputData(db.build())
                .build();
        mCalculationEvent.setValue(new Event<>(request.getId()));

        WorkManager.getInstance(getApplication()).enqueue(request);
    }

    @Override
    public void clear()
    {
        mRepository.deleteResultsById(mChangeGearsId);
        mFirstResultNumber.setValue(1);
        mLastResultNumber.setValue(1);
        mNotificationEvent.setValue(Calculation.notifyAction(Calculation.NOTIFY_CLEAR));
    }

    @Override
    public boolean close()
    {
        return true;
    }

    @Override
    public void start()
    {
        mStarted = false;
        load();
        mStarted = true;
    }

    public GearSetsViewModel.GSet gearSet(int set)
    {
        return mGearSets.set(set);
    }

//    public void setGearSet(int set, String valuesStr)
//    {
//        Integer[] values = Numbers.getIntegerNumbers(valuesStr);
//        setGearSet(set, values);
//    }

    public void setGearSet(int set, Integer[] values)
    {
        String gearsStr = Numbers.getString(values);
        mGearSets.set(set).setGears(gearsStr);
        if (set > G.Z1 && set < G.Z6)
        {
            mGearSets.set(set+1).setEnabled(values.length > 0);
        }
    }

//    public void setGearSetSwitched(int set, boolean switched)
//    {
//        if (set < G.Z1 || set > G.Z6)
//            return;
//        mGearSets.set(set).setSwitched(switched);
//        if (switched)
//        {
//            mOneSetGearsCount = (set % 2) == 0 ? set : set - 1;
//            if (set < G.Z6)
//                mGearSets.set(set + 1).setEnabled(true);
//        }
//        else
//        {
//            int s = set - 1;
//            mOneSetGearsCount = (s % 2) == 0 ? s : s - 1;
//            for (set++; set <= G.Z6; set++)
//            {
//                GearSetsViewModel.GSet gk = mGearSets.set(set);
//                gk.setSwitched(false);
//                gk.setEnabled(false);
//            }
//        }
//    }

//    public void setGearSetEditable(int set, boolean editable)
//    {
//        mGearSets.set(set).setEditable(editable);
//    }

//    public void setGearSetEnabled(int set, boolean enabled)
//    {
//        mGearSets.set(set).setEnabled(enabled);
//    }

    private void switchOneSet(boolean value)
    {
        if (!mStarted)
            return;
        if (value)
        {
            mGearSets.set0().setEditable(true).setEnabled(true).setSwitched(true);
            mGearSets.set1().setEditable(false).setEnabled(false).setSwitched(true);
            mGearSets.set2().setEditable(false).setEnabled(false).setSwitched(true);
            mGearSets.set3().setEditable(false).setEnabled(true);
            mGearSets.set4().setEditable(false);
            mGearSets.set5().setEditable(false);
            mGearSets.set6().setEditable(false);

            for (int set = G.Z4; set <= G.Z6; set++)
            {
                GearSetsViewModel.GSet gk_n = mGearSets.set(set);
                GearSetsViewModel.GSet gk_p = mGearSets.set(set-1);
                gk_n.setEnabled(gk_p.isSwitched().getValue());
            }
        }
        else
        {
            mGearSets.set0().setEditable(false).setEnabled(false).setSwitched(false);
            mGearSets.set1().setEditable(true).setEnabled(false).setSwitched(true);
            mGearSets.set2().setEditable(true).setEnabled(false).setSwitched(true);
            mGearSets.set3().setEditable(true);
            mGearSets.set4().setEditable(true);
            mGearSets.set5().setEditable(true);
            mGearSets.set6().setEditable(true);

            boolean prevNotEmpty = !mGearSets.set(G.Z2).isEmpty();
            for (int set = G.Z3; set <= G.Z6; set++)
            {
                boolean notEmpty = !mGearSets.set(set).isEmpty();
                mGearSets.set(set).setEnabled(prevNotEmpty).setSwitched(notEmpty);
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
            case G.CHG_RATIOS_BY_GEARS:
                mLeadscrewPitchEnabled.setValue(false);
                mThreadPitchEnabled.setValue(false);
                mRatioEnabled.setValue(false);
                mRatioCalculatedEnabled.setValue(false);
                break;
            case G.CHG_THREAD_BY_GEARS:
                mLeadscrewPitchEnabled.setValue(true);
                mThreadPitchEnabled.setValue(false);
                mRatioEnabled.setValue(false);
                mRatioCalculatedEnabled.setValue(false);
                break;
            case G.CHG_GEARS_BY_RATIO:
                mLeadscrewPitchEnabled.setValue(false);
                mThreadPitchEnabled.setValue(false);
                mRatioEnabled.setValue(true);
                mRatioCalculatedEnabled.setValue(true);
                recalculateRatio();
                break;
            case G.CHG_GEARS_BY_THREAD:
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

    public MutableLiveData<Long> getRatioNumerator()
    {
        return mRatioNumerator;
    }

    public MutableLiveData<Long> getRatioDenominator()
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

    public LiveData<List<ChGearsResult>> getResultsToShow()
    {
        return mResultsToShow;
    }

    public LiveData<List<ChGearsResultViewModel>> getResultsVmToShow()
    {
        return mResultsVmToShow;
    }

    public int getNextResults()
    {
        int fi = mLastResultNumber.getValue() > 1 ? mLastResultNumber.getValue() + 1 : 1;
        int li = fi + 20;
        mFirstResultNumber.postValue(fi);
        mLastResultNumber.postValue(li);
        //List<ChGearsResult> chGearsResults = mRepository.getChGearsResults(mChangeGearsId, mFirstResultNumber.getValue(), mLastResultNumber.getValue());
        List<ChGearsResult> chGearsResults = mRepository.getChGearsResultsLive(mChangeGearsId);
        if (chGearsResults.size() == 0)
            return 0;
        mResultsToShow.postValue(chGearsResults);
        return chGearsResults.size();

//        int fi = mLastResultNumber.getValue() > 1 ? mLastResultNumber.getValue() + 1 : 1;
//        if (fi > mResults.size())
//            return 0;
//        int li = fi + 99;
//        if (li > mResults.size())
//            li = mResults.size();
//        mFirstResultNumber.postValue(fi);
//        mLastResultNumber.postValue(li);
//        List<ChangeGearsWorker.Result> next = mResults.subList(fi-1, li);
//        mResultsToShow.postValue(next);
//        return next.size();
    }

    public int getPrevResults()
    {
        int fi = mFirstResultNumber.getValue() - 20;
        if (fi < 0)
            return 0;
        int li = fi + 20;
        mFirstResultNumber.postValue(fi);
        mLastResultNumber.postValue(li);
        List<ChGearsResult> chGearsResults = mRepository.getChGearsResults(mChangeGearsId, mFirstResultNumber.getValue(), mLastResultNumber.getValue());
        if (chGearsResults.size() == 0)
            return 0;
        mResultsToShow.postValue(chGearsResults);
        return chGearsResults.size();
//        int fi = mFirstResultNumber.getValue() - 100;
//        if (fi < 0)
//            return 0;
//        int ti = fi + 99;
//        if (ti > mResults.size())
//            ti = mResults.size();
//        mFirstResultNumber.postValue(fi);
//        mLastResultNumber.postValue(ti);
//        List<ChangeGearsWorker.Result> prev = mResults.subList(fi-1, ti);
//        mResultsToShow.postValue(prev);
//        return prev.size();
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

        if (_calculationMode == G.CHG_GEARS_BY_THREAD)
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
                Fraction fr = tpf.divide(spf);
                _calculatedRatio = fr.toDouble();
                ratioInfo = "R = " + _threadPitch + " (" + _threadPitchUnit + ") / " +
                        _leadscrewPitch + " (" + _leadscrewPitchUnit + ") = " + fr.toString() +
                        " = " + mRatioFormat.format(_calculatedRatio);
            }
        }
        else if (_calculationMode == G.CHG_GEARS_BY_RATIO)
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
                    Fraction fract = Fraction.fromDouble(_ratioNumerator, _ratioDenominator);
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
}
