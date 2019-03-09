package com.apsolete.machinery.calculation.gearing.gearwheel;

import com.apsolete.machinery.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.calculation.*;
import com.apsolete.machinery.common.mvp.BaseContract;

public class View extends CalculationView implements Contract.View
{
    public View()
    {
        super(G.GEARWHEEL, R.layout.view_gearwheel, R.string.title_gearwheel);
    }

    @Override
    public SettingsBase getSettings()
    {
        return null;
    }

    @Override
    public void showProgress(int percent)
    {

    }

    @Override
    public void setPresenter(Contract.Presenter presenter)
    {

    }
}
