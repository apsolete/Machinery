package com.apsolete.machinery.calculation.belting.tbelt;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationView;
import com.apsolete.machinery.common.G;
import com.apsolete.machinery.common.SettingsBase;

public final class View extends CalculationView implements Contract.View
{
    public View()
    {
        super(G.TBELT, R.layout.view_tbelt, R.string.title_tbelt);
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
