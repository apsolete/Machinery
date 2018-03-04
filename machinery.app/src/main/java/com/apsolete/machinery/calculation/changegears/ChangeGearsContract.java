package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.Calculation;

import java.util.List;

public interface ChangeGearsContract
{
    interface Presenter extends Calculation.Contract.Presenter
    {
        void setOneGearsSet(boolean oneSet);
        void setGearsSet(int set, String valuesStr);
        void setGearsSet(int set, List<Integer> values);
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

        void getNextResults();
        void getPrevResults();
    }

    interface Result
    {
        int Number = 0;
        String Ratio = null;
        String Z1 = null;
        String Z2 = null;
        String Z3 = null;
        String Z4 = null;
        String Z5 = null;
        String Z6 = null;
    }

    interface View extends Calculation.Contract.View<Presenter>
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

        void setRatios(String ratioStr, String ratioNumStr, String ratioDenStr);
        void setRatioAsFration(boolean visible);
        void showRatio(boolean visible);
        void showRatioAsFration(boolean visible);

        void setFormattedRatio(String ratioStr);
        void showFormattedRatio(boolean visible);

        void setFirstResultNumber(String valueStr);
        void setLastResultNumber(String valueStr);
        void setResultItem(CgResult result);
        void showResults(List<Result> results);
        void clearResults();

        void showMessage(String message);
    }
}