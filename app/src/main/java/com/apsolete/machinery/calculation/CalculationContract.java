package com.apsolete.machinery.calculation;

import com.apsolete.machinery.common.SettingsBase;
import com.apsolete.machinery.common.mvp.BaseContract;

@Deprecated
public interface CalculationContract
{
	interface Model extends BaseContract.Model
    {
        void calculate();
    }

	@Deprecated
    interface Presenter extends BaseContract.Presenter
	{
		void save();
		void clear();
		void calculate();
		boolean close();
		View getView();
	}

	@Deprecated
	interface View<P extends Presenter> extends BaseContract.View<P>
	{
		SettingsBase getSettings();
		void showProgress(int percent);
	}
}
