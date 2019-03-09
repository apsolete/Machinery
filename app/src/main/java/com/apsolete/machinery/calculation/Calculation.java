package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.mvp.BaseContract;

public interface Calculation
{
    interface Contract
    {
        interface Presenter extends BaseContract.BasePresenter
        {
            void save();
            void clear();
            void calculate();
            boolean close();
            View getView();
        }

        interface View<P extends Presenter> extends BaseContract.BaseView<P>
        {
            SettingsBase getSettings();
            void showProgress(int percent);
        }
    }
}
