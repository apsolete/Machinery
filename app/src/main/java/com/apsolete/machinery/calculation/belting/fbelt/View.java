package com.apsolete.machinery.calculation.belting.fbelt;

import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.SettingsBase;

public final class View extends CalculationView implements Contract.View
{
    public View(int type, int layout, int title)
    {
        super(type, layout, title);
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
