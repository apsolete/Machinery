package com.apsolete.machinery.calculation.belting.fbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.Calculation;
import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.SettingsBase;

public final class View extends CalculationView implements Contract.View
{
    public View()
    {
        super(Calculation.FBELT, R.layout.view_fbelt, R.string.title_fbelt);
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
