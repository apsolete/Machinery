package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.mvp.*;

public interface Calculation
{
    int CHANGEGEARS = 0;
    int GEARWHEEL = 1;
    int GEARDRIVE = 2;
    int FBELT = 3;
	int VBELT = 4;
	int PBELT = 5;
	int TBELT = 6;
	int CHAINDRIVE = 7;

    interface Contract
    {
        interface Presenter extends BasePresenter
        {
            void save();
            void clear();
            void calculate();
            boolean close();
        }

        interface View<P extends Presenter> extends BaseView<P>
        {
            SettingsBase getSettings();
            void showProgress(int percent);
        }
    }
}
