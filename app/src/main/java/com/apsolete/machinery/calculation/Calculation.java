package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.mvp.*;

public final class Calculation
{
    public static final int CHANGEGEARS = 0;
    public static final int GEARWHEELS = 1;
    public static final int GEARWHEELSEXT = 2;
    public static final int BELTS = 3;

    public interface Contract
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
