package com.apsolete.machinery.calculation.gearing.changegears;

import com.apsolete.machinery.calculation.*;

import java.util.List;

public interface ChangeGearsContract
{
    interface Presenter extends Calculation.Contract.Presenter
    {
        void setOneGearKit(boolean oneKit);
        void setGearKit(int set, String valuesStr);
        void setGearKit(int set, List<Integer> values);
        void setGearKitChecked(int kit, boolean checked);
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

        int getNextResults();
        int getPrevResults();
    }

    interface Result
    {
        String id();
        String z1();
        String z2();
        String z3();
        String z4();
        String z5();
        String z6();
        String ratio();
        String threadPitch();
    }

    interface View extends Calculation.Contract.View<Presenter>
    {
        void setOneGearKit(boolean oneKit);
        void setGearKit(int kit, String gearsStr);
        void setGearKitChecked(int kit, boolean checked);
        void setGearKitEnabled(int kit, boolean enabled);
        void setGearKitEditable(int kit, boolean enable);

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
        void showResults(List<Result> results);
        void clearResults();

        void showMessage(String message);
    }
}
