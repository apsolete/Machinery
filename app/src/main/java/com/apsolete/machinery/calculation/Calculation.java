package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.mvp.*;

public final interface Calculation
{
    public static final int CHANGEGEARS = 0;
    public static final int GEARWHEELS = 1;
    public static final int GEARDRIVE = 2;
    public static final int FBELT = 3;
	public static final int VBELT = 4;
	public static final int PBELT = 5;
	public static final int TBELT = 6;
	public static final int CHAINDRIVE = 7;

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
