package com.apsolete.machinery.calculation.changegears;

import com.apsolete.machinery.calculation.Calculation;

public interface ChangeGearsContract
{
    public interface Presenter extends Calculation.Contract.Presenter
    {
        void setOneGearsSet(boolean oneSet);
        void setGearsSet(int set, String valueStr);
        void setGearsSetChecked(int set, boolean checked);
    }

    public interface View extends Calculation.Contract.View<Presenter>
    {
        void setOneGearsSet(boolean oneSet);
        void setGearsSet(int set, String gearsStr);
        void setGearsSetChecked(int set, boolean checked);
        void setGearsSetEnabled(int set, boolean enabled);
        void setGearsSetEnableSet(int set, boolean enable);

        void setCalculationMode(int mode);

        void setThreadPitch(String valueStr);
        void showThreadPitch(boolean visible);

        void setLeadscrewPitch(String valueStr);
        void showLeadscrewPitch(boolean visible);

        void setRatio(String valueStr);
        void showRatio(boolean visible);

        void setRatioNumerator(String valueStr);
        void showRatioNumerator(boolean visible);

        void setRatioDenominator(String valueStr);
        void showRatioDenominator(boolean visible);

        void setFormattedRatio(String ratioStr);
        void showFormattedRatio(boolean visible);

        void setFirstResultNumber(String valueStr);
        void setLastResultNumber(String valueStr);
        void setResultItem(CgResult result);
        void clearResults();

        void showError(String error);
    }
}