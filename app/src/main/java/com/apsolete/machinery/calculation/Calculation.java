package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.mvp.BaseContract;

public interface Calculation
{
    int CHANGEGEARS = 1;
    int GEARWHEEL   = 2;
    int GEARDRIVE   = 3;
    int FBELT       = 4;
	int VBELT       = 5;
	int PBELT       = 6;
	int TBELT       = 7;
	int CHAINDRIVE  = 8;

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
