package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.Calculation;

import java.util.ArrayList;

public interface ChangeGearsContract
{
    public interface Presenter extends Calculation.Contract.Presenter
    {
        void setOneGearsSet(boolean oneSet);
        void setGearsSet(int set, String valuesStr);
        void setGearsSet(int set, ArrayList<Integer> values);
        void setGearsSetChecked(int set, boolean checked);
        void setCalculationMode(int calcType);

        void setLeadscrewPitch(String valueStr);
        void setLeadscrewPitchUnit(ThreadPitchUnit unit);

        void setThreadPitch(String valueStr);
        void setThreadPitchUnit(ThreadPitchUnit unit);

        void setRatio(String valueStr);
        void setRatio(String numStr, String denStr);
        void setRatioNumerator(String valueStr);
        void setRatioDenominator(String valueStr);
        void setRatioFormat(int precision);
        void setRatioAsFraction(boolean asFraction);
    }

    public interface View extends Calculation.Contract.View<Presenter>
    {
        void setOneGearsSet(boolean oneSet);
        void setGearsSet(int set, String gearsStr);
        void setGearsSetChecked(int set, boolean checked);
        void setGearsSetEnabled(int set, boolean enabled);
        void setGearsSetEditable(int set, boolean enable);

        void setCalculationMode(int mode);

        void setThreadPitch(String valueStr);
        void setThreadPitchUnit(int unit);
        void showThreadPitch(boolean visible);

        void setLeadscrewPitch(String valueStr);
        void setLeadscrewPitchUnit(int unit);
        void showLeadscrewPitch(boolean visible);

        void setRatio(String valueStr);
        void showRatio(boolean visible);

        void setRatioNumerator(String valueStr);
        void setRatioDenominator(String valueStr);
        void setRatioAsFration(boolean visible);
        void showRatioAsFration(boolean visible);

        void setFormattedRatio(String ratioStr);
        void showFormattedRatio(boolean visible);

        void setFirstResultNumber(String valueStr);
        void setLastResultNumber(String valueStr);
        void setResultItem(CgResult result);
        void clearResults();

        void showError(String error);
    }
}