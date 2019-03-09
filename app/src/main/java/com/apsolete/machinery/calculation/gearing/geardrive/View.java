package com.apsolete.machinery.calculation.gearing.geardrive;

import com.apsolete.machinery.*;
import com.apsolete.machinery.common.*;
import com.apsolete.machinery.calculation.*;

public class View extends CalculationView implements Contract.View
{
    public View()
    {
        super(G.GEARDRIVE, R.layout.view_geardrive, R.string.title_geardrive);
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
